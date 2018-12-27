package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.utils.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SIMTest {
    String csvPath = "csv365";

    @Test
    public void execute() {
    }

    @Test
    public void createF365CSVFile() {

        FileUtil.deleteDirectory(csvPath);
        SIM sim = new SIM(csvPath);

        List<Trade> originalTrades = GetTrades();
        sim.createF365CSVFile(originalTrades);

        File[] files = new File(csvPath).listFiles();
        Assert.assertNotNull(files);
        Assert.assertEquals(1, files.length);
    }

    private List<Trade> GetTrades() {
        List<Trade> trades = new ArrayList<Trade>();
        trades.add(new Trade(1, 100, 100, "Trade 1"));
        trades.add(new Trade(2, 100, 100, "Trade 2"));
        return trades;
    }
}