package com.example.votebe;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class MyController
{
    private static final Logger log = LoggerFactory.getLogger(MyController.class);
    @Autowired
    MyUser myUser;

    @RequestMapping("/test")
    public String hello()
    {
        return "Hello World";
    }
    //登入，傳入account(String),password(String),回傳Login Successful, Password not correct, Account not exist
    @PostMapping("/login")
    public String login(@RequestBody MyUser myUser)
    {
        this.myUser.account = myUser.account;
        this.myUser.password = myUser.password;
        return this.myUser.check();
    }
    //創建帳號，傳入account(String),password(String),回傳Account created, Account already exist
    @PostMapping("/createAccount")
    public String creatAccount(@RequestBody MyUser myUser)
    {
        this.myUser.account = myUser.account;
        this.myUser.password = myUser.password;
        return this.myUser.createAccount();
    }
    //取得比較的兩個物件，網址加/objectSet/groupName, groupName換成類別的名字，例:Animate, 回傳id(int), name(String), description(String), type(string), tag(List<String>), imageURL(String)
    @PostMapping("/objectSet/{group}")
    public MyObject[] getCompareSetById(@PathVariable String group)
    {
        return this.myUser.getCompareSet(group);
    }

    //追蹤，傳入account(String), objectId(int), {account} Following {objectId}><
    @RequestMapping("/follow/{user}/{objectId}")
    public String follow(@PathVariable String user, @PathVariable Integer objectId)
    {
        return this.myUser.follow(new MyFollow(user, objectId));
    }
    //取消追蹤，同上
    @RequestMapping("/unfollow/{user}/{objectId}")
    public String unfollow(@PathVariable String user, @PathVariable Integer objectId)
    {
        return this.myUser.unFollow(new MyFollow(user, objectId));
    }
    //按讚、按倒讚或更改讚，傳入user(String), objectId(int), rate(bool), rate為TRUE是讚,false是倒讚，回傳{user} press Thumbs rate to {objectId}
    @PostMapping("/updateThumbs")
    public String updateThumbs(@RequestBody MyThumbs myThumbs){
        return this.myUser.addThumbs(myThumbs);
    }
    //刪除讚，傳入user(String), objectId(int)，回傳Remove Thumbs to {objectId}
    @PostMapping("/removeThumbs")
    public String removeThumbs(@RequestBody MyThumbs myThumbs){
        return this.myUser.removeThumbs(myThumbs);
    }
    //紀錄選擇結果，傳入UserName, winnerId, loserId, group(int)
    @PostMapping("/record")
    public String record(@RequestBody MyRecord myRecord)
    {
        return this.myUser.record(myRecord);

    }
    //新增物件，傳入name(String), description(String), type(string), tag(List<String>), imageURL(String)
    @PostMapping("/addObject")
    public String addObject(@RequestBody MyObject myObject)
    {
        return this.myUser.addObject(myObject);
    }
    //取得物件資訊，下面網址{objectId}改objectId(int)
    @PostMapping("/object/{objectId}")
    public MyObject getObject(@PathVariable Integer objectId)
    {
        return myUser.getObjectById(objectId);
    }
    @PostMapping("/objectTag/{objectId}")
    public List<String> getObjectTag(@PathVariable Integer objectId){return myUser.getTagsOfObject(objectId);}
    @PostMapping("/objectGroup/{objectId}")
    public List<String> getObjectGroup(@PathVariable Integer objectId){return myUser.getGroupsOfObject(objectId);}
    //新增tag，傳入tag{String}
    @PostMapping("/addtag")
    public Integer addTag(@RequestBody MyTag tag){
        return myUser.addTag(tag.tag);
    }
    //取得全部的tag，回傳tagId(int),tag(String)的List
    @PostMapping("/tags")
    public List<MyTag> getTags(){
        return myUser.getTags();
    }
    //取得對對物件讚的狀態，傳入user(String), objectId(int),回傳-1(倒讚),0,1(讚)
    @PostMapping("/userThumbs")
    public Integer getUserThumbs(@RequestBody MyThumbs myThumbs)
    {
        return myUser.getUserThumb(myThumbs);
    }
    //新增類別，傳入tags(List<String>), group(String)，回傳類別ID(int)
    @PostMapping("/addGroup")
    public Integer addGroup(@RequestBody MyTagGroup myTagGroup){
        return myUser.addGroup(myTagGroup.group, myTagGroup.tags);
    }
    @RequestMapping("/winRate/{objectId}/{groupName}")
    public WinRateAndGames winRate(@PathVariable Integer objectId, @PathVariable String groupName){
        log.warn("objectId: {}, groupName: {}", objectId, groupName);
        return myUser.calWinRate(objectId, groupName);
    }

    @RequestMapping("/getRank/{groupName}/{rankInGroup}")
    public String getRank(@PathVariable String groupName, @PathVariable Integer rankInGroup){
        //log.error(rankInGroup+groupName);
        MyObject result = myUser.getObjectOfRank(rankInGroup, groupName);
        return ("{\n\"httpString\" : \""+result.imageURL+ "\",\n" + "\"IDNumber\" : " +"\""+ result.id+"\"\n}");
    }
    @RequestMapping("/notification")
    public String notification(){
        return "Spy x Family Season 2 has risen to No. 1 in Animate’s rankings";
    }

    @RequestMapping("/following/{user}")
    public List<ImageAndId> following(@PathVariable String user)
    {
        List<MyObject> objects = myUser.getFollowingObjects(user);
        List<ImageAndId> result = new ArrayList<>();
        for (MyObject myObject : objects){
            result.add(new ImageAndId(myObject.imageURL, myObject.id));
        }
        return result;
    }

    @RequestMapping("/isFollowing/{user}/{objectId}")
    public Boolean isFollowing(@PathVariable String user, @PathVariable Integer objectId)
    {
        return myUser.isFollowing(user, objectId);
    }
    // undo
    // @PostMapping("/thumbNotify")
    //getThumb
    //getObjectGroup
}