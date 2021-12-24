package com.scottlogic.matchingengine;

import com.google.gson.Gson;
import com.scottlogic.matchingengine.interfaces.MatcherInterface;
import com.scottlogic.matchingengine.entities.*;
import org.apache.commons.lang3.SerializationUtils;

import java.sql.Timestamp;
import java.util.*;

public class Matcher implements MatcherInterface {


    public ArrayList<Order> buyList = new ArrayList<>();
    public ArrayList<Order> sellList = new ArrayList<>();
    public ArrayList<Trade> tradeList = new ArrayList<>();

    public ArrayList<Order> aggBuyList = new ArrayList<>();
    public ArrayList<Order> aggSellList = new ArrayList<>();

    public ArrayList<CumulateOrder> cumulatedBuyList = new ArrayList<>();
    public ArrayList<CumulateOrder> cumulatedSellList = new ArrayList<>();


    @Override
    public void processOrder(Order order) {

        ArrayList<Order> opposingMap = (order.getAction().equals(Action.BUY) ? sellList : buyList);
        ArrayList<Order> map = (order.getAction().equals(Action.BUY) ? buyList : sellList);

        if (opposingMap.isEmpty()) {
            map.add(order);
        }

        for (int i = 0; i <= opposingMap.size() - 1; i++) {

            if (evaluateOperator(order, opposingMap.get(i))) {
                int size = opposingMap.get(i).getSize() - order.getSize();
                if (size > 0) {

                    if (order.getAction().equals(Action.BUY) && sellList.isEmpty()) {

                        addToList(order);
                    } else {
                        partiallyMatch(opposingMap.get(i), order);

                    }

                } else if (size < 0) {

                    if (order.getAction().equals(Action.SELL) && buyList.isEmpty()) {
                        addToList(order);

                    } else {
                        partiallyMatch(order, opposingMap.get(i));

                    }
                } else {
                    match(order, opposingMap.get(i));
                    break;


                }
            } else {
                addToList(order);

            }

        }


        //The buy and sell maps must have a deep copy made to avoid modifications
        //made when the aggregator runs. Using SerializationUtils

        aggBuyList = aggregateMap(SerializationUtils.clone(buyList));
        aggSellList = aggregateMap(SerializationUtils.clone(sellList));

        if (aggBuyList.size() > 0) {
            cumulatedBuyList = cumulateList(aggBuyList);
        }

        if (aggSellList.size() > 0) {
            cumulatedSellList = cumulateList(aggSellList);
        }

    }


    public void addToList(Order order) {
        if (order.getAction().equals(Action.BUY)) {
            buyList.add(order);
        } else {
            sellList.add(order);
        }
    }

    @Override
    public void partiallyMatch(Order entry, Order order) {

        ArrayList<Order> map = (order.getAction().equals(Action.BUY) ? buyList : sellList);
        ArrayList<Order> opposingMap = (order.getAction().equals(Action.BUY) ? sellList : buyList);

        Order buyOrder;
        Order sellOrder;

        if (order.getAction().equals(Action.BUY)) {
            buyOrder = order;
            sellOrder = entry;
        } else {
            sellOrder = order;
            buyOrder = entry;
        }

        Gson gson = new Gson();
        Order originalBuyOrder = gson.fromJson(gson.toJson(buyOrder), Order.class);
        Order originalSellOrder = gson.fromJson(gson.toJson(sellOrder), Order.class);

        int finalSize = Math.abs(order.getSize() - entry.getSize());


        //Remove consumed trade from its list
        map.remove(order);
        opposingMap.remove(entry);
        entry.setSize(finalSize);
        opposingMap.add(entry);

        //Add to trade list the consumed Trade and Order
        int finalTradeSize = (Math.min(order.getSize(), entry.getSize()));

        Trade trade = new Trade(1,finalTradeSize, originalBuyOrder, originalSellOrder, buyOrder.getAccount(), sellOrder.getAccount(), new Timestamp(new Date().getTime()));
        tradeList.add(trade);
    }

    @Override
    public void match(Order order, Order entry) {
        ArrayList<Order> entryMap = (entry.getAction().equals(Action.BUY) ? buyList : sellList);

        Trade trade = new Trade(1,
                order.getPrice(),
                entry,
                order,
                entry.getAccount(),
                order.getAccount(),
                new Timestamp(new Date().getTime()));
        tradeList.add(trade);

        entryMap.remove(entry);

    }

    @Override
    public Boolean evaluateOperator(Order order, Order entry) {
        if (order.getAction().equals(Action.BUY)) {
            return order.getPrice() >= entry.getPrice();
        } else {
            return order.getPrice() <= entry.getPrice();
        }
    }

    @Override
    public ArrayList<Order> aggregateMap(ArrayList<Order> map) {

        ArrayList<Order> orders = new ArrayList<>(map);

        for (int i = 0; i < orders.size(); i++) {

            orders.get(i).setOrderId(0);
            orders.get(i).setAccount(null);
            orders.get(i).setTimestamp(null);
            for (int j = i + 1; j <= orders.size() - 1; j++) {

                if (orders.get(i).getPrice() == orders.get(j).getPrice()) {
                    orders.get(i).setSize(orders.get(i).getSize() + orders.get(j).getSize());
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
