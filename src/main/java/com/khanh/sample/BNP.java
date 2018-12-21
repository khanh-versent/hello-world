package com.khanh.sample;

public class BNP {
    private String csvPath;
    private String nuggetPath;

    public BNP(String nuggetPath, String csvPath) {
        this.csvPath = csvPath;
        this.nuggetPath = nuggetPath;
    }

    public void Execute() {
        checkNewNuggetFile();
        createF46CSVFile();
    }

    public void checkNewNuggetFile() {


    }

    private void createF46CSVFile() {

    }
}
