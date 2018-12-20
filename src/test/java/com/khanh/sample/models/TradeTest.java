package com.khanh.sample.models;

import org.junit.Assert;
import org.junit.Test;

public class TradeTest {
    @Test
    public void testTradeConstructor() {

        long id = 1;
        Trade trade = new Trade(id, 100, 100, "trade " + id);

        Assert.assertEquals(id, trade.getTradeId());
        Assert.assertEquals(100, trade.getPrice(), 0.000001);
        Assert.assertEquals(100, trade.getVolume(), 0.000001);
        Assert.assertEquals("trade " + id, trade.getDescription());
    }
}
