package com.scottlogic.matchingengine;

import com.scottlogic.matchingengine.controllers.MatcherRestController;
import com.scottlogic.matchingengine.entities.Account;
import com.scottlogic.matchingengine.entities.Action;
import com.scottlogic.matchingengine.entities.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class MatcherRestControllerTests {

    MatcherRestController matcherRestController = new MatcherRestController();

    @Test
    public void aggregatesAreEmpty(){
        HashMap<String, ArrayList<Order>> order = matcherRestController.aggregates();

        assertEquals("aggBuyList is empty", true, order.get("aggBuyList").isEmpty());
        assertEquals("aggSellList is empty", true, order.get("aggSellList").isEmpty());

    }

    @Test
    public void aggregatesContainBuyValues(){

        Order order1 = new Order(1, 10, 10, Action.BUY, new Account(1, "Test"), new Timestamp(new Date().getTime()));
        Order order2 = new Order(2, 10, 10, Action.BUY, new Account(1, "Test"), new Timestamp(new Date().getTime()));

        matcherRestController.matcher.processOrder(order1);
        matcherRestController.matcher.processOrder(order2);

        HashMap<String, ArrayList<Order>> order = matcherRestController.aggregates();
        assertEquals("aggBuyList is empty", false, order.get("aggBuyList").isEmpty());
        assertEquals("aggSellList is empty", true, order.get("aggSellList").isEmpty());

    }

    @Test
    public void aggregatesContainSellValues(){

        Order order1 = new Order(1, 10, 10, Action.SELL, new Account(1, "Test"), new Timestamp(new Date().getTime()));
        Order order2 = new Order(2, 10, 10, Action.SELL, new Account(1, "Test"), new Timestamp(new Date().getTime()));

        matcherRestController.matcher.processOrder(order1);
        matcherRestController.matcher.processOrder(order2);

        HashMap<String, ArrayList<Order>> order = matcherRestController.aggregates();
        assertEquals("aggBuyList is empty", true, order.get("aggBuyList").isEmpty());
        assertEquals("aggSellList is empty", false, order.get("aggSellList").isEmpty());

    }

    @Test
    public void tradesReturnsEmpty(){
        assertTrue("Trades is empty", matcherRestController.getTrades().isEmpty());
    }

    @Test
    public void tradesReturnsTrade(){

        Order order1 = new Order(1, 10, 10, Action.SELL, new Account(1, "Test"), new Timestamp(new Date().getTime()));
        Order order2 = new Order(2, 10, 10, Action.BUY, new Account(1, "Test"), new Timestamp(new Date().getTime()));
        matcherRestController.matcher.processOrder(order1);
        matcherRestController.matcher.processOrder(order2);

        assertEquals("Trades contains value", 1, matcherRestController.getTrades().size());
    }


    @Test
    public void privateOrderBookReturnsEmpty(){

        HashMap<String, ArrayList<Order>> privateOrderBook = matcherRestController.getPrivateOrderBook(1);
        assertTrue("private Buy List is empty", privateOrderBook.get("privateBuyList").isEmpty());
        assertTrue("private Buy List is empty", privateOrderBook.get("privateSellList").isEmpty());
    }

    @Test
    public void privateOrderBookReturnsValue(){
        Account account = new Account(1, "Test");
        Order order1 = new Order(1, 10, 10, Action.BUY, account, new Timestamp(new Date().getTime()));
        Order order2 = new Order(1, 10, 10, Action.BUY, account, new Timestamp(new Date().getTime()));
        matcherRestController.matcher.processOrder(order1);
        matcherRestController.matcher.processOrder(order2);

        HashMap<String, ArrayList<Order>> privateOrderBook = matcherRestController.getPrivateOrderBook(1);
        System.out.println(privateOrderBook.get("privateBuyList").size());
        assertEquals("private Buy List is of size 2", 2, privateOrderBook.get("privateBuyList").size());
        assertTrue("private Buy List is empty", privateOrderBook.get("privateSellList").isEmpty());    }
}
