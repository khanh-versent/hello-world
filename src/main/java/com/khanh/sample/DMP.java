package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeDetails;
import com.khanh.sample.models.TradeMetadata;
import com.khanh.sample.utils.CompressUtil;
import com.khanh.sample.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DMP {
    private String nuggetPath;
    private String forwardedNuggetPath;
    private String archivedNuggetPath;
    private String csvPath;

    Map<String, Map.Entry<TradeDetails, TradeMetadata>> newNuggets;
    Map<String, List<Trade>> newCSVs;

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
        processNewNuggetFile();

        try {
            for (String nugget : newNuggets.keySet()) {
                // TODO process those files

                if (!forwardNuggetFile(nugget, this.forwardedNuggetPath)) {
                    throw new Exception(nugget);
                }
                if (!archivedNuggetFile(nugget, this.archivedNuggetPath)) {
                    throw new Exception(nugget);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void executeF46CSV() {
        checkNewF46CSVFile();

        for (String csv : newCSVs.keySet()) {
            // TODO process those files

            archivedF46CSVFile(csv);
        }
    }

    public void processNewNuggetFile() {
        List<File> files = getNewFilesList(this.nuggetPath, this.lastNuggetCheck);
        lastNuggetCheck = new Date();

        for (File file : files) {
            try {
                CompressUtil.extractTarFile(file, file.getParentFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean forwardNuggetFile(String source, String destination) {
        return true;
    }

    private boolean archivedNuggetFile(String source, String destination) {
        return true;
    }


    public void checkNewF46CSVFile() {

        lastCSVCheck = new Date();
    }

    public boolean archivedF46CSVFile(String filePath) {
        return true;
    }

    private List<File> getNewFilesList(String path, Date lastCheck) {
        try {
            return FileUtil.getNewFiles(path, lastCheck.getTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
