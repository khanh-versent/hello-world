package com.khanh.sample.utils;

import com.khanh.sample.models.Trade;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CSVUtilTest {
    private String fileName = "Trades.csv";
    private static final double DELTA = 1e-15;

    @Test
    public void testCSVUtilWriteToFile() throws IOException {
        File f = new File(fileName);
        if (f.exists()) {
            Assert.assertTrue(f.delete());
        }

        List<Trade> trades = GetTrades();
        CSVUtil.writeToFile(fileName, Trade.class, trades);

        f = new File(fileName);
        Assert.assertNotNull(f);
        Assert.assertTrue(f.exists());
    }

    @Test
    public void testCSVUtilReadFromFile() throws IOException {
        testCSVUtilWriteToFile();

        List<Trade> trades = CSVUtil.readFromFile(fileName, Trade.class);
        List<Trade> originalTrades = GetTrades();

        Assert.assertNotNull(trades);
        Assert.assertEquals(trades.size(), originalTrades.size());

        int matchCount = 0;
        for (Trade trade : trades) {
            for (Trade originalTrade : originalTrades) {
                if (trade.getTradeId() == originalTrade.getTradeId()) {
                    matchCount++;
                    Assert.assertEquals(trade.getPrice(), originalTrade.getPrice(), DELTA);
                    Assert.assertEquals(trade.getVolume(), originalTrade.getVolume(), DELTA);
                    Assert.assertEquals(trade.getDescription(), originalTrade.getDescription());
                }
            }
        }

        Assert.assertEquals(matchCount, trades.size());
    }

    private List<Trade> GetTrades() {
        List<Trade> trades = new ArrayList<Trade>();
        trades.add(new Trade(1, 100, 100, "Trade 1"));
        trades.add(new Trade(2, 100, 100, "Trade 2"));
        return trades;
    }
}