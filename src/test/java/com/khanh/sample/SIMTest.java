package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.utils.CSVUtil;
import com.khanh.sample.utils.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SIMTest {
    String csvPath = "csv365";

    @Test
    public void testExecute() throws IOException {

        Assert.assertTrue(FileUtil.deleteDirectory(csvPath));
        SIM sim = new SIM(csvPath);

        List<Trade> originalTrades = TestUtil.generateTrades(10, 1);
        sim.setTrades(originalTrades);
        sim.execute();

        File[] files = new File(csvPath).listFiles();
        Assert.assertNotNull(files);
        Assert.assertEquals(1, files.length);

        List<Trade> savedTrades = CSVUtil.readFromFile(files[0], Trade.class);
        TestUtil.assertEqualsTrades(originalTrades, savedTrades);
    }

    @Test
    public void testCreateF365CSVFile() throws IOException {

        Assert.assertTrue(FileUtil.deleteDirectory(csvPath));
        SIM sim = new SIM(csvPath);

        List<Trade> originalTrades = generateTrades(10, 1);
        sim.createF365CSVFile(originalTrades);

        File[] files = new File(csvPath).listFiles();
        Assert.assertNotNull(files);
        Assert.assertEquals(1, files.length);

        List<Trade> savedTrades = CSVUtil.readFromFile(files[0], Trade.class);
        TestUtil.assertEqualsTrades(originalTrades, savedTrades);
    }

    private List<Trade> generateTrades(int amount, long startId) {
        List<Trade> trades = new ArrayList<Trade>();
        for(int i = 0; i < amount; i++) {
            long id = i + startId;
            trades.add(new Trade(id, TestUtil.getRandomDouble(), TestUtil.getRandomDouble(), "Trade " + id));
        }
        return trades;
    }
}