package com.example.votebe;

import com.example.votebe.MyUser;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MyUserRowMapper implements RowMapper<MyUser>
{

    @Override
    public MyUser mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        MyUser myUser;
        myUser = new MyUser();
        myUser.account = rs.getString("account");
        myUser.password = rs.getString("password");
        return myUser;
    }
}