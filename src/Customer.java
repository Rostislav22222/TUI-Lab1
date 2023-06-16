package com.mybank.tui;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String name;
    private String address;
    private List<Account> accounts;

    public Customer(String name, String address) {
        this.name = name;
        this.address = address;
        accounts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}
