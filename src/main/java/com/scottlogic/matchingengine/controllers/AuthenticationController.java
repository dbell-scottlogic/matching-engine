package com.scottlogic.matchingengine.controllers;

import com.scottlogic.matchingengine.Matcher;
import com.scottlogic.matchingengine.dao.AccountJdbcDAO;
import com.scottlogic.matchingengine.entities.Account;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    Matcher matcher;
    public HashMap<String, Account> accounts = new HashMap<String, Account>();

    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Autowired
    AccountJdbcDAO accountJdbcDAO = new AccountJdbcDAO(jdbcTemplate);


    public AuthenticationController(){
        Account acc1 = new Account(1, "David");
        Account acc2 = new Account(2, "Steve");
        accounts.put(acc1.getUsername(), acc1);
        accounts.put(acc2.getUsername(), acc2);
    }

    public AuthenticationController(Matcher matcher) {
        this.matcher = matcher;
    }

    @GetMapping("/list")
    public List<Account> list(){
        return accountJdbcDAO.list();
    }

    @GetMapping("/create")
    public void create(@RequestParam String username){
        Account account = new Account();
        account.setUsername(username);
        accountJdbcDAO.create(account);
    }

    @GetMapping("/read")
    public Account read(@RequestParam int id){
        Optional<Account> optionalAccount = accountJdbcDAO.read(id);
        if (optionalAccount.isPresent()){
            return optionalAccount.get();
        } else {
            Account nullAccount = new Account();
            return nullAccount;
        }

    }

    @PostMapping("/login")
    public Account login(@RequestParam String username, String password){

        //H2 database check here

        if (accounts.containsKey(username)) {
            String token = getJWTToken(username);
            Account account = accounts.get(username);
            account.setUsername(username);
            account.setToken(token);
            return account;
        } else {
            Account account = new Account();
            account.setUsername(null);
            account.setAccountId(0);
            account.setToken(null);
            return account;
        }

    }

    private String getJWTToken(String username) {

        SecretKey key = Keys.hmacShaKeyFor(new byte[512]);
        List<GrantedAuthority> grantedAuthorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
        String token = Jwts
                .builder()
                .setId("customJWT")
                .setSubject(username)
                .claim("authorities", grantedAuthorityList.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        key).compact();

        return "Bearer " + token;
    }

    //Token based approach
    //Login
    //Logout
    //New Account
    //Modify account



}
