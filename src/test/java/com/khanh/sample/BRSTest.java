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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BRSTest {
    long id = 1;
    Map<String, List<Trade>> createdTrades = new HashMap<>();

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
    public void testCreateNuggetFile()  {
        FileUtil.deleteDirectory("sim");
        FileUtil.deleteDirectory("brs");

        BRS brs = new BRS("sim", "brs");

        List<Trade> trades = getTrades(10);

        brs.createNuggetFile(trades);
        FileFilter filter = new RegexFileFilter(".*\\.tar\\.gz");
        File[] files = new File("brs").listFiles(filter);
        Assert.assertNotNull(files);
        Assert.assertEquals(1, files.length);

    }

    private void createCSVFiles(int amountOfSet) throws IOException {

        for(int i = 0; i < amountOfSet; i++) {
            List<Trade> trades = getTrades(10);

            String path = "sim" + File.separator + "trades" + trades.get(0).getTradeId() + ".csv";
            CSVUtil.writeToFile(path, Trade.class, trades);

            File file = new File(path);
            createdTrades.put(file.getName(), trades);
        }
    }

    private List<Trade> getTrades(int amount) {
        List<Trade> trades = TestUtil.generateTrades(amount, id);
        id += amount;
        return trades;
    }
}