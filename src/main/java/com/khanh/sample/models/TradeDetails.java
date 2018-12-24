package com.khanh.sample.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class TradeDetails {
    @JacksonXmlElementWrapper(localName = "trades")
    @JacksonXmlProperty(localName = "trade")
    private List<Trade> trades;

    public TradeDetails() {}

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
