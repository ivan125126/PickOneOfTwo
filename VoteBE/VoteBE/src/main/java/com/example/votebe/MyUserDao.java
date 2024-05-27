package com.example.votebe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.springframework.dao.EmptyResultDataAccessException;

import javax.security.auth.login.AccountException;

@Component
public class MyUserDao {

    private static final Logger log = LoggerFactory.getLogger(MyUserDao.class);
    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public boolean isAccountExist(String account) {
        String sql = "SELECT COUNT(*) FROM USER WHERE account = :account";
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
        String sql = "INSERT INTO USER (account, password) VALUES (:account, :password)";
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
                myObject.description = rs.getString("objectDescription");
                myObject.type = rs.getString("category");
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
}

