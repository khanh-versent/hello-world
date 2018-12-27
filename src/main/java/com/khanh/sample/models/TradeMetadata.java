package com.khanh.sample.models;

import java.util.List;

public class TradeMetadata {
    private List<Trade> trades;

    public TradeMetadata() {

    }

    public TradeMetadata(List<Trade> trades) {
        this.trades = trades;
    }


    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }
}
