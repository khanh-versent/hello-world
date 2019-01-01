package com.khanh.sample.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class TradeMetadata {
    @JacksonXmlElementWrapper(localName = "trades")
    @JacksonXmlProperty(localName = "trade")
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
