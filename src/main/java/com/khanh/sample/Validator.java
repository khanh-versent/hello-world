package com.khanh.sample;

import java.util.Date;

public class Validator {
    private String f365CsvPath;
    private String nuggetPath;
    private String forwardedNuggetPath;
    private String archivedNuggetPath;
    private String f46CsvPath;

    private Date lastCheckF365Csv;
    private Date lastCheckNugget;
    private Date lastCheckForwardedNugget;
    private Date lastCheckArchivedNugget;
    private Date lastCheckF46Csv;
    private Date lastCheckArchivedF46Csv;

    public Validator(String f365CsvPath, String nuggetPath, String forwardedNuggetPath,
                     String archivedNuggetPath, String f46CsvPath) {
        this.f365CsvPath = f365CsvPath;
        this.nuggetPath = nuggetPath;
        this.forwardedNuggetPath = forwardedNuggetPath;
        this.archivedNuggetPath = archivedNuggetPath;
        this.f46CsvPath = f46CsvPath;

        lastCheckF365Csv = new Date();
    }

    public boolean verifyF365CSVFile() {
        return true;
    }

    public boolean verifyNuggetFile() {
        return true;
    }

    public boolean verifyForwardedNuggetFile() {
        return true;
    }

    public boolean verifyArchivedNuggetFile() {
        return true;
    }

    public boolean verifyF46CSVFile() {
        return true;
    }

    public boolean verifyArchivedF46CSVFile() {
        return true;
    }
}
