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
    @GetMapping("/object/{groupId}")
    public MyObject[] getCompareSetById(@PathVariable Integer groupId)
    {
        return this.myUser.getCompareSet(groupId);
    }

    @PostMapping("/follow")
    public String follow(@RequestBody MyFollow myFollow){
        return this.myUser.follow(myFollow);
    }

    @PostMapping("/unFollow")
    public String unFollow(@RequestBody MyFollow myUnFollow){
        return this.myUser.unFollow(myUnFollow);
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
    //@PostMapping("/addtag")
}
