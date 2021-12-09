package com.scottlogic.matchingengine.interfaces;

import com.scottlogic.matchingengine.models.Account;
import com.scottlogic.matchingengine.models.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface MatcherInterface {

    /**
     *
     * @param order
     */
    public void processOrder(Order order);

    /**
     *
     * @param orderList
     * @return
     */
    public ArrayList<Order> aggregateMap(HashMap<Account, Order> orderList);

    /**
     *
     * @param
     * @return
     */
    public ArrayList<Order> cumulateList(ArrayList<Order> aggList);

    /**
     *
     * @param order
     * @param entry
     * @return
     */
    public Boolean evaluateOperator(Order order, Map.Entry<Account, Order> entry);

    /**
     *
     * @param orderOne
     * @param orderTwo
     */
    public void match(Order orderOne, Order orderTwo);


}
