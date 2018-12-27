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
        createF365CSVFile(this.trades);
    }

    public void createF365CSVFile(List<Trade> data) {
        String fileName = this.csvPath + File.separator + getCurrentTimeString() + ".csv";

        try {
            CSVUtil.writeToFile(fileName, Trade.class, data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
