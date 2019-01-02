package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeDetails;
import com.khanh.sample.models.TradeMetadata;
import com.khanh.sample.utils.CSVUtil;
import com.khanh.sample.utils.CompressUtil;
import com.khanh.sample.utils.FileUtil;
import com.khanh.sample.utils.XmlUtil;

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

    public void execute() {
        processNewNuggetFile();

        // I assuming that the BRS will combine all CSV data from multiple new files and export into one .tar.gz file
        List<Trade> trades = new ArrayList<>();
        for (Map.Entry<String, Map.Entry<TradeDetails, TradeMetadata>> entry : this.nuggetData.entrySet()) {
            trades.addAll(entry.getValue().getValue().getTrades());
        }

        createF46CSVFile(trades);
        this.nuggetData.clear();
    }

    public void processNewNuggetFile() {
        List<File> files = getNewFilesList(this.nuggetPath, this.lastNuggetCheck);
        this.lastNuggetCheck = new Date();

        for (File nuggetFile : files) {
            try {
                TradeDetails details = null;
                TradeMetadata metadata = null;

                List<String> filePaths = CompressUtil.extractTarFile(nuggetFile);
                for (String extractedFilePath : filePaths) {
                    if (extractedFilePath.contains("details"))
                        details = XmlUtil.readFromFile(extractedFilePath, TradeDetails.class);
                    else if (extractedFilePath.contains("metadata")) {
                        metadata = XmlUtil.readFromFile(extractedFilePath, TradeMetadata.class);
                    }
                }

                if(details != null && metadata != null) {
                    this.nuggetData.put(nuggetFile.getName(), Map.entry(details, metadata));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void createF46CSVFile(List<Trade> data) {
        if(data.size() == 0)
            return;

        String fileName = getCurrentTimeString() + ".csv";

        try {
            CSVUtil.writeToFile(this.csvPath + File.separator + fileName, Trade.class, data);
        } catch (IOException ex) {
            ex.printStackTrace();
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
