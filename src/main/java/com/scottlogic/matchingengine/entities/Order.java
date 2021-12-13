package com.scottlogic.matchingengine.entities;

import java.sql.Timestamp;

public class Order {

    private int id;
    private int size;
    private int price;
    private Action action;
    private Account account;
    private Timestamp timestamp;

    public Order(int id, int size, int price, Action action, Account account, Timestamp timestamp) {
        this.id = id;
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return order.getId() == this.getId()
                && order.getSize() == this.getSize()
                && order.price == this.price
                && order.action == this.action;
    }
}
