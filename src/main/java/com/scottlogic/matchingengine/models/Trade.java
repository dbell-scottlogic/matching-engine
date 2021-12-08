package com.scottlogic.matchingengine.models;

import java.sql.Timestamp;

public class Trade {

    private int price;
    private Order buyOrder;
    private Order sellOrder;
    private Account buyAccount;
    private Account sellAccount;
    private Timestamp timestamp;

    public Trade(int price, Order buyOrder, Order sellOrder, Account buyAccount, Account sellAccount, Timestamp timestamp) {
        this.price = price;
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.buyAccount = buyAccount;
        this.sellAccount = sellAccount;
        this.timestamp = timestamp;
    }



    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Account getBuyAccount() {
        return buyAccount;
    }

    public void setBuyAccount(Account buyAccount) {
        this.buyAccount = buyAccount;
    }

    public Account getSellAccount() {
        return sellAccount;
    }

    public void setSellAccount(Account sellAccount) {
        this.sellAccount = sellAccount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Order getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(Order buyOrder) {
        this.buyOrder = buyOrder;
    }

    public Order getSellOrder() {
        return sellOrder;
    }

    public void setSellOrder(Order sellOrder) {
        this.sellOrder = sellOrder;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "price=" + price +
                ", buyOrder=" + buyOrder +
                ", sellOrder=" + sellOrder +
                ", buyAccount=" + buyAccount +
                ", sellAccount=" + sellAccount +
                ", timestamp=" + timestamp +
                '}';
    }
}
