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
    @PostMapping("/creataccount")
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
}