package com.scottlogic.matchingengine;

import com.scottlogic.matchingengine.dao.AccountJdbcDAO;
import com.scottlogic.matchingengine.entities.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@SpringBootTest
public class AccountJdbcDAOTests {


    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Autowired
    AccountJdbcDAO accountJdbcDAO = new AccountJdbcDAO(jdbcTemplate);

    @Test
    public void shouldReturnHash() throws NoSuchAlgorithmException {
        Account account = accountJdbcDAO.getHashedPassword("David97");
        System.out.println(account.toString());

    }

}
