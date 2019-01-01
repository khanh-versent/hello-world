package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeDetails;
import com.khanh.sample.models.TradeMetadata;
import com.khanh.sample.utils.CompressUtil;
import com.khanh.sample.utils.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DMPTest {
    String f365CsvPath = "f365csv";
    String nuggetPath = "brs";
    String forwardedNuggetPath = "bnp";
    String archivedNuggetPath = "archived";
    String f46CsvPath = "f46csv";

    @Test
    public void testProcessNewNuggetFile() throws IOException {
        FileUtil.deleteDirectory(nuggetPath);

        BRS brs = new BRS(f365CsvPath, nuggetPath);
        DMP dmp = new DMP(nuggetPath, forwardedNuggetPath, archivedNuggetPath, f46CsvPath);

        List<Trade> trades = TestUtil.generateTrades(10, 1);
        String fileName = brs.createNuggetFile(trades);

        File nuggetFile = new File(nuggetPath + File.separator + fileName);
        Assert.assertTrue(nuggetFile.exists());

        Assert.assertEquals(0, dmp.getNuggetData().size());
        dmp.processNewNuggetFile();
        Assert.assertEquals(1, dmp.getNuggetData().size());
    }

    @Test
    public void testForwardNuggets() {
        FileUtil.deleteDirectory(nuggetPath);
        FileUtil.deleteDirectory(forwardedNuggetPath);
        FileUtil.deleteDirectory(archivedNuggetPath);

        BRS brs = new BRS(f365CsvPath, nuggetPath);
        DMP dmp = new DMP(nuggetPath, forwardedNuggetPath, archivedNuggetPath, f46CsvPath);

        List<Trade> trades = TestUtil.generateTrades(10, 1);
        String fileName = brs.createNuggetFile(trades);

        Map<String, Map.Entry<TradeDetails, TradeMetadata>> data = new HashMap<>();
        data.put(new File(fileName).getName(), Map.entry(new TradeDetails(trades), new TradeMetadata(trades)));

        dmp.setNuggetData(data);
        dmp.forwardNuggets();

        File file = new File(forwardedNuggetPath);
        Assert.assertNotNull(file);
        Assert.assertNotNull(file.listFiles());
        Assert.assertEquals(1, file.listFiles().length);

        file = new File(archivedNuggetPath);
        Assert.assertNotNull(file);
        Assert.assertNotNull(file.listFiles());
        Assert.assertEquals(1, file.listFiles().length);
    }
}