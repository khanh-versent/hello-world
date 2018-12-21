package com.khanh.sample;

import java.util.ArrayList;
import java.util.List;

public class DMP {
    private String nuggetPath;
    private String forwardedNuggetPath;
    private String archivedNuggetPath;
    private String csvPath;

    List<String> newNuggets;
    List<String> newCSVs;

    public DMP(String nuggetPath, String forwardedNuggetPath, String archivedNuggetPath, String csvPath) {
        this.nuggetPath = nuggetPath;
        this.forwardedNuggetPath = forwardedNuggetPath;
        this.archivedNuggetPath = archivedNuggetPath;
        this.csvPath = csvPath;
    }

    public void ExecuteNugget() {
        checkNewNuggetFile();

        // TODO process those files

        forwardNuggetFile(newNuggets);
        archivedNuggetFile(newNuggets);
    }

    public void ExecuteF46CSV() {
        checkNewF46CSVFile();
        archivedF46CSVFile(newCSVs);
    }

    public void checkNewNuggetFile() {

    }

    private boolean forwardNuggetFile(List<String> filePaths) {
        return true;
    }

    private boolean archivedNuggetFile(List<String> filePaths) {
        return true;
    }



    public void checkNewF46CSVFile() {
        newCSVs = new ArrayList<String>();
    }

    public boolean archivedF46CSVFile(List<String> filePaths) {
        return true;
    }
}
