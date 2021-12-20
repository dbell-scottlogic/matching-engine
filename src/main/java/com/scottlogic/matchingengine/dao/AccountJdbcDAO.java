package com.scottlogic.matchingengine.dao;

import com.scottlogic.matchingengine.entities.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;


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
        String sql = "SELECT accountId, username from accounts";
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
    public Optional<Account> read(int id) {
        String sql = "SELECT * FROM ACCOUNTS WHERE accountId = ?;";
        Account account = null;
        try{
            account = jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
        } catch (DataAccessException ex){
            log.info("Account not found: " + id);
        }
        return Optional.ofNullable(account);
    }

    @Override
    public void update(Account account, int id) {
        String sql = "UPDATE ACCOUNTS SET username =? WHERE accountId = ?;";
        int update = jdbcTemplate.update(sql, account.getUsername(), id);

        if (update == 1){
            log.info("Account updated for username: " + account.getUsername());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM ACCOUNTS WHERE accountId = ?;";
        jdbcTemplate.update(sql);
    }
}
