package com.example.votebe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
}

