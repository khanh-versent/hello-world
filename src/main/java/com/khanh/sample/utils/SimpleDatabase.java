package com.khanh.sample.utils;

import com.khanh.sample.models.Trade;

import java.util.List;

public class SimpleDatabase {
    private List<Trade> trades;

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }
}
