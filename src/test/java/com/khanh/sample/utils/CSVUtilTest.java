package com.khanh.sample.utils;

import com.khanh.sample.TestUtil;
import com.khanh.sample.models.Trade;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class CSVUtilTest {
    private String fileName = "Trades.csv";

    @Test
    public void testCSVUtilWriteToFile() throws IOException {
        File f = new File(fileName);
        if (f.exists()) {
            Assert.assertTrue(f.delete());
        }

        List<Trade> trades = TestUtil.generateTrades(10, 1);
        CSVUtil.writeToFile(fileName, Trade.class, trades);

        f = new File(fileName);
        Assert.assertNotNull(f);
        Assert.assertTrue(f.exists());
    }

    @Test
    public void testCSVUtilReadFromFile() throws IOException {

        List<Trade> originalTrades = TestUtil.generateTrades(10, 1);
        CSVUtil.writeToFile(fileName, Trade.class, originalTrades);

        List<Trade> savedTrades = CSVUtil.readFromFile(fileName, Trade.class);


        Assert.assertNotNull(savedTrades);
        Assert.assertEquals(savedTrades.size(), originalTrades.size());

        TestUtil.assertEqualsTrades(savedTrades, originalTrades);
    }
}