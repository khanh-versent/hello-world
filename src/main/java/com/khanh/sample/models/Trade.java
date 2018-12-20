package com.khanh.sample.models;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "trade")
public class Trade {
    private long tradeId;
    private double price;
    private double volume;
    private String description;

    public Trade(long tradeId, double price, double volume, String description) {
        this.tradeId = tradeId;
        this.price = price;
        this.volume = volume;
        this.description = description;
    }

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
