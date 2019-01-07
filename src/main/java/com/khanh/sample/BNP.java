package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeDetails;
import com.khanh.sample.models.TradeMetadata;
import com.khanh.sample.utils.*;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BNP {
    private final static String datePattern = "yyyyMMddHHmmss";
    private String csvPath;
    private String nuggetPath;

    private Map<String, Map.Entry<TradeDetails, TradeMetadata>> nuggetData;

    Date lastNuggetCheck;

    public BNP(String nuggetPath, String csvPath) {
        this.csvPath = csvPath;
        this.nuggetPath = nuggetPath;

        this.nuggetData = new HashMap<>();

        this.lastNuggetCheck = new Date();
    }

    public String execute() {
        processNewNuggetFile();

        // I assuming that the BRS will combine all CSV data from multiple new files and export into one .tar.gz file
        List<Trade> trades = new ArrayList<>();
        for (Map.Entry<String, Map.Entry<TradeDetails, TradeMetadata>> entry : this.nuggetData.entrySet()) {
            trades.addAll(entry.getValue().getValue().getTrades());
        }

        String csvFileName = createF46CSVFile(trades);
        if(!csvFileName.equals("")) {
            this.nuggetData.clear();
        }
        return csvFileName;
    }

    public void processNewNuggetFile() {
        List<File> files = getNewFilesList(this.nuggetPath, this.lastNuggetCheck);
        this.lastNuggetCheck = new Date();

        for (File nuggetFile : files) {

            Map.Entry<TradeDetails, TradeMetadata> nuggetData = NuggetUtil.readNugget(nuggetFile);
            if(nuggetData != null) {
                this.nuggetData.put(nuggetFile.getName(), nuggetData);
            }
        }
    }

    public String createF46CSVFile(List<Trade> data) {
        if(data.size() == 0)
            return "";

        String fileName = getCurrentTimeString() + ".csv";
        try {
            CSVUtil.writeToFile(this.csvPath + File.separator + fileName, Trade.class, data);
            return fileName;
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    private String getCurrentTimeString() {
        DateFormat df = new SimpleDateFormat(datePattern);
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }

    private List<File> getNewFilesList(String path, Date lastCheck) {
        try {
            return FileUtil.getNewFiles(path, lastCheck.getTime());
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
}
