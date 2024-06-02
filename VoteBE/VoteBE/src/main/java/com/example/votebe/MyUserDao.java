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

import javax.xml.transform.Result;

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
    public boolean isRecordExist(MyRecord record){
        String sql = "SELECT COUNT(*) FROM record WHERE record.user = :user AND ((winnerId = :object1 AND loserId = :object2) OR (winnerId = :object2 AND loserId = :object1)) AND groupId = :groupId";
        Map<String, Object> params = new HashMap<>();
        params.put("user", record.UserName);
        params.put("object1", record.winnerId);
        params.put("object2", record.loserId);
        params.put("groupId", getGroupId(record.group));
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
    public List<Integer> getObjectsIdByTag(Integer tagId)
    {
        String sql = "SELECT ot.objId " +
                "FROM objectTag ot " +
                "WHERE ot.tagId = :tagId ";
        Map<String, Object> params = new HashMap<>();
        params.put("tagId", tagId);

        return namedParameterJdbcTemplate.query(sql, params, new RowMapper<Integer>() {
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                return rs.getInt("objId");
            }
        });
    }
    public List<Integer> getTagsIdOfObject(Integer objectId)
    {
        String sql = "SELECT ot.tagId " +
                "FROM objectTag ot " +
                "WHERE ot.objectId = :objectId ";
        Map<String, Object> params = new HashMap<>();
        params.put("objectId", objectId);

        return namedParameterJdbcTemplate.query(sql, params, new RowMapper<Integer>() {
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                return rs.getInt("tagId");
            }
        });
    }
    public List<String> getTagsOfObject(Integer objectId)
    {
        String sql = "SELECT t.name " +
                "FROM objectTag ot JOIN tag t ON ot.tagId = t.id " +
                "WHERE ot.objId = :objectId ";
        Map<String, Object> params = new HashMap<>();
        params.put("objectId", objectId);

        return namedParameterJdbcTemplate.query(sql, params, new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                return rs.getString("name");
            }
        });
    }
    public List<MyObject> getObjectsByName(String name)
    {
        String sql = "SELECT * " +
                "FROM object " +
                "WHERE objectName = :objName ";
        Map<String, Object> params = new HashMap<>();
        params.put("objName", name);

        return namedParameterJdbcTemplate.query(sql, params, new RowMapper<MyObject>() {
            public MyObject mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                MyObject myObject = new MyObject();
                myObject.id = rs.getInt("id");
                myObject.name = rs.getString("objectName");
                myObject.description = rs.getString("objectInformation");
                myObject.type = rs.getString("objectType");
                myObject.imageURL = rs.getString("imageURL");
                return myObject;
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
        String sql = "INSERT INTO groupObject(objectId, groupId, winGames, games) VALUES (:objectId, :groupId, 0, 0)";
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

    public void addChoiceResult(MyRecord myRecord){
        // Put result into table record
        String sql = "INSERT INTO record (user, winnerId, loserId, groupId) VALUES (:user, :winner, :loser, :groupId)";
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
    public void updateChoiceResult(MyRecord myRecord){
        String sql = "UPDATE record SET winnerId = :winner, loserId = :loser WHERE user = :user AND groupId = :groupId AND winnerId = :loser AND loserId = :winner";
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

    public MyGroupObject getGroupObject(int objId, int gId){
        String sql = "SELECT * FROM groupobject WHERE objectId = :objId, groupId = :gId";
        Map<String, Object> params = new HashMap<>();
        params.put("objId", objId);
        params.put("gId", gId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, new RowMapper<MyGroupObject>(){
            public MyGroupObject mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                MyGroupObject GO = new MyGroupObject();
                GO.objId = rs.getInt("objectId");
                GO.gId = rs.getInt("groupId");
                GO.winGames = rs.getInt("winGames");
                GO.games = rs.getInt("games");
                GO.rankInGroup = rs.getInt("rankInGroup");
                return GO;
            }
        });
    }

    // update the result into Table groupObject
    public void updateGroupObject_new(MyRecord myRecord){
        // update the new value to the group_object

        // winner
        String sql = "UPDATE groupobject SET winGames = :winGames, games = :games WHERE objectId = :winnerId AND groupId = :gId";
        // get the winGames and games
        MyGroupObject winGO = getGroupObject(myRecord.winnerId, myRecord.groupId);
        Map<String, Object> paramsWin = new HashMap<>();
        paramsWin.put("winGames", winGO.winGames+1);
        paramsWin.put("games", winGO.games+1);
        paramsWin.put("winnerId", myRecord.winnerId);
        paramsWin.put("gId", myRecord.groupId);
        try
        {
            namedParameterJdbcTemplate.update(sql, paramsWin);
        }catch (DataAccessException e) {
            log.error("Error update winner", e.getMessage());
        }

        // loser
        // update the new value to the group_object
        String sql1 = "UPDATE groupobject SET games = :games WHERE objectId = :loserId AND groupId = :gId";
        MyGroupObject loseGO = getGroupObject(myRecord.loserId, myRecord.groupId);
        Map<String, Object> paramsLose = new HashMap<>();
        paramsLose.put("games", loseGO.games+1);
        paramsLose.put("loserId", myRecord.loserId);
        paramsLose.put("gId", myRecord.groupId);
        try
        {
            namedParameterJdbcTemplate.update(sql1, paramsLose);
        }catch (DataAccessException e){
            log.error("Error update loser", e.getMessage());
        }
    }

    public void updateGroupObject_reVote(MyRecord myRecord){
        // update the new value to the group_object
        // winner
        String sql = "UPDATE groupobject SET winGames = :winGames WHERE objectId = :winnerId AND groupId = :gId";
        // get the winGames and games
        MyGroupObject winGO = getGroupObject(myRecord.winnerId, myRecord.groupId);
        Map<String, Object> paramsWin = new HashMap<>();
        paramsWin.put("winGames", winGO.winGames+1);
        paramsWin.put("winnerId", myRecord.winnerId);
        paramsWin.put("gId", myRecord.groupId);
        try
        {
            namedParameterJdbcTemplate.update(sql, paramsWin);
        }catch (DataAccessException e) {
            log.error("Error update winner", e.getMessage());
        }

        // loser
        // update the new value to the group_object
        String sql1 = "UPDATE groupobject SET wiGames = :winGames WHERE objectId = :loserId AND groupId = :gId";
        MyGroupObject loseGO = getGroupObject(myRecord.loserId, myRecord.groupId);
        Map<String, Object> paramsLose = new HashMap<>();
        paramsWin.put("winGames", loseGO.winGames-1);
        paramsLose.put("loserId", myRecord.loserId);
        paramsLose.put("gId", myRecord.groupId);
        try
        {
            namedParameterJdbcTemplate.update(sql1, paramsLose);
        }catch (DataAccessException e){
            log.error("Error update loser", e.getMessage());
        }
    }
    
//    calculate the win rate
    public double winRateCounter(int objId, int gId){
        MyGroupObject GO = getGroupObject(objId, gId);
        return GO.calWinRate();
    }

    public void updateRank(MyRecord record){

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
        String sql = "SELECT id FROM tag WHERE name = :tag";
        Map<String, Object> params = new HashMap<>();
        params.put("tag", tag);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }
    public String getTagById(Integer tagId){
        String sql = "SELECT name FROM tag WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", tagId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, String.class);
    }



    public void addTag(String tag)
    {
        String sql = "INSERT INTO tag (name) VALUES (:tag)";
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
        String sql = "SELECT COUNT(*) FROM tag WHERE name = :tagName";
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


    public List<String> getGroups()
    {
        String sql = "SELECT groupName FROM mygroup";

        return namedParameterJdbcTemplate.query(sql, new RowMapper<String>(){
            public String mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                return rs.getString("groupName");
            }
        });
    }

    public List<MyTag> getTagsOfGroup(Integer groupId)
    {
        String sql = "SELECT tag.id,tag.name FROM tag JOIN taggroup ON tag.id = taggroup.tagId WHERE tagGroup.groupId = :groupId";
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        try
        {
            return namedParameterJdbcTemplate.query(sql, params, new RowMapper<MyTag>(){
                public MyTag mapRow(ResultSet rs, int rowNum) throws SQLException
                {
                    MyTag tag = new MyTag();
                    tag.tag = rs.getString("name");
                    tag.tagId = rs.getInt("id");
                    return tag;
                }
            });
        }catch (DataAccessException e){
            log.error(e.getMessage());
        }
        return null;
    }
}


