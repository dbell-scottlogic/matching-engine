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
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AccountJdbcDAOTests {


    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Autowired
    AccountJdbcDAO accountJdbcDAO = new AccountJdbcDAO(jdbcTemplate);

    @Test
    public void shouldReturnHash() throws NoSuchAlgorithmException {
        byte[] data = accountJdbcDAO.getHashedPassword("David97");
        String base64 = Base64.getEncoder().encodeToString(data);
        System.out.println(base64);
       // System.out.println(new String(data, StandardCharsets.UTF_8));

    }

    @Test
    public void passwordShouldMatch() throws NoSuchAlgorithmException {
        assertTrue(accountJdbcDAO.isMatched("David97", "pass"));
    }

}
