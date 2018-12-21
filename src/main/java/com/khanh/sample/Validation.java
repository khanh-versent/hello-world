package com.khanh.sample;

import java.util.Date;

public class Validation {
    private String f365CsvPath;
    private String nuggetPath;
    private String forwardedNuggetPath;
    private String archivedNuggetPath;
    private String f46CsvPath;

    public Validation(String f365CsvPath, String nuggetPath, String forwardedNuggetPath,
                      String archivedNuggetPath, String f46CsvPath) {
        this.f365CsvPath = f365CsvPath;
        this.nuggetPath = nuggetPath;
        this.forwardedNuggetPath = forwardedNuggetPath;
        this.archivedNuggetPath = archivedNuggetPath;
        this.f46CsvPath = f46CsvPath;
    }

    public boolean verifyF365CSVFile() {
        return true;
    }

    public boolean verifyNuggetFile(String path) {
        return true;
    }

    public boolean verifyF46CSVFile(String path) {
        return true;
    }
}
