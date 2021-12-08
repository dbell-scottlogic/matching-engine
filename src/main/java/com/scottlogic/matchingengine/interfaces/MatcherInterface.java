package com.scottlogic.matchingengine.interfaces;

import com.scottlogic.matchingengine.models.Account;
import com.scottlogic.matchingengine.models.Order;
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
    public HashMap<Account, Order> aggregateOrderList(HashMap<Account, Order> orderList);

    /**
     *
     * @param orderList
     * @return
     */
    public HashMap<Account, Order> cumulateList(HashMap<Account, Order> orderList);

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
    public void addToTrades(Order orderOne, Order orderTwo);

}
