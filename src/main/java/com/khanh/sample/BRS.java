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
        createNuggetFiles();
    }

    public void createNuggetFiles() {
        for(String fileName : this.getTrades().keySet()) {
            try {
                createNuggetFile(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void processNewF365CSVFile() {
        List<File> files = getNewFilesList();
        this.lastCheckCsv = new Date();

        for(File file : files) {
            try {
                List<Trade> newTrades = CSVUtil.readFromFile(file, Trade.class);
                this.getTrades().put(file.getName(), newTrades);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void createNuggetFile(String fileName) throws IOException {
        String time = getCurrentTimeString();

        TradeDetails details = new TradeDetails(this.getTrades().get(fileName));
        String detailsFilePath = this.nuggetPath + File.separator + time + "-details.xml";
        XmlUtil.writeToFile(detailsFilePath, details);

        TradeMetadata metadata = new TradeMetadata(this.getTrades().get(fileName));
        String metadataFilePath = this.nuggetPath + File.separator + time + "-metadata.xml";
        XmlUtil.writeToFile(metadataFilePath, metadata);

        String tarGzFilePath = this.nuggetPath + File.separator + time + ".tar.gz";
        CompressUtil.createTarFile(tarGzFilePath, new String[] { detailsFilePath, metadataFilePath });

        this.getTrades().remove(fileName);
    }

    private String getCurrentTimeString() {
        DateFormat df = new SimpleDateFormat(datePattern);
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }

    private List<File> getNewFilesList() {
        try {
            return FileUtil.getNewFiles(this.csvPath, lastCheckCsv.getTime());
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
