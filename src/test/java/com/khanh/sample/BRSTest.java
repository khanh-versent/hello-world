package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.utils.CSVUtil;
import com.khanh.sample.utils.FileUtil;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BRSTest {
    long id = 1;
    Map<String, List<Trade>> createdTrades = new HashMap<>();
    private static final double DELTA = 1e-15;

    @Test
    public void testExecute() {

    }

    @Test
    public void testProcessNewF365CSVFile() throws IOException {
        int amountOfSet = 5;
        FileUtil.deleteDirectory("sim");

        createdTrades.clear();
        createCSVFiles(amountOfSet);
        BRS brs = new BRS("sim", "brs");

        // since the files are created before the BRS, hence, the lastCheckCSV of BRS will be newer than the files
        // then, it will ignore those files
        brs.processNewF365CSVFile();
        Map<String, List<Trade>> processedTrades = brs.getTrades();
        Assert.assertEquals(0, processedTrades.size());

        createdTrades.clear();
        createCSVFiles(amountOfSet);
        brs.processNewF365CSVFile();
        processedTrades = brs.getTrades();
        Assert.assertEquals(amountOfSet, processedTrades.size());

        for (Map.Entry<String, List<Trade>> entry : createdTrades.entrySet()) {
            Assert.assertTrue(processedTrades.containsKey(entry.getKey()));
            TestUtil.assertEqualsTrades(processedTrades.get(entry.getKey()), entry.getValue());
        }
    }

    @Test
    public void testCreateNuggetFile() throws IOException {
        FileUtil.deleteDirectory("sim");
        FileUtil.deleteDirectory("brs");

        BRS brs = new BRS("sim", "brs");

        List<Trade> trades = getTrades();

        brs.createNuggetFile(trades);
        FileFilter filter = new RegexFileFilter(".*\\.tar\\.gz");
        File[] files = new File("brs").listFiles(filter);
        Assert.assertNotNull(files);
        Assert.assertEquals(1, files.length);

    }

    private Trade findById(List<Trade> trades, long id) {
        for(Trade trade : trades) {
            if(trade.getTradeId() == id) {
                return trade;
            }
        }
        return null;
    }

    private void createCSVFiles(int amountOfSet) throws IOException {

        for(int i = 0; i < amountOfSet; i++) {
            List<Trade> trades = getTrades();

            String path = "sim" + File.separator + "trades" + trades.get(1).getTradeId() + ".csv";
            CSVUtil.writeToFile(path, Trade.class, trades);

            File file = new File(path);
            createdTrades.put(file.getName(), trades);
        }
    }

    private List<Trade> getTrades() {
        List<Trade> trades = new ArrayList<>();
        trades.add(new Trade(id, getRandomDouble(), getRandomDouble(), "Trade " + id++));
        trades.add(new Trade(id, getRandomDouble(), getRandomDouble(), "Trade " + id++));
        trades.add(new Trade(id, getRandomDouble(), getRandomDouble(), "Trade " + id++));
        return trades;
    }

    private double getRandomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }
}