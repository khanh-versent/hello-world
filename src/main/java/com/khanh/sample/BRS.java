package com.khanh.sample;

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
