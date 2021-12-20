package com.scottlogic.matchingengine.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CumulateOrder {

    //private int id; may add this in as an incrementer on DB side
    @NotNull
    @Positive
    private int size;

    @NotNull
    @Positive
    private int price;

    @NotNull
    private Action action;


    public CumulateOrder(int size, int price, Action action) {
        this.size = size;
        this.price = price;
        this.action = action;
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

    @Override
    public String toString() {
        return "CumulateOrder{" +
                "size=" + size +
                ", price=" + price +
                ", action=" + action +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        CumulateOrder order = (CumulateOrder) obj;
        if (order.getSize() == this.getSize()
                && order.price == this.price
                && order.action == this.action) {
            return true;
        } else {
            return false;
        }
    }

}
