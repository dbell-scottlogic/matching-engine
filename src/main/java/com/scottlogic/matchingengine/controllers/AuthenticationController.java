package com.scottlogic.matchingengine.controllers;

import com.scottlogic.matchingengine.matcher.Matcher;
import com.scottlogic.matchingengine.dao.AccountJdbcDAO;
import com.scottlogic.matchingengine.dao.TradeJdbcDAO;
import com.scottlogic.matchingengine.entities.Account;
import com.scottlogic.matchingengine.models.AuthenticationRequest;
import com.scottlogic.matchingengine.models.AuthenticationResponse;
import com.scottlogic.matchingengine.security.CustomUserDetailsService;
import com.scottlogic.matchingengine.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("")
public class AuthenticationController {

    Matcher matcher;

    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Autowired
    AccountJdbcDAO accountJdbcDAO = new AccountJdbcDAO(jdbcTemplate);

    @Autowired
    TradeJdbcDAO tradeJdbcDAO = new TradeJdbcDAO(jdbcTemplate);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService();

    @Autowired
    JwtUtil jwtUtil = new JwtUtil();

    public AuthenticationController() {
    }

    public AuthenticationController(Matcher matcher) {
        this.matcher = matcher;
    }

    @GetMapping("/test")
    public String test(){
        return "Hello World";
    }


    @GetMapping("/getusername")
    public String getUsername(@RequestHeader String jwt){
        return jwtUtil.extractUsername(jwt.substring(7));
    }

    @GetMapping("/list")
    public List<Account> list() {
        return accountJdbcDAO.list();
    }

    @GetMapping("/create")
    public void create(@RequestParam String username) {
        Account account = new Account();
        account.setUsername(username);
        accountJdbcDAO.create(account);
    }

    @GetMapping("/read")
    public Account read(@RequestParam String username) {
        Optional<Account> optionalAccount = accountJdbcDAO.read(username);

        // Need alternative to this - dont want to return an empty account
        return optionalAccount.orElseGet(Account::new);
    }

    @RequestMapping(value = "authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Bad Credentials");
        }

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }


}


