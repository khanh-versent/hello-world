package com.khanh.sample.models;

import java.util.Date;

public class TradeInfo {
    private Trade trade;
    private TradeStatus tradeStatus;
    private Date lastUpdate;

    public TradeInfo(Trade trade) {
        this.trade = trade;
        this.lastUpdate = new Date();
        this.tradeStatus = TradeStatus.F365Created;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public void updateTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
        this.lastUpdate = new Date();
    }

    public Date getLastUpdate() {
        return this.lastUpdate;
    }
}

