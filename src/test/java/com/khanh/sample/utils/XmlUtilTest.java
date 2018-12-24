package com.khanh.sample.utils;

import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeDetails;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class XmlUtilTest {
    String fileName = "Trades.xml";

    @Test
    public void testXmlUtilWriteToFile() throws IOException {

        File f = new File(fileName);
        if(f.exists()) {
            Assert.assertTrue(f.delete());
        }

        List<Trade> trades =  new ArrayList<Trade>();
        trades.add(new Trade(1, 100, 100, "Trade 1"));
        trades.add(new Trade(2, 100, 100, "Trade 2"));
        TradeDetails details = new TradeDetails(trades);
        XmlUtil.WriteToFile(fileName, details);

        f = new File(fileName);
        Assert.assertNotNull(f);
        Assert.assertTrue(f.exists());
    }

    @Test
    public void testXmlUtilReadFromFile() throws IOException {
        testXmlUtilWriteToFile();

        TradeDetails details = XmlUtil.ReadFromFile(fileName, TradeDetails.class);
        Assert.assertNotNull(details);

        List<Trade> trades = details.getTrades();
        List<Trade> originalTrades = getTradeDetails().getTrades();
        Assert.assertEquals(trades.size(), originalTrades.size());
    }

    private TradeDetails getTradeDetails() {
        List<Trade> trades =  new ArrayList<Trade>();
        trades.add(new Trade(1, 100, 100, "Trade 1"));
        trades.add(new Trade(2, 100, 100, "Trade 2"));
        return new TradeDetails(trades);
    }
}