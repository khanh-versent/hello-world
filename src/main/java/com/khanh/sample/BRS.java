package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeDetails;
import com.khanh.sample.models.TradeMetadata;
import com.khanh.sample.utils.CSVUtil;
import com.khanh.sample.utils.CompressUtil;
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

    private Map<String, List<Trade>>  trades;

    public BRS(String csvPath, String nuggetPath) {
        this.csvPath = csvPath;
        this.nuggetPath = nuggetPath;

        trades = new HashMap<>();
    }

    public void execute() {
        checkNewF365CSVFile();

        for(String fileName : this.trades.keySet()) {
            try {
                createNuggetFile(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkNewF365CSVFile() {
        // TODO read the folder and check if there's new files
        List<String> fileNames = new ArrayList<>();

        for(String fileName : fileNames) {
            List<Trade> newTrades = null;

            try {
                newTrades = CSVUtil.readFromFile(fileName, Trade.class);
                this.trades.put(fileName, newTrades);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void createNuggetFile(String fileName) throws IOException {
        String time = getCurrentTimeString();

        TradeDetails details = new TradeDetails(this.trades.get(fileName));
        String detailsFilePath = this.nuggetPath + File.separator + time + "-details.xml";
        XmlUtil.writeToFile(detailsFilePath, details);

        TradeMetadata metadata = new TradeMetadata(this.trades.get(fileName));
        String metadataFilePath = this.nuggetPath + File.separator + time + "-metadata.xml";
        XmlUtil.writeToFile(metadataFilePath, metadata);

        String tarGzFilePath = this.nuggetPath + File.separator + time + ".tar.gz";
        CompressUtil.createTarFile(tarGzFilePath, new String[] { detailsFilePath, metadataFilePath });

        this.trades.remove(fileName);
    }

    private String getCurrentTimeString() {
        DateFormat df = new SimpleDateFormat(datePattern);
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }
}
