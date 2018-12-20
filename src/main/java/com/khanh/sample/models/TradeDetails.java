package com.khanh.sample.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TradeDetails {
    @JsonProperty(value = "trades")
    private List<Trade> trades;

    public TradeDetails(List<Trade> trades) {
        this.trades = trades;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }
}
