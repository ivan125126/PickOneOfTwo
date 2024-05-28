package com.example.votebe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.springframework.dao.EmptyResultDataAccessException;

@Component
public class MyUserDao {

    private static final Logger log = LoggerFactory.getLogger(MyUserDao.class);

    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public boolean isAccountExist(String account) {
        String sql = "SELECT COUNT(*) FROM user WHERE account = :account";
        Map<String, Object> params = new HashMap<>();
        params.put("account", account);
        try {
            Integer count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
            return count != null && count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
    public boolean isPasswordCorrect(String account, String password) {
        String sql = "SELECT COUNT(*) FROM USER WHERE account = :account AND password = :password";
        Map<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("password", password);
        try {
            Integer count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
            return count != null && count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public boolean isGroupIdExist(int groupId)
    {
        String sql = "SELECT COUNT(*) FROM taggroup WHERE groupId = :groupId";
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        try
        {
            Integer count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
            return count != null && count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
    public void createAccount(String account, String password)
    {
        String sql = "INSERT INTO USER VALUES (:account, :password)";
        Map<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("password", password);
        try {
            namedParameterJdbcTemplate.update(sql, params);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
        }
    }

    public MyObject[] getCompareSet(int groupId)
    {
        int firstObject, secondObject;
        List<Integer> objectsId = findObjectsWithAllTagsInGroup(groupId);
        if (objectsId.isEmpty()) return null;
        firstObject = objectsId.get(new Random().nextInt(objectsId.size()));
        do
        {
            secondObject = objectsId.get(new Random().nextInt(objectsId.size()));
        }while (firstObject != secondObject);
        return new MyObject[] {getObjectById(firstObject), getObjectById(secondObject)};
    }
    public MyObject getObjectById(int id)
    {
        String sql = "SELECT *" +
                "FROM object o" +
                "WHERE o.id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return namedParameterJdbcTemplate.query(sql, params, new RowMapper<MyObject>()
        {
            public MyObject mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyObject myObject = new MyObject();
                myObject.id = rs.getInt("id");
                myObject.name = rs.getString("objectName");
                myObject.description = rs.getString("objectInformation");
                myObject.type = rs.getString("objectType");
                myObject.pictureId = rs.getInt("pictureId");
                return myObject;
            }
        }).get(0);
    }

    public List<Integer> findObjectsWithAllTagsInGroup(int groupId) {
        String sql = "SELECT ot.objId " +
                "FROM objectTag ot " +
                "JOIN tagGroup tg ON ot.tagId = tg.tagId " +
                "WHERE tg.groupId = :groupId " +
                "GROUP BY ot.objId " +
                "HAVING COUNT(DISTINCT ot.tagId) = ( " +
                "    SELECT COUNT(DISTINCT tagId) " +
                "    FROM tagGroup " +
                "    WHERE groupId = :groupId " +
                ")";

        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);

        return namedParameterJdbcTemplate.query(sql, params, new RowMapper<Integer>() {
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                return rs.getInt("objId");
            }
        });
    }

    public void addFollow(String account, int objectId){
        String sql = "INSERT INTO follow VALUES (:account, :objectId)";
        Map<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("objectId", objectId);
        try {
            namedParameterJdbcTemplate.update(sql, params);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
        }
    }

    public void unFollow(String account, int objectId){
        String sql = "DELETE FROM follow WHERE account = :account AND objectId = :objectId";
        Map<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("objectId", objectId);
        try {
            namedParameterJdbcTemplate.update(sql, params);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
        }
    }

    public void recordChoiceResult(MyRecord myRecord){
        String sql = "INSERT INTO record VALUES (:user, :winner, :loser, :groupId)";
        Map<String, Object> params = new HashMap<>();
        params.put("user", myRecord.UserName);
        params.put("winner", myRecord.winnerId);
        params.put("loser", myRecord.loserId);
        params.put("groupId", myRecord.groupId);
        try
        {
            namedParameterJdbcTemplate.update(sql, params);
        }catch (DataAccessException e){
            log.error(e.getMessage());
        }
    }

    public int addObject(MyObject myObject)
    {
        String sql = "INSERT INTO object(objectName, objectInformation, objectType, pictureId) VALUES (:name, :Info, :type, :pictureId)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", myObject.name)
                .addValue("Info", myObject.description)
                .addValue("type", myObject.type)
                .addValue("pictureId", myObject.pictureId);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try
        {
            namedParameterJdbcTemplate.update(sql, params, keyHolder);
        }catch (DataAccessException e){
            log.error(e.getMessage());
        }
        // Retrieve the generated key
        Number id = keyHolder.getKey();
        if (id != null)
        {
            return id.intValue();
        }
        return 0;
    }
    public void addObjectTag(int objectId, int tagId){
        String sql = "INSERT INTO objecttag VALUES (:objectId, :tagId)";
        Map<String, Object> params = new HashMap<>();
        params.put("objectId", objectId);
        params.put("tagId", tagId);
        try
        {
            namedParameterJdbcTemplate.update(sql, params);
        }catch (DataAccessException e){
            log.error(e.getMessage());
        }
    }

    public void addTag(String tag)
    {
        String sql = "INSERT INTO tag VALUES (:tag)";
        Map<String, Object> params = new HashMap<>();
        params.put("tag", tag);
        try
        {
            namedParameterJdbcTemplate.update(sql, params);
        }catch (DataAccessException e)
        {
            log.error(e.getMessage());
        }
    }

    public List<MyTag> getTagList()
    {
        String sql = "SELECT * FROM tag";

        return namedParameterJdbcTemplate.query(sql, new RowMapper<MyTag>(){
            public MyTag mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                MyTag tag = new MyTag();
                tag.tagId = rs.getInt("tagId");
                tag.tag = rs.getString("tag");
                return tag;
            }
        });

    }


}


