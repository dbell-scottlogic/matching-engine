package com.scottlogic.matchingengine.interfaces;

import com.scottlogic.matchingengine.models.Account;
import com.scottlogic.matchingengine.models.CumulateOrder;
import com.scottlogic.matchingengine.models.Order;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface MatcherInterface {

    /**
     * Assesses the order, determines appropriate actions and
     * calls relevant methods, depending on whether an opposing trade
     * is available
     * @param order the order received from the REST API
     */
    void processOrder(Order order);

    /**
     * Takes map, aggregates it, and returns as an array list
     * @param orderList the map being passed in
     * @return ArrayList of type Order
     */
    ArrayList<Order> aggregateMap(HashMap<Account, Order> orderList);

    /**
     * First orders the list by price ascending, then cumulates each order and
     * returns an array list of type CumulateOrder
     * @param aggList of type order
     * @return arraylist of type CumulateOrder
     */
    ArrayList<CumulateOrder> cumulateList(ArrayList<Order> aggList);

    /**
     * Evaluates whether the price is higher or lower, and returns boolean to processOrder,
     * directing the order to its appropriate destination
     * @param order order received
     * @param entry the entry within the map the order is being compared too
     * @return boolean
     */
    Boolean evaluateOperator(Order order, Map.Entry<Account, Order> entry);

    /**
     * Creates and trade from order and entry, adds it to list. Then removes the consumed entry from its map.
     * @param order Order received
     * @param entry Entry in map
     */
    void match(Order order, Order entry);


}
