package com.example.votebe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyUser {
    String account = null;
    String password = null;

    @Autowired
    private MyUserDao myUserDao;

    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
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
        if(checkGroupId(groupId)) return myUserDao.getCompareSet(groupId);
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
        myUserDao.updateThumbs(T);
        return T.user + " press Thumbs " + T.rate + " to " + T.recordId;
    }

    public String removeThumbs(MyThumbs T){
        myUserDao.removeThumbs(T);
        return T.user + " Remove Thumbs to " + T.recordId;
    }

    public void record(MyRecord record){
        myUserDao.recordVoteResult(record);
        myUserDao.updateGroupObject(record);
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
        myUserDao.addTag(tag);
        return myUserDao.getTagId(tag);
    }
    public List<MyTag> getTags()
    {
        return myUserDao.getTagList();
    }
    public MyObject getObjectById(Integer objectId)
    {
        return myUserDao.getObjectById(objectId);
    }
}
