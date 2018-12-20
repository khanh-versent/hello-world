package com.khanh.sample.utils;

import com.khanh.sample.models.Customer;
import com.khanh.sample.models.Trade;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CSVUtilTest {
    private String fileName = "Trades.csv";

    @Test
    public void testCSVUtilWriteToFile() throws IOException {
        File f = new File(fileName);
        if(f.exists()) {
            f.delete();
        }

        List<Trade> trades =  new ArrayList();
        trades.add(new Trade(1, 100, 100, "Trade 1"));
        trades.add(new Trade(2, 100, 100, "Trade 2"));

        CSVUtil.WriteToFile(fileName, Trade.class, trades);

        f = new File(fileName);
        Assert.assertNotNull(f);
        Assert.assertEquals(true, f.exists());
    }
}