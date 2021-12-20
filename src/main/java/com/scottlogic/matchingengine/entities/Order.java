package com.scottlogic.matchingengine.entities;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.sql.Timestamp;


public class Order implements Serializable {

    @NotNull
    @Positive
    private int orderId;

    @NotNull
    @Positive
    private int size;

    @NotNull
    @Positive
    private int price;

    @NotNull
    private Action action;

    @NotNull
    private Account account;

    @NotNull
    private Timestamp timestamp;

    public Order(){}

    public Order(int orderId, int size, int price, Action action, Account account, Timestamp timestamp) {
        this.orderId = orderId;
        this.size = size;
        this.price = price;
        this.action = action;
        this.account = account;
        this.timestamp = timestamp;
    }

    public Order(int size, int price, Action action) {
        this.size = size;
        this.price = price;
        this.action = action;

    }

    public Order(int size, int price, Action action , Account account, Timestamp timestamp ) {
        this.size = size;
        this.price = price;
        this.action = action;
        this.account = account;
        this.timestamp = timestamp;

    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestampNull(){
        this.timestamp = null;
    }

    public void setAccountNull(){
        this.account = null;
    }

    @Override
    public String toString() {
        return "Order{" +
                "size=" + size +
                ", price=" + price +
                ", action=" + action +
                ", account=" + account +
                ", timestamp=" + timestamp +
                '}';
    }

    

    @Override
    public boolean equals(Object obj) {
        Order order = (Order) obj;
        return order.getOrderId() == this.getOrderId()
                && order.getSize() == this.getSize()
                && order.price == this.price
                && order.action == this.action;
    }
}
