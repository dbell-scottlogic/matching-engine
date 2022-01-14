package com.scottlogic.matchingengine.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;

public class Trade {
    @NotNull
    private int tradeId;

    @NotNull
    @Positive
    private int price;

    @NotNull
    private int buyOrderId;

    @NotNull
    private int sellOrderId;

    @NotNull
    private String buyAccountUsername;

    @NotNull
    private String sellAccountUsername;

    @NotNull
    private Timestamp timestamp;

    public Trade(int tradeId, int price,int buyOrderId, int sellOrderId, String buyAccountUsername, String sellAccountUsername, Timestamp timestamp) {
        this.tradeId = tradeId;
        this.price = price;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.buyAccountUsername = buyAccountUsername;
        this.sellAccountUsername = sellAccountUsername;
        this.timestamp = timestamp;
    }

    public Trade(){

    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }



    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getBuyOrderId() {
        return buyOrderId;
    }

    public void setBuyOrderId(int buyOrderId) {
        this.buyOrderId = buyOrderId;
    }

    public int getSellOrderId() {
        return sellOrderId;
    }

    public void setSellOrderId(int sellOrderId) {
        this.sellOrderId = sellOrderId;
    }

    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    public String getBuyAccountUsername() {
        return buyAccountUsername;
    }

    public void setBuyAccountUsername(String buyAccountUsername) {
        this.buyAccountUsername = buyAccountUsername;
    }

    public String getSellAccountUsername() {
        return sellAccountUsername;
    }

    public void setSellAccountUsername(String sellAccountUsername) {
        this.sellAccountUsername = sellAccountUsername;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "tradeId=" + tradeId +
                ", price=" + price +
                ", buyOrderId=" + buyOrderId +
                ", sellOrderId=" + sellOrderId +
                ", buyAccountUsername=" + buyAccountUsername +
                ", sellAccountUsername=" + sellAccountUsername +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        Trade trade = (Trade) obj;
        return trade.price == this.price
                && trade.tradeId == this.tradeId
                && trade.buyOrderId == this.buyOrderId
                && trade.sellOrderId == this.sellOrderId
                && trade.buyAccountUsername == this.buyAccountUsername
                && trade.sellAccountUsername == this.sellAccountUsername
                && trade.timestamp == this.timestamp;
    }
}

