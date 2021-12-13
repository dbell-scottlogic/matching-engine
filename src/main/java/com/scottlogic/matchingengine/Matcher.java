package com.scottlogic.matchingengine;

import com.scottlogic.matchingengine.interfaces.MatcherInterface;
import com.scottlogic.matchingengine.entities.*;
import java.sql.Timestamp;
import java.util.*;

public class Matcher implements MatcherInterface {

    public HashMap<Account, Order> buyMap = new HashMap<>();
    public HashMap<Account, Order> sellMap = new HashMap<>();
    public ArrayList<Trade> tradeList = new ArrayList<>();

    public ArrayList<Order> aggBuyList = new ArrayList<>();
    public ArrayList<Order> aggSellList = new ArrayList<>();

    public ArrayList<CumulateOrder> cumulatedBuyList = new ArrayList<>();
    public ArrayList<CumulateOrder> cumulatedSellList = new ArrayList<>();

    @Override
    public void processOrder(Order order) {

        HashMap<Account, Order> opposingMap = (order.getAction().equals(Action.BUY) ? sellMap : buyMap);
        HashMap<Account, Order> map = (order.getAction().equals(Action.BUY) ? buyMap : sellMap);

        for (Map.Entry<Account, Order> entry : opposingMap.entrySet()) {

            if (evaluateOperator(order, entry)) {
                int size = entry.getValue().getSize() - order.getSize();
                if (size > 0) {

                    if (order.getAction().equals(Action.BUY) && sellMap.isEmpty()) {
                        addToList(order);
                    } else {
                        partiallyMatch(entry.getValue(), order);
                    }

                } else if (size < 0) {

                    if (order.getAction().equals(Action.SELL) && buyMap.isEmpty()) {
                        addToList(order);
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

        if (aggBuyList.size() > 0) {
            cumulatedBuyList = cumulateList(aggBuyList);
        }

        if (aggSellList.size() > 0) {
            cumulatedSellList = cumulateList(aggSellList);
        }

    }

    public void addToList(Order order) {
        if (order.getAction().equals(Action.BUY)) {
            buyMap.put(order.getAccount(), order);
        } else {
            sellMap.put(order.getAccount(), order);
        }
    }

    public void partiallyMatch(Order entry, Order order) {

        HashMap<Account, Order> map = (order.getAction().equals(Action.BUY) ? buyMap : sellMap);
        HashMap<Account, Order> opposingMap = (order.getAction().equals(Action.BUY) ? sellMap : buyMap);

        entry.setSize(Math.abs(order.getSize() - entry.getSize()));

        //Remove consumed trade from its list
        map.remove(order.getAccount(), order);
        opposingMap.put(entry.getAccount(), entry);

        //Add to trade list the consumed Trade and Order
        Order buyOrder = (order.getAction().equals(Action.BUY) ? order : entry);
        Order sellOrder = (order.getAction().equals(Action.SELL) ? order : entry);
        Trade trade = new Trade(order.getPrice(), buyOrder, sellOrder, buyOrder.getAccount(), sellOrder.getAccount(), new Timestamp(new Date().getTime()));
        tradeList.add(trade);
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

        HashMap<Account, Order> mapDeepCopy = (HashMap<Account, Order>) map.clone();

        Collection<Order> values = mapDeepCopy.values();
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
    public ArrayList<CumulateOrder> cumulateList(ArrayList<Order> aggList) {

        aggList.sort(Comparator.comparing(Order::getSize));
        ArrayList<CumulateOrder> cumulateOrderArrayList = new ArrayList<>();

        for (Order order : aggList) {
            CumulateOrder cumulateOrder = new CumulateOrder(order.getSize(), order.getPrice(), order.getAction());
            cumulateOrderArrayList.add(cumulateOrder);
        }

        for (int i = 0; i < cumulateOrderArrayList.size() - 1; i++) {
            cumulateOrderArrayList.get(i + 1).setSize(cumulateOrderArrayList.get(i).getSize() + cumulateOrderArrayList.get(i + 1).getSize());
        }
        return cumulateOrderArrayList;
    }
}
