package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.utils.CompressUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class DMPTest {
    String f365CsvPath = "sim";
    String nuggetPath = "brs";
    String forwardedNuggetPath = "bnp";
    String archivedNuggetPath = "archived";

    @Test
    public void checkNewF46CSVFile() throws IOException {
        BRS brs = new BRS(f365CsvPath, nuggetPath);

        List<Trade> trades = TestUtil.generateTrades(10, 1);
        String fileName = brs.createNuggetFile(trades);

        File nuggetFile = new File(nuggetPath + File.separator + fileName);
        Assert.assertTrue(nuggetFile.exists());

        CompressUtil.extractTarFile(nuggetFile, nuggetFile.getParentFile());

    }
}