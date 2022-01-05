package com.scottlogic.matchingengine.security;

import com.scottlogic.matchingengine.dao.AccountJdbcDAO;
import com.scottlogic.matchingengine.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Autowired
    AccountJdbcDAO accountJdbcDAO = new AccountJdbcDAO(jdbcTemplate);

    JwtUtil jwtUtil = new JwtUtil();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Can call DB here

        if(accountJdbcDAO.read(username).isPresent()){
            User user = new User(accountJdbcDAO.read(username).get().getUsername(), "Pass", new ArrayList<>());
            return user;
        } else throw new UsernameNotFoundException("Username: " + username + " not found");
    }
}
