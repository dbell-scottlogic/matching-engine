package com.scottlogic.matchingengine;

import com.scottlogic.matchingengine.interfaces.MatcherInterface;
import com.scottlogic.matchingengine.models.Account;
import com.scottlogic.matchingengine.models.Action;
import com.scottlogic.matchingengine.models.Order;
import com.scottlogic.matchingengine.models.Trade;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Matcher implements MatcherInterface {

    public HashMap<Account, Order> buyList = new HashMap<>();
    public HashMap<Account, Order> sellList = new HashMap<>();
    public ArrayList<Trade> tradeList = new ArrayList<>();

    @Override
    public void processOrder(Order order) {
        HashMap<Account, Order> map = (order.getAction().equals(Action.BUY) ? sellList : buyList);
        HashMap<Account, Order> opposingMap = (order.getAction().equals(Action.BUY) ? buyList : sellList);


            for (Map.Entry<Account, Order> entry : map.entrySet()) {
                if (evaluateOperator(order, entry)) {
                    int finalSize = order.getSize() - entry.getValue().getSize();
                    if (finalSize < 0) {
                        //finalSize is less than zero, entry quan is greater than order quan
                        //For buy order, this means add to buy list
                        //For sell order, this means partial match, add ress of sell quan to sell list

                        if (order.getAction().equals(Action.BUY)) {
                            buyList.put(order.getAccount(), order);
                        } else {
                            partiallyMatch(order, entry.getValue());
                        }

                    } else if (finalSize > 0) {
                        //if final size is greater than zero, entry quan is less than order quan
                        //For buy order,  this means partial match
                        //For sell , this means add to sell list

                        if (order.getAction().equals(Action.SELL)) {
                            sellList.put(order.getAccount(), order);
                        } else {
                            partiallyMatch(order, entry.getValue());
                        }
                    } else {
                        addToTrades(order, entry.getValue());
                        //if final size is zero, entry quan is equal to order quan
                        //For buy, match
                        //For sell match
                    }

                } else {
                    addToList(order);
                }
            }

        if (map.isEmpty()){
            opposingMap.put(order.getAccount(), order);
        }
        }



    public void addToList(Order order) {
        if (order.getAction().equals(Action.BUY)) {
            buyList.put(order.getAccount(), order);
        } else {
            sellList.put(order.getAccount(), order);
        }
    }

    public void partiallyMatch(Order order, Order entry) {

        HashMap<Account, Order> map = (order.getAction().equals(Action.BUY) ? buyList : sellList);
        HashMap<Account, Order> opposingMap = (order.getAction().equals(Action.BUY) ? sellList : buyList);

        int finalSize = order.getSize() - entry.getSize();
        order.setSize(finalSize);
        map.remove(entry.getAccount(), entry);
        opposingMap.put(order.getAccount(), order);

    }

    public void addToTrades(Order order, Order entry) {
        if (order.getAction().equals(Action.BUY)) {

            Trade trade = new Trade(
                    order.getPrice(),
                    order,
                    entry,
                    order.getAccount(),
                    entry.getAccount(),
                    new Timestamp(new Date().getTime()));
            tradeList.add(trade);
            sellList.remove(entry.getAccount(), entry);

        } else {

            Trade trade = new Trade(
                    order.getPrice(),
                    entry,
                    order,
                    entry.getAccount(),
                    order.getAccount(),
                    new Timestamp(new Date().getTime()));
            tradeList.add(trade);
            buyList.remove(order.getAccount(), order);
        }

    }

    @Override
    public Boolean evaluateOperator(Order order, Map.Entry<Account, Order> entry) {
        if (order.getAction().equals(Action.BUY)) {
            if (order.getPrice() >= entry.getValue().getPrice()) {
                return true;
            } else {
                return false;
            }
        } else {
            if (order.getPrice() <= entry.getValue().getPrice()) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public HashMap<Account, Order> aggregateOrderList(HashMap<Account, Order> orderList) {
        return null;
    }

    @Override
    public HashMap<Account, Order> cumulateList(HashMap<Account, Order> orderList) {
        return null;
    }
}
