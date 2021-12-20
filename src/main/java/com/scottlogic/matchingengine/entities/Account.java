package com.scottlogic.matchingengine.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

public class Account implements Serializable {

    @NotNull
    @Positive
    private int accountId;

    @NotNull
    private String username;

    @NotNull
    private String token;

    public Account(){}

    public Account(int accountId, String username) {
        this.accountId = accountId;
        this.username = username;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int id) {
        this.accountId = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + accountId +
                ", username='" + username + '\'' +
                '}';
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
