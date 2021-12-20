package com.scottlogic.matchingengine.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;

public class Trade {

    @NotNull
    @Positive
    private int price;

    @NotNull
    private Order buyOrder;

    @NotNull
    private Order sellOrder;

    @NotNull
    private Account buyAccount;

    @NotNull
    private Account sellAccount;

    @NotNull
    private Timestamp timestamp;

    public Trade(int price,Order buyOrder, Order sellOrder, Account buyAccount, Account sellAccount, Timestamp timestamp) {
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
                "price=" + price + '\n' +
                ", buyOrder=" + buyOrder + '\n' +
                ", sellOrder=" + sellOrder + '\n' +
                ", buyAccount=" + buyAccount +'\n' +
                ", sellAccount=" + sellAccount +'\n' +
                ", timestamp=" + timestamp +'\n' +
                '}';

    }



    @Override
    public boolean equals(Object obj) {
        Trade trade = (Trade) obj;
        return trade.price == this.price
                && trade.buyOrder == this.buyOrder
                && trade.sellOrder == this.sellOrder
                && trade.buyAccount == this.buyAccount
                && trade.sellAccount == this.sellAccount
                && trade.timestamp == this.timestamp;
    }
}

