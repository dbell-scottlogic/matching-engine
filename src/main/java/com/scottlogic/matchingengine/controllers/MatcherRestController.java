package com.scottlogic.matchingengine.controllers;

import com.scottlogic.matchingengine.Matcher;
import com.scottlogic.matchingengine.dao.AccountJdbcDAO;
import com.scottlogic.matchingengine.entities.Account;
import com.scottlogic.matchingengine.entities.Order;
import com.scottlogic.matchingengine.entities.Trade;
import com.scottlogic.matchingengine.models.AuthenticationRequest;
import com.scottlogic.matchingengine.util.JwtUtil;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/matcher")
public class MatcherRestController {

    public Matcher matcher = new Matcher();
    public AuthenticationController authenticationController = new AuthenticationController(matcher);
    @Autowired
    JwtUtil jwtUtil = new JwtUtil();

    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Autowired
    AccountJdbcDAO accountJdbcDAO = new AccountJdbcDAO(jdbcTemplate);

    @GetMapping(value = "/aggregates")
    public HashMap<String, ArrayList<Order>> aggregates() {
        HashMap<String, ArrayList<Order>> response = new HashMap<>();
        response.put("aggBuyList", matcher.aggBuyList);
        response.put("aggSellList", matcher.aggSellList);
        return response;
    }

    @GetMapping(value = "/trades")
    public ArrayList<Trade> getTrades() {
        return matcher.tradeList;
    }

    @GetMapping(value = "/privateorderbook")
    public HashMap<String, ArrayList<Order>> getPrivateOrderBook(@RequestHeader("Authorization") String jwt){


        //Determine the account here
        String username = jwtUtil.extractUsername(jwt.substring(7));

        if (accountJdbcDAO.read(username).isPresent()) {

            Account account = accountJdbcDAO.read(username).get();
            ArrayList<Order> buyList = new ArrayList<>();
            ArrayList<Order> sellList = new ArrayList<>();

            //Maybes store orders in the accounts section

            for (Order entry : matcher.buyList) {
                if (entry.getAccount().getAccountId() == account.getAccountId()) {
                    buyList.add(entry);
                    System.out.println("Adding to buy list: " + entry);
                }
            }

            for (Order entry : matcher.sellList) {
                if (entry.getAccount().getAccountId()  == account.getAccountId()) {
                    buyList.add(entry);
                }
            }

            HashMap<String, ArrayList<Order>> response = new HashMap<>();
            response.put("privateBuyList", buyList);
            response.put("privateSellList", sellList);
            return response;
        } else {
            HashMap<String, ArrayList<Order>> response = new HashMap<>();
            response.put("privateBuyList", null);
            response.put("privateSellList", null);
            return  response;
        }
    }

    @RequestMapping(value = "/neworder", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<String, ArrayList<Order>> createOrder(@RequestHeader("Authorization") String jwt, @RequestBody Order order) {
        //Remove any pre-defined account
        order.setAccount(null);

        //Determine the account here
        String username = jwtUtil.extractUsername(jwt.substring(7));
        order.setAccount(accountJdbcDAO.read(username).get());

        matcher.processOrder(order);
        HashMap<String, ArrayList<Order>> response = new HashMap<>();
        response.put("aggBuyMap", matcher.aggBuyList);
        response.put("aggSellMap", matcher.aggSellList);
        return response;

    }

}