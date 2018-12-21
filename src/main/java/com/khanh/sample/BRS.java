package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeDetails;
import com.khanh.sample.models.TradeMetadata;

import java.util.ArrayList;
import java.util.List;

public class BRS {
    private String csvPath;
    private String nuggetPath;

    public BRS(String csvPath, String nuggetPath) {
        this.csvPath = csvPath;
        this.nuggetPath = nuggetPath;
    }

    public void Execute() {
        checkNewF365CSVFile();
        createNuggetFile();
    }

    public void checkNewF365CSVFile() {

    }

    public void createNuggetFile() {

    }
}
