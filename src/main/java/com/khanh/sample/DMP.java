package com.khanh.sample;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class DMP {
    private String nuggetPath;
    private String forwardedNuggetPath;
    private String archivedNuggetPath;
    private String csvPath;

    List<String> newNuggets;
    List<String> newCSVs;

    Date lastNuggetCheck;
    Date lastCSVCheck;

    public DMP(String nuggetPath, String forwardedNuggetPath,
               String archivedNuggetPath, String csvPath) {
        this.nuggetPath = nuggetPath;
        this.forwardedNuggetPath = forwardedNuggetPath;
        this.archivedNuggetPath = archivedNuggetPath;
        this.csvPath = csvPath;
    }

    public void executeNugget() {
        checkNewNuggetFile();

        try {
            for (String nugget : newNuggets) {
                // TODO process those files

                if (!forwardNuggetFile(nugget)) {
                    throw new Exception(nugget);
                }
                if(!archivedNuggetFile(nugget)) {
                    throw new Exception(nugget);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void executeF46CSV() {
        checkNewF46CSVFile();

        for(String csv : newCSVs) {
            // TODO process those files

            archivedF46CSVFile(csv);
        }
    }

    public void checkNewNuggetFile() {


        lastNuggetCheck = new Date();
    }

    private boolean forwardNuggetFile(String filePath) {
        return true;
    }

    private boolean archivedNuggetFile(String filePath) {
        return true;
    }


    public void checkNewF46CSVFile() {

        lastCSVCheck = new Date();
    }

    public boolean archivedF46CSVFile(String filePath) {
        return true;
    }
}
