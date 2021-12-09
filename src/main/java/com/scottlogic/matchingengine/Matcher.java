package com.scottlogic.matchingengine;

import com.scottlogic.matchingengine.interfaces.MatcherInterface;
import com.scottlogic.matchingengine.models.Account;
import com.scottlogic.matchingengine.models.Action;
import com.scottlogic.matchingengine.models.Order;
import com.scottlogic.matchingengine.models.Trade;

import java.sql.Timestamp;
import java.util.*;

//finalSize is less than zero, entry quan is greater than order quan
//For buy order, this means add to buy list
//For sell order, this means partial match, add ress of sell quan to sell list

//if final size is greater than zero, entry quan is less than order quan
//For buy order,  this means partial match
//For sell , this means add to sell list

//if final size is zero, entry quan is equal to order quan
//For buy, match
//For sell match
public class Matcher implements MatcherInterface {

    public HashMap<Account, Order> buyMap = new HashMap<>();
    public HashMap<Account, Order> sellMap = new HashMap<>();
    public ArrayList<Trade> tradeList = new ArrayList<>();

    public ArrayList<Order> aggBuyList = new ArrayList<>();
    public ArrayList<Order> aggSellList = new ArrayList<>();

    public ArrayList<Order> cumulatedBuyList = new ArrayList<>();
    public ArrayList<Order> cumulatedSellList = new ArrayList<>();

    @Override
    public void processOrder(Order order) {
        HashMap<Account, Order> opposingMap = (order.getAction().equals(Action.BUY) ? sellMap : buyMap);
        HashMap<Account, Order> map = (order.getAction().equals(Action.BUY) ? buyMap : sellMap);

        for (Map.Entry<Account, Order> entry : opposingMap.entrySet()) {
            if (evaluateOperator(order, entry)) {
                int finalSize = order.getSize() - entry.getValue().getSize();
                if (finalSize < 0) {

                    if (order.getAction().equals(Action.BUY)) {
                        buyMap.put(order.getAccount(), order);
                    } else {
                        partiallyMatch(order, entry.getValue());
                    }

                } else if (finalSize > 0) {

                    if (order.getAction().equals(Action.SELL)) {
                        sellMap.put(order.getAccount(), order);
                    } else {
                        partiallyMatch(order, entry.getValue());
                    }
                } else {
                    match(order, entry.getValue());

                }

            } else {
                addToList(order);
            }
        }

        if (opposingMap.isEmpty()) {
            map.put(order.getAccount(), order);
        }

        aggBuyList = aggregateMap(buyMap);
        aggSellList = aggregateMap(sellMap);

        //Need to fix this so that either of them dont run if list is of length 0
//        cumulatedBuyList = cumulateList(aggBuyList);
//        cumulatedSellList = cumulateList(aggSellList);

        //cumulate lists
    }


    public void addToList(Order order) {
        if (order.getAction().equals(Action.BUY)) {
            buyMap.put(order.getAccount(), order);
        } else {
            sellMap.put(order.getAccount(), order);
        }
    }

    public void partiallyMatch(Order order, Order entry) {

        HashMap<Account, Order> map = (order.getAction().equals(Action.BUY) ? buyMap : sellMap);
        HashMap<Account, Order> opposingMap = (order.getAction().equals(Action.BUY) ? sellMap : buyMap);

        int finalSize = order.getSize() - entry.getSize();
        order.setSize(finalSize);
        map.remove(entry.getAccount(), entry);
        opposingMap.put(order.getAccount(), order);
    }

    public void match(Order order, Order entry) {
        HashMap<Account, Order> map = (order.getAction().equals(Action.BUY) ? sellMap : buyMap);

        Trade trade = new Trade(
                order.getPrice(),
                entry,
                order,
                entry.getAccount(),
                order.getAccount(),
                new Timestamp(new Date().getTime()));
        tradeList.add(trade);
        map.remove(order.getAccount(), order);

    }

    @Override
    public Boolean evaluateOperator(Order order, Map.Entry<Account, Order> entry) {
        if (order.getAction().equals(Action.BUY)) {
            return order.getPrice() >= entry.getValue().getPrice();
        } else {
            return order.getPrice() <= entry.getValue().getPrice();
        }
    }

    @Override
    public ArrayList<Order> aggregateMap(HashMap<Account, Order> map) {

        Collection<Order> values = map.values();
        ArrayList<Order> orders = new ArrayList<>(values);


        for (int i = 0; i <= orders.size(); i++) {
            for (int j = i + 1; j <= orders.size() - 1; j++) {

                if (orders.get(i).getPrice() == orders.get(j).getPrice()) {
                    orders.get(i).setSize(orders.get(i).getSize() + orders.get(j).getSize());
                    orders.get(i).setAccount(null);
                    orders.get(i).setTimestamp(null);
                    orders.remove(j);
                    j -= 1;
                }
            }
        }


        return orders;
    }

    @Override
    public ArrayList<Order> cumulateList(ArrayList<Order> aggList) {

        aggList.sort(Comparator.comparing(Order::getSize));

        for (int i = 0; i < aggList.size() - 1; i++) {
            aggList.get(i + 1).setSize(aggList.get(i).getSize() + aggList.get(i + 1).getSize());
            aggList.get(i).setAccount(null);
            aggList.get(i).setTimestamp(null);
        }

        aggList.get(aggList.size() - 1).setAccount(null);
        aggList.get(aggList.size() - 1).setTimestamp(null);

        return aggList;
    }
}
