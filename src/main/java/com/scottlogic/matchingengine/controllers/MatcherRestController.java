package com.scottlogic.matchingengine.controllers;

import com.scottlogic.matchingengine.matcher.Matcher;
import com.scottlogic.matchingengine.dao.AccountJdbcDAO;
import com.scottlogic.matchingengine.dao.OrderJdbcDAO;
import com.scottlogic.matchingengine.dao.TradeJdbcDAO;
import com.scottlogic.matchingengine.entities.*;
import com.scottlogic.matchingengine.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    OrderJdbcDAO orderJdbcDAO = new OrderJdbcDAO(jdbcTemplate);

    @Autowired
    TradeJdbcDAO tradeJdbcDAO = new TradeJdbcDAO(jdbcTemplate);

    @Autowired
    AccountJdbcDAO accountJdbcDAO = new AccountJdbcDAO(jdbcTemplate);

    @GetMapping(value = "/aggregates")
    public HashMap<String, ArrayList<Order>> aggregates() {
        HashMap<String, ArrayList<Order>> response = new HashMap<>();
        //List all buy list and sell list, pass to aggregator
        List<Order> topBuyOrders = orderJdbcDAO.getTopBuy();
        List<Order> topSellOrders = orderJdbcDAO.getTopSell();

        response.put("aggBuyList", matcher.aggregateMap((ArrayList<Order>) topBuyOrders));
        response.put("aggSellList", matcher.aggregateMap((ArrayList<Order>) topSellOrders));
        return response;
    }

    @GetMapping(value = "/marketdepth")
    public HashMap<String, ArrayList<CumulateOrder>> marketDepth() {
        HashMap<String, ArrayList<CumulateOrder>> response = new HashMap<>();
        response.put("cumulatedBuyList", matcher.cumulatedBuyList);
        response.put("cumulatedSellList", matcher.cumulatedSellList);
        return response;
    }

    @GetMapping(value = "/trades")
    public List<Trade> getTrades() {
        return tradeJdbcDAO.list();
    }

    @GetMapping(value = "/privateorderbook")
    public HashMap<String, List<Order>> getPrivateOrderBook(@RequestHeader("Authorization") String jwt) {

        //Determine the account here
        String username = jwtUtil.extractUsername(jwt.substring(7));
        List<Order> orders = orderJdbcDAO.listByUsername(username);
        HashMap<String, List<Order>> response = new HashMap<>();
        response.put("privateList", orders);
        return response;

    }

    @RequestMapping(value = "/neworder", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<String, ArrayList<Order>> createOrder(@RequestHeader("Authorization") String jwt, @RequestBody Order order) {
//        Remove any pre-defined account
//        order.setAccount(null);

        //Determine the account here
        //String username = jwtUtil.extractUsername(jwt.substring(7));
        //order.setAccount(accountJdbcDAO.read(username).get());
        order.setTimestamp(new Timestamp(new Date().getTime()));
        orderJdbcDAO.create(order);
        matcher.processOrder(order);
        HashMap<String, ArrayList<Order>> response = new HashMap<>();
        response.put("aggBuyMap", matcher.aggBuyList);
        response.put("aggSellMap", matcher.aggSellList);
        return response;

    }

}