package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeDetails;
import com.khanh.sample.models.TradeMetadata;
import com.khanh.sample.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DMP {
    private String nuggetPath;
    private String forwardedNuggetPath;
    private String archivedNuggetPath;
    private String csvPath;

    private Map<String, Map.Entry<TradeDetails, TradeMetadata>> nuggetData;
    private Map<String, List<Trade>> csvData;

    Date lastNuggetCheck;
    Date lastCSVCheck;

    public DMP(String nuggetPath, String forwardedNuggetPath,
               String archivedNuggetPath, String csvPath) {
        this.nuggetPath = nuggetPath;
        this.forwardedNuggetPath = forwardedNuggetPath;
        this.archivedNuggetPath = archivedNuggetPath;
        this.csvPath = csvPath;

        lastNuggetCheck = new Date();
        lastCSVCheck = new Date();

        this.nuggetData = new HashMap<>();
        this.csvData = new HashMap<>();
    }

    public void executeNugget() {
        processNewNuggetFile();
        forwardNuggets();
    }

    public void forwardNuggets() {
        try {
            for (Iterator<Map.Entry<String, Map.Entry<TradeDetails, TradeMetadata>>> it = this.nuggetData.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Map.Entry<TradeDetails, TradeMetadata>> entry = it.next();

                forwardNuggetFile(entry.getKey());
                archiveNuggetFile(entry.getKey());

                it.remove();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void executeF46CSV() {
        processNewF46CSVFile();

        for (String csv : csvData.keySet()) {

            try {
                archiveF46CSVFile(csv);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void processNewNuggetFile() {
        List<File> files = getNewFilesList(this.nuggetPath, this.lastNuggetCheck, "tar.gz");
        lastNuggetCheck = new Date();

        for (File nuggetFile : files) {

            Map.Entry<TradeDetails, TradeMetadata> nuggetData = NuggetUtil.readNugget(nuggetFile);
            if (nuggetData != null) {
                this.nuggetData.put(nuggetFile.getName(), nuggetData);
            }

        }

    }

    private void forwardNuggetFile(String source) throws IOException {
        FileUtil.copyFile(new File(this.nuggetPath + File.separator + source), new File(this.forwardedNuggetPath));
    }

    private void archiveNuggetFile(String source) throws IOException {
        FileUtil.copyFile(new File(this.nuggetPath + File.separator + source), new File(this.archivedNuggetPath));
    }


    public void processNewF46CSVFile() {
        List<File> files = getNewFilesList(this.csvPath, this.lastCSVCheck, "csv");
        lastCSVCheck = new Date();

        for (File csvFile : files) {
            try {
                List<Trade> trades = CSVUtil.readFromFile(csvFile, Trade.class);
                csvData.put(csvFile.getName(), trades);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void archiveF46CSVFile(String source) throws IOException {
        FileUtil.copyFile(new File(this.csvPath + File.separator + source), new File(this.archivedNuggetPath));
    }

    private List<File> getNewFilesList(String path, Date lastCheck, String extension) {
        try {
            return FileUtil.getNewFiles(path, lastCheck.getTime(), extension);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Map<String, Map.Entry<TradeDetails, TradeMetadata>> getNuggetData() {
        return nuggetData;
    }

    public void setNuggetData(Map<String, Map.Entry<TradeDetails, TradeMetadata>> nuggetData) {
        this.nuggetData = nuggetData;
    }

    public Map<String, List<Trade>> getCsvData() {
        return csvData;
    }

    public void setCsvData(Map<String, List<Trade>> csvData) {
        this.csvData = csvData;
    }
}
