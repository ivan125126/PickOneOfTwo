package com.example.votebe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class MyUser {
    String account = null;
    String password = null;

    @Autowired
    private MyUserDao myUserDao;

    public boolean checkPassword() {
        return myUserDao.isPasswordCorrect(this.account, this.password);
    }
    public boolean checkAccount() {
        return myUserDao.isAccountExist(this.account);
    }
    public String check(){
        if (this.checkAccount())
        {
            if (this.checkPassword())
            {
                return "Login Successful";
            }
            else
            {
                return "Password not correct";
            }
        }
        else
        {
            return "Account not exist";
        }
    }

    public String createAccount() {
        if(checkAccount())
        {
            return "Account already exist";
        }
        else
        {
            myUserDao.createAccount(this.account, this.password);
            return "Account created";
        }
    }
    public boolean checkGroupId(int groupId)
    {
        return myUserDao.isGroupIdExist(groupId);
    }

    public MyObject[] getCompareSet(String group){
        Integer groupId = myUserDao.getGroupId(group);
        if(checkGroupId(groupId)) {
            //不確定KneLu有沒有要這個功能所以做到一半
            MyObject[] objectSet = myUserDao.getCompareSet(groupId);
            objectSet[0].thumbs = new Random().nextInt(2)-1;//要改這裡
            objectSet[1].thumbs = new Random().nextInt(2)-1;
            return objectSet;
        }
        else return null;
    }

    // follow fcn
    public String follow(MyFollow F) {
        myUserDao.addFollow(F.account, F.objectId);

        return F.account + " Following " + F.objectId + " ><";
    }

    public String unFollow(MyFollow F) {
        myUserDao.unFollow(F.account, F.objectId);
        return F.account + " Unfollowing " + F.objectId + " TT";
    }

    public String addThumbs(MyThumbs T){
        if(T.newRate == 0){
            return removeThumbs(T);
        }else {
            T.rate = T.newRate == 1;
            if(myUserDao.checkThumbs(T)) myUserDao.updateThumbs(T);
            else myUserDao.addThumbs(T);
            return T.user + " press Thumbs " + T.rate + " to " + T.objectId;
        }
    }

    public String removeThumbs(MyThumbs T){
        myUserDao.removeThumbs(T);
        return T.user + " Remove Thumbs to " + T.objectId;
    }

    public String record(MyRecord record){
        if(checkRecord(record)) {
            myUserDao.updateChoiceResult(record);
            myUserDao.updateGroupObject_reVote(record);
            // undo 
            return "update";
        }
        else{
            myUserDao.addChoiceResult(record);
            myUserDao.updateGroupObject_new(record);
            return "add";
        }
    }
    public boolean checkRecord(MyRecord record){
        return myUserDao.isRecordExist(record);
    }

    public double calWinRate(int objId, String groupName){
        return myUserDao.winRateCounter(objId, groupName);
    }
    public String addObject(MyObject object)
    {
        object.id = myUserDao.addObject(object);
        for(int i = 0; i < object.tag.size(); i++)
        {
            addObjectTag(object.id, object.tag.get(i));
        }
        return "Object added";
    }
    public void addObjectTag(Integer objectId, String tag){
        myUserDao.addObjectTag(objectId, tag);
    }

    public Integer addTag(String tag)
    {
        if(!checkTag(tag)) myUserDao.addTag(tag);
        return myUserDao.getTagId(tag);
    }
    public Boolean checkTag(String tag){
        return myUserDao.isTagExist(tag);
    }
    public List<MyTag> getTags()
    {
        return myUserDao.getTagList();
    }
    public MyObject getObjectById(Integer objectId)
    {
        return myUserDao.getObjectById(objectId);
    }
    public Integer getUserThumb(MyThumbs myThumbs){
        if(myUserDao.checkThumbs(myThumbs))
        {
            Boolean result = myUserDao.getUserThumb(myThumbs.user, myThumbs.objectId);
            if (result == null) return 0;
            else return result ? 1 : -1;
        }
        else return 0;
    }
    public Integer addGroup(String group, List<String> tags){
        Integer groupId = myUserDao.addGroup(group);
        for (String tag : tags)
        {
            myUserDao.addTagToGroup(groupId, myUserDao.getTagId(tag));
        }
        List<Integer> objectsId = myUserDao.getObjectsWithAllTagsInGroup(groupId);
        for (Integer integer : objectsId)
        {
            myUserDao.creatGroupObject(integer, groupId);
        }
        return groupId;
    }

    public List<MyObject> getObjectsByName(String objectName)
    {
        List<MyObject> objects = myUserDao.getObjectsByName(objectName);
        for(MyObject object : objects){
            object.tag =myUserDao.getTagsOfObject(object.id) ;
        }
        return objects;
    }
    public List<MyTag> getTagsOfGroup(String groupName)
    {
        return myUserDao.getTagsOfGroup(myUserDao.getGroupId(groupName));
    }


    public List<String> getGroups()
    {
        return myUserDao.getGroups();
    }
}
