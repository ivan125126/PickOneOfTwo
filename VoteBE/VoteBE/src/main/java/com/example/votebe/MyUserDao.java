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

import javax.swing.plaf.PanelUI;

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

    public void addTagToGroup(int groupId, int tagId){
        String sql = "INSERT INTO taggroup (groupId, tagId) VALUES (:groupId, :tagId)";
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("tagId", tagId);
        namedParameterJdbcTemplate.update(sql, params);
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
    public Integer getGroupId(String groupName){
        String sql = "SELECT id FROM myGroup WHERE groupName = :groupName";
        Map<String, Object> params = new HashMap<>();
        params.put("groupName", groupName);
        try
        {
            return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
        }catch (DataAccessException e) {
            log.error(e.getMessage());
        }
        return -1;
    }
    public MyObject[] getCompareSet(int groupId)
    {
        int firstObject, secondObject;
        List<Integer> objectsId = getObjectsWithAllTagsInGroup(groupId);
        if (objectsId.isEmpty()) return null;
        firstObject = objectsId.get(new Random().nextInt(objectsId.size()));
        do
        {
            secondObject = objectsId.get(new Random().nextInt(objectsId.size()));
        }while (firstObject == secondObject);
        return new MyObject[] {getObjectById(firstObject), getObjectById(secondObject)};
    }
    public MyObject getObjectById(int id)
    {
        String sql = "SELECT * " +
                "FROM object o " +
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
                myObject.imageURL = rs.getString("imageURL");
                return myObject;
            }
        }).get(0);
    }

    public List<Integer> getObjectsWithAllTagsInGroup(int groupId)
    {
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

    public List<Integer> getFollowingObjects(String user)
    {
        String sql = "SELECT object FROM follow WHERE user = :user";
        Map<String, Object> params = new HashMap<>();
        params.put("user", user);
        return namedParameterJdbcTemplate.query(sql, params, new  RowMapper<Integer>() {
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                return rs.getInt("objId");
            }
        });


    }
    public Integer addGroup(String groupName){
        String sql = "INSERT INTO myGroup(groupName) VALUES (:groupName)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("groupName", groupName);
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

    public Integer creatGroupObject(Integer objectId, Integer groupId){
        String sql = "INSERT INTO myGroup(objectId, groupId, winGames, games) VALUES (:objectId, :groupId, 0, 0)";
        Map<String, Object> params = new HashMap<>();
        params.put("objectId", objectId);
        params.put("groupId", groupId);
        try
        {
            namedParameterJdbcTemplate.update(sql, params);
            return 1;
        }catch (DataAccessException e){
            log.error(e.getMessage());
            return 0;
        }
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

    public void unFollow(String user, int objectId){
        String sql = "DELETE FROM follow as f WHERE f.user = :user AND objectId = :objectId";
        Map<String, Object> params = new HashMap<>();
        params.put("user", user);
        params.put("objectId", objectId);
        try {
            namedParameterJdbcTemplate.update(sql, params);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
        }
    }

    public Boolean checkThumbs(MyThumbs myThumbs)
    {
        String sql = "SELECT COUNT(*) FROM thumbs WHERE thumbs.user = :user AND objectId = :objectId";
        Map<String, Object> params = new HashMap<>();
        params.put("user", myThumbs.user);
        params.put("objectId", myThumbs.objectId);
        try
        {
            Integer count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
            return count != null && count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
    public void updateThumbs(MyThumbs T){
        String sql = "UPDATE Thumbs SET Thumbs.rate = :rate WHERE user = :user AND objectId = :objectId";
        Map<String, Object> params = new HashMap<>();
        params.put("user", T.user);
        params.put("objectId", T.objectId);
        params.put("rate", T.rate);
        try
        {
            namedParameterJdbcTemplate.update(sql, params);
        }catch (DataAccessException e){
            log.error(e.getMessage());
        }
    }
    public void addThumbs(MyThumbs T){
        String sql = "INSERT INTO Thumbs(user, objectId, rate) VALUES (:user, :objectId, :rate)";
        Map<String, Object> params = new HashMap<>();
        params.put("user", T.user);
        params.put("objectId", T.objectId);
        params.put("rate", T.rate);
        try
        {
            namedParameterJdbcTemplate.update(sql, params);
        }catch (DataAccessException e){
            log.error(e.getMessage());
        }
    }

    public void removeThumbs(MyThumbs T){
        String sql = "DELETE FROM Thumbs as t WHERE t.user = :user AND objectId = :objectId";;
        Map<String, Object> params = new HashMap<>();
        params.put("user", T.user);
        params.put("objectId", T.objectId);
        try
        {
            namedParameterJdbcTemplate.update(sql, params);
        }catch (DataAccessException e){
            log.error(e.getMessage());
        }
    }

    public void thumbsDown(MyThumbs T){
        String sql = "INSERT INTO Thumbs VALUES (:user, :recordId, :rate)";
        Map<String, Object> params = new HashMap<>();
        params.put("user", T.user);
        params.put("recordId", T.objectId);
        params.put("rate", T.rate);
        try
        {
            namedParameterJdbcTemplate.update(sql, params);
        }catch (DataAccessException e){
            log.error(e.getMessage());
        }
    }

    public void recordChoiceResult(MyRecord myRecord){
        String sql = "INSERT INTO record(user, winnerId, loserId, groupId) VALUES (:user, :winner, :loser, :groupId)";
        Map<String, Object> params = new HashMap<>();
        params.put("user", myRecord.UserName);
        params.put("winner", myRecord.winnerId);
        params.put("loser", myRecord.loserId);
        params.put("groupId", getGroupId(myRecord.group));
        try
        {
            namedParameterJdbcTemplate.update(sql, params);
        }catch (DataAccessException e){
            log.error(e.getMessage());
        }
    }

    public int addObject(MyObject myObject)
    {
        String sql = "INSERT INTO object(objectName, objectInformation, objectType, imageURL) VALUES (:name, :Info, :type, :imageURL)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", myObject.name)
                .addValue("Info", myObject.description)
                .addValue("type", myObject.type)
                .addValue("imageURL", myObject.imageURL);
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
    //插入新的object與tag的關聯
    public void addObjectTag(int objectId, String tag){
        Integer tagId = getTagId(tag);
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
    public Integer getTagId(String tag){
        String sql = "SELECT tagId FROM tagGroup WHERE tag = :tag";
        Map<String, Object> params = new HashMap<>();
        params.put("tag", tag);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
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
    public Boolean isTagExist(String tag){
        String sql = "SELECT COUNT(*) FROM user WHERE name = :tagName";
        Map<String, Object> params = new HashMap<>();
        params.put("tagName", tag);
        try {
            Integer count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
            return count != null && count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
    public List<MyTag> getTagList()
    {
        String sql = "SELECT * FROM tag";

        return namedParameterJdbcTemplate.query(sql, new RowMapper<MyTag>(){
            public MyTag mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                MyTag tag = new MyTag();
                tag.tagId = rs.getInt("id");
                tag.tag = rs.getString("name");
                return tag;
            }
        });

    }

    public Boolean getUserThumb(String user, int objectId){
        String sql = "SELECT rate FROM thumbs WHERE thumbs.user = :user AND objectId = :objectId";
        Map<String, Object> params = new HashMap<>();
        params.put("user", user);
        params.put("objectId", objectId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Boolean.class);
    }


}


