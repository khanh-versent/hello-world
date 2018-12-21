package com.khanh.sample;

import com.khanh.sample.models.Trade;

import java.util.List;

public class SIM {
    private String csvPath;

    public SIM(String csvPath) {
        this.csvPath = csvPath;
    }

    public void Execute() {
        createF365CSVFile();
    }

    public void createF365CSVFile() {

    }
}
