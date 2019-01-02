package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeDetails;
import com.khanh.sample.models.TradeMetadata;
import com.khanh.sample.utils.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class BNPTest {
    private String f365CsvPath = "data" + File.separator + "FOLDER1";
    private String nuggetPath = "data" + File.separator + "FOLDER2";
    private String forwardedNuggetPath = "data" + File.separator + "FOLDER3";
    private String archivedNuggetPath = "data" + File.separator + "FOLDER4";
    private String f46CsvPath = "data" + File.separator + "FOLDER5";

    @Test
    public void testProcessNewNuggetFile() {
        FileUtil.deleteDirectory(nuggetPath);
        FileUtil.deleteDirectory(forwardedNuggetPath);
        FileUtil.deleteDirectory(archivedNuggetPath);

        BRS brs = new BRS(f365CsvPath, nuggetPath);
        DMP dmp = new DMP(nuggetPath, forwardedNuggetPath, archivedNuggetPath, f46CsvPath);
        BNP bnp = new BNP(forwardedNuggetPath, f46CsvPath);

        List<Trade> trades = TestUtil.generateTrades(10, 1);
        brs.createNuggetFile(trades);
        dmp.executeNugget();

        bnp.processNewNuggetFile();
        Map<String, Map.Entry<TradeDetails, TradeMetadata>> nuggetData = bnp.getNuggetData();
        Assert.assertEquals(1, nuggetData.size());

        Map.Entry<String, Map.Entry<TradeDetails, TradeMetadata>> entry = nuggetData.entrySet().iterator().next();
        TestUtil.assertEqualsTrades(trades, entry.getValue().getValue().getTrades());

    }
}