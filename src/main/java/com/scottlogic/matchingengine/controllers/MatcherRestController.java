package com.scottlogic.matchingengine.controllers;

import com.scottlogic.matchingengine.Matcher;
import com.scottlogic.matchingengine.entities.Account;
import com.scottlogic.matchingengine.entities.Order;
import com.scottlogic.matchingengine.entities.Trade;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/matcher")
public class MatcherRestController {

    public Matcher matcher = new Matcher();
    public AuthenticationController authenticationController = new AuthenticationController(matcher);

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
    public HashMap<String, ArrayList<Order>> getPrivateOrderBook(@RequestParam(value = "id") int id){
        Account account = authenticationController.accounts.get(id);
        System.out.println(account);
        ArrayList<Order> buyList = new ArrayList<>();
        ArrayList<Order> sellList = new ArrayList<>();

        //Maybes store orders in the accounts section

        for (Order entry: matcher.buyMap) {
            if (entry.getOrderId() == account.getAccountId()){
                buyList.add(entry);
                System.out.println("Adding to buy list: " + entry);
            }
        }

        for (Order entry: matcher.sellMap) {
            if (entry.getOrderId() == account.getAccountId()){
                buyList.add(entry);
            }
        }

        HashMap<String, ArrayList<Order>> response = new HashMap<>();
        response.put("privateBuyList", buyList);
        response.put("privateSellList", sellList);
        return response;
    }

    @PostMapping(value = "/neworder")
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<String, ArrayList<Order>> createOrder(@RequestBody Order order) {

        matcher.processOrder(order);
        HashMap<String, ArrayList<Order>> response = new HashMap<>();
        response.put("aggBuyMap", matcher.aggBuyList);
        response.put("aggSellMap", matcher.aggSellList);
        return response;

    }

}