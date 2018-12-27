package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.utils.CSVUtil;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SIM {
    private final static String datePattern = "yyyyMMddHHmmss";
    private String csvPath;
    private List<Trade> trades;

    public SIM(String csvPath) {
        this.csvPath = csvPath;
    }

    public void execute() {
        try {
            createF365CSVFile(this.trades);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createF365CSVFile(List<Trade> data) throws IOException {
        String fileName = this.csvPath + File.separator + getCurrentTimeString() + ".csv";
        CSVUtil.writeToFile(fileName, Trade.class, data);
    }

    private String getCurrentTimeString() {
        DateFormat df = new SimpleDateFormat(datePattern);
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }
}
