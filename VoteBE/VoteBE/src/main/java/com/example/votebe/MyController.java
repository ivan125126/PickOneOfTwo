package com.example.votebe;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MyController
{
    @Autowired
    MyUser myUser;

    @RequestMapping("/test")
    public String hello()
    {
        return "Hello World";
    }

    @PostMapping("/login")
    public String login(@RequestBody MyUser myUser)
    {
        this.myUser.account = myUser.account;
        this.myUser.password = myUser.password;
        return this.myUser.check();
    }
    @PostMapping("/createAccount")
    public String creatAccount(@RequestBody MyUser myUser)
    {
        this.myUser.account = myUser.account;
        this.myUser.password = myUser.password;
        return this.myUser.createAccount();
    }
    @PostMapping("/objectSet/{group}")
    public MyObject[] getCompareSetById(@PathVariable String group)
    {
        return this.myUser.getCompareSet(group);
    }

    @PostMapping("/follow")
    public String follow(@RequestBody MyFollow myFollow){
        return this.myUser.follow(myFollow);
    }

    @PostMapping("/unFollow")
    public String unFollow(@RequestBody MyFollow myUnFollow){
        return this.myUser.unFollow(myUnFollow);
    }
    @PostMapping("/thumbUp")
    public String thumbsUp(){
        return "Still thinking 要倒讚嗎";
    }

    @PostMapping("/groupTheTag")
    public String groupTheTag() {
        return"undo";
    }


    @PostMapping("/record")
    public String record(@RequestBody MyRecord myRecord)
    {
        this.myUser.record(myRecord);
        return "success";
    }
    @PostMapping("/addObject")
    public String addObject(@RequestBody MyObject myObject)
    {
        return this.myUser.addObject(myObject);
    }
    @GetMapping("/object/{objectId}")
    public MyObject getObject(@PathVariable Integer objectId)
    {
        return myUser.getObjectById(objectId);
    }
    @PostMapping("/addtag")
    public Integer addTag(@RequestBody MyTag tag){
        return myUser.addTag(tag.tag);
    }

}
