package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeDetails;
import com.khanh.sample.models.TradeMetadata;
import com.khanh.sample.utils.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DMPTest {
    private String f365CsvPath = "data" + File.separator + "FOLDER1";
    private String nuggetPath = "data" + File.separator + "FOLDER2";
    private String forwardedNuggetPath = "data" + File.separator + "FOLDER3";
    private String archivedNuggetPath = "data" + File.separator + "FOLDER4";
    private String f46CsvPath = "data" + File.separator + "FOLDER5";

    @Test
    public void testProcessNewNuggetFile() {
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

        File forwardedFile = new File(forwardedNuggetPath);
        Assert.assertNotNull(forwardedFile);
        File[] files = forwardedFile.listFiles();
        Assert.assertNotNull(files);
        Assert.assertEquals(1, files.length);

        File archivedFile = new File(archivedNuggetPath);
        Assert.assertNotNull(archivedFile);
        files = archivedFile.listFiles();
        Assert.assertNotNull(files);
        Assert.assertEquals(1, files.length);

        Map<String, Map.Entry<TradeDetails, TradeMetadata>> emptyMap = dmp.getNuggetData();
        Assert.assertEquals(0, emptyMap.size());
    }

    @Test
    public void testExecuteNugget() throws IOException {
        FileUtil.deleteDirectory(nuggetPath);
        FileUtil.deleteDirectory(forwardedNuggetPath);
        FileUtil.deleteDirectory(archivedNuggetPath);

        BRS brs = new BRS(f365CsvPath, nuggetPath);
        DMP dmp = new DMP(nuggetPath, forwardedNuggetPath, archivedNuggetPath, f46CsvPath);

        List<Trade> trades = TestUtil.generateTrades(10, 1);
        String fileName = brs.createNuggetFile(trades);

        File nuggetFile = new File(nuggetPath + File.separator + fileName);
        Assert.assertTrue(nuggetFile.exists());

        Assert.assertEquals(0, dmp.getNuggetData().size());
        dmp.executeNugget();

        File forwardedFile = new File(forwardedNuggetPath);
        Assert.assertNotNull(forwardedFile);
        File[] files = forwardedFile.listFiles();
        Assert.assertNotNull(files);
        Assert.assertEquals(1, files.length);

        TestUtil.assertCheckNuggetFile(trades, files[0]);

        File archivedFile = new File(archivedNuggetPath);
        Assert.assertNotNull(archivedFile);
        files = archivedFile.listFiles();
        Assert.assertNotNull(files);
        Assert.assertEquals(1, files.length);

        TestUtil.assertCheckNuggetFile(trades, files[0]);
    }

    @Test
    public void testExecuteF46CSV() {

    }
}