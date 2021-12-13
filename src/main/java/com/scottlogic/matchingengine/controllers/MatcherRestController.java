package com.scottlogic.matchingengine.controllers;

import com.scottlogic.matchingengine.Matcher;
import com.scottlogic.matchingengine.entities.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/matcher")
public class MatcherRestController {

    Matcher matcher = new Matcher();

    @GetMapping(value = "/sellList")
    public ArrayList<Order> getSellList(){
        System.out.println("Getting sell list");
        return matcher.aggSellList;
    }

    @GetMapping(value = "/buyList")
    public ArrayList<Order> getBuyList(){
        System.out.println("Getting buy list");
        return matcher.aggBuyList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody Order order){ //Eventually return json response of all lists

    }

}