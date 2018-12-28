package com.khanh.sample;

import com.khanh.sample.models.Trade;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestUtil {
    private static final double DELTA = 1e-15;

    public static void assertEqualsTrade(Trade expected, Trade actual) {
        Assert.assertEquals(expected.getVolume(), actual.getVolume(), DELTA);
        Assert.assertEquals(expected.getPrice(), actual.getPrice(), DELTA);
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
    }

    public static void assertEqualsTrades(List<Trade> expected, List<Trade>  actual) {
        for(Trade expectedTrade : expected) {
            Trade actualTrade = findById(actual, expectedTrade.getTradeId());
            assertEqualsTrade(expectedTrade, actualTrade);
        }
    }

    private static Trade findById(List<Trade> trades, long id) {
        for(Trade trade : trades) {
            if(trade.getTradeId() == id) {
                return trade;
            }
        }
        return null;
    }

    public static double getRandomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static List<Trade> generateTrades(int amount, long startId) {
        List<Trade> trades = new ArrayList<Trade>();
        for(int i = 0; i < amount; i++) {
            long id = i + startId;
            trades.add(new Trade(id, TestUtil.getRandomDouble(), TestUtil.getRandomDouble(), "Trade " + id));
        }
        return trades;
    }
}
