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

public class BRS {
    private final static String datePattern = "yyyyMMddHHmmss";
    private String csvPath;
    private String nuggetPath;

    private Map<String, List<Trade>> trades;
    private Date lastCheckCsv;

    public BRS(String csvPath, String nuggetPath) {
        this.csvPath = csvPath;
        this.nuggetPath = nuggetPath;

        setTrades(new HashMap<>());
        lastCheckCsv = new Date();
    }

    public void execute() {
        processNewF365CSVFile();

        // I assuming that the BRS will combine all CSV data from multiple new files and export into one .tar.gz file
        List<Trade> trades = new ArrayList<>();
        for (Map.Entry<String, List<Trade>> entry : this.trades.entrySet()) {
            trades.addAll(entry.getValue());
        }

        createNuggetFile(trades);

        this.trades.clear();
    }

    public void processNewF365CSVFile() {
        List<File> files = getNewFilesList(this.csvPath, this.lastCheckCsv);
        this.lastCheckCsv = new Date();

        for (File file : files) {
            try {
                List<Trade> newTrades = CSVUtil.readFromFile(file, Trade.class);
                this.getTrades().put(file.getName(), newTrades);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String createNuggetFile(List<Trade> trades) {
        if(trades.size() == 0)
            return "";

        String time = getCurrentTimeString();

        TradeDetails details = new TradeDetails(trades);
        String detailsFilePath = this.nuggetPath + File.separator + time + "-details.xml";
        try {
            XmlUtil.writeToFile(detailsFilePath, details);
        } catch (IOException e) {
            e.printStackTrace();
        }

        TradeMetadata metadata = new TradeMetadata(trades);
        String metadataFilePath = this.nuggetPath + File.separator + time + "-metadata.xml";
        try {
            XmlUtil.writeToFile(metadataFilePath, metadata);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileName = time + ".tar.gz";
        String tarGzFilePath = this.nuggetPath + File.separator + fileName;
        try {
            CompressUtil.createTarFile(tarGzFilePath, new String[]{detailsFilePath, metadataFilePath});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileName;
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

    public Map<String, List<Trade>> getTrades() {
        return trades;
    }

    public void setTrades(Map<String, List<Trade>> trades) {
        this.trades = trades;
    }
}
