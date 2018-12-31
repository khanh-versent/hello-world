package com.khanh.sample.utils;

import com.khanh.sample.TestUtil;
import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeDetails;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class XmlUtilTest {
    String fileName = "Trades.xml";

    @Test
    public void testXmlUtilWriteToFile() throws IOException {

        File f = new File(fileName);
        if(f.exists()) {
            Assert.assertTrue(f.delete());
        }

        TradeDetails details = getTradeDetails();
        XmlUtil.writeToFile(fileName, details);

        f = new File(fileName);
        Assert.assertNotNull(f);
        Assert.assertTrue(f.exists());
    }

    @Test
    public void testXmlUtilReadFromFile() throws IOException {
        TradeDetails originalTradeDetails = getTradeDetails();
        XmlUtil.writeToFile(fileName, originalTradeDetails);

        List<Trade> originalTrades = originalTradeDetails.getTrades();

        TradeDetails savedTradeDetails = XmlUtil.readFromFile(fileName, TradeDetails.class);
        Assert.assertNotNull(savedTradeDetails);

        Assert.assertEquals(originalTrades.size(), savedTradeDetails.getTrades().size());
        TestUtil.assertEqualsTrades(originalTrades, savedTradeDetails.getTrades());
    }

    private TradeDetails getTradeDetails() {
        List<Trade> trades = TestUtil.generateTrades(10, 1);
        return new TradeDetails(trades);
    }
}