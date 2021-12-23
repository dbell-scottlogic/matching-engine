package com.scottlogic.matchingengine.dao;

import com.scottlogic.matchingengine.entities.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Component
public class AccountJdbcDAO implements DAO<Account> {

    private static final Logger log = LoggerFactory.getLogger(AccountJdbcDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    RowMapper<Account> rowMapper = (rs, rowNum) -> {
        Account account = new Account();
        account.setAccountId(rs.getInt("accountId"));
        account.setUsername(rs.getString("username"));
        return account;
    };

    public AccountJdbcDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AccountJdbcDAO() {

    }

    @Override
    public List<Account> list() {
        String sql = "SELECT * from accounts";
        log.info("Listing accounts");
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void create(Account account) {
        String sql = "INSERT INTO ACCOUNTS (username) VALUES (?);";
        log.info("New account created for username: " + account.getUsername());
        jdbcTemplate.update(sql, account.getUsername());
    }

    @Override
    public Optional<Account> read(String username) {
        String sql = "SELECT * FROM ACCOUNTS WHERE username = ?;";
        Account account = null;
        try{
            account = jdbcTemplate.queryForObject(sql, new Object[]{username}, rowMapper);
        } catch (DataAccessException ex){
            log.info("Account not found: " + username);
        }
        return Optional.ofNullable(account);
    }

    @Override
    public void update(Account account, String username){
        String sql = "UPDATE ACCOUNTS SET username = ? WHERE username = ?;";
        int update = jdbcTemplate.update(sql, account.getUsername(), username);

        if (update == 1){
            log.info("Account updated for username: " + account.getUsername());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM ACCOUNTS WHERE accountId = ?;";
        jdbcTemplate.update(sql);
    }

   public byte[] getHashedPassword(String username) throws NoSuchAlgorithmException {
        String sql = "SELECT password FROM ACCOUNTS where username = ?;";
       return jdbcTemplate.queryForObject(sql, new Object[]{username}, byte[].class);
   }

   public Boolean isMatched(String username, String password) throws NoSuchAlgorithmException {
       MessageDigest digest = MessageDigest.getInstance("SHA-512");
       digest.reset();
       digest.update(password.getBytes(StandardCharsets.UTF_8));
       String hashedPassword = String.format("%0128x", new BigInteger(1, digest.digest()));

       System.out.println(hashedPassword);

       String sql = "SELECT username FROM ACCOUNTS where username = ? AND password = ?;";
       String selectedUsername = jdbcTemplate.queryForObject(sql, new Object[]{username, hashedPassword}, String.class);

        if (selectedUsername !=null && selectedUsername.equals(username)){
            return true;
        } else return false;
   }
}
