package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.utils.CSVUtil;
import com.khanh.sample.utils.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BRSTest {
    long id = 1;
    Map<String, List<Trade>> createdTrades;
    private static final double DELTA = 1e-15;

    @Test
    public void testExecute() {

    }

    @Test
    public void testProcessNewF365CSVFile() throws IOException {
        int amountOfSet = 5;
        FileUtil.deleteDirectory("sim");

        createCSVFiles(amountOfSet);
        BRS brs = new BRS("sim", "brs");

        // since the files are created before the BRS, hence, the lastCheckCSV of BRS will be newer than the files
        // then, it will ignore those files
        brs.processNewF365CSVFile();
        Map<String, List<Trade>> processedTrades = brs.getTrades();
        Assert.assertEquals(0, processedTrades.size());

        createCSVFiles(amountOfSet);
        brs.processNewF365CSVFile();
        processedTrades = brs.getTrades();
        Assert.assertEquals(amountOfSet, processedTrades.size());

        for (Map.Entry<String, List<Trade>> entry : createdTrades.entrySet()) {
            Assert.assertTrue(processedTrades.containsKey(entry.getKey()));

            List<Trade> trades = processedTrades.get(entry.getKey());
            for(Trade trade : trades) {
                Trade foundTrade = findById(entry.getValue(), trade.getTradeId());
                Assert.assertNotNull(foundTrade);
                Assert.assertEquals(foundTrade.getVolume(), trade.getVolume(), DELTA);
                Assert.assertEquals(foundTrade.getPrice(), trade.getPrice(), DELTA);
                Assert.assertEquals(foundTrade.getDescription(), trade.getDescription());
            }
        }
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
        createdTrades = new HashMap<>();
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