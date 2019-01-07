package com.khanh.sample;

import com.khanh.sample.models.*;
import com.khanh.sample.utils.CSVUtil;
import com.khanh.sample.utils.FileUtil;
import com.khanh.sample.utils.NuggetUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class Validator {
    private String f365CsvPath;
    private String nuggetPath;
    private String forwardedNuggetPath;
    private String archivedNuggetPath;
    private String f46CsvPath;

    private Date lastCheckF365Csv;
    private Date lastCheckNugget;
    private Date lastCheckForwardedNugget;
    private Date lastCheckArchivedNugget;
    private Date lastCheckF46Csv;
    private Date lastCheckArchivedF46Csv;

    private List<TradeInfo> tradesInfo;

    public Validator(String f365CsvPath, String nuggetPath, String forwardedNuggetPath,
                     String archivedNuggetPath, String f46CsvPath) {
        this.f365CsvPath = f365CsvPath;
        this.nuggetPath = nuggetPath;
        this.forwardedNuggetPath = forwardedNuggetPath;
        this.archivedNuggetPath = archivedNuggetPath;
        this.f46CsvPath = f46CsvPath;

        this.tradesInfo = new ArrayList<>();

        lastCheckF365Csv = new Date();
        lastCheckNugget = new Date();
    }

    public boolean verifyF365CSVFile() {
        List<File> newFiles = getNewFilesList(f365CsvPath, lastCheckF365Csv, "csv");
        lastCheckF365Csv = new Date();

        for (File file : newFiles) {
            List<Trade> newTrades = null;
            try {
                newTrades = CSVUtil.readFromFile(file, Trade.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (newTrades != null) {
                for (Trade newTrade : newTrades) {
                    // TODO: extra F365 verification should be added here

                    TradeInfo tradeInfo = new TradeInfo(newTrade);
                    tradesInfo.add(tradeInfo);
                }
            }
        }
        return true;
    }

    public boolean verifyNuggetFile() {
        List<File> newFiles = getNewFilesList(nuggetPath, lastCheckNugget, "tar.gz");
        lastCheckNugget = new Date();

        for (File nuggetFile : newFiles) {
            Map.Entry<TradeDetails, TradeMetadata> nuggetData = NuggetUtil.readNugget(nuggetFile);
            if (nuggetData != null) {
                // TODO: do the verification here to ensure the TradeDetails and TradeMetadata is correct
                checkAndUpdateTradeDetailsAndMetaData(nuggetData, tradeInfo -> {
                    tradeInfo.updateTradeStatus(TradeStatus.NuggetCreated);
                });
            }
        }

        return true;
    }

    private void checkAndUpdateTradeDetailsAndMetaData(Map.Entry<TradeDetails, TradeMetadata> nuggetData,
                                                       Consumer<TradeInfo> updateTradeInfoConsumer) {
        // Not sure the structure of nugget data and their valid condition
        // will need to be updated later
        List<Trade> tradeList1 = nuggetData.getKey().getTrades();
        List<Trade> tradeList2 = nuggetData.getValue().getTrades();

        for (int i = 0; i < tradeList1.size(); i++) {
            Trade trade = tradeList1.get(i);
            if (trade.getTradeId() == tradeList2.get(i).getTradeId()) {
                TradeInfo tradeInfo = findTradeInfoById(trade.getTradeId());
                if (tradeInfo != null) {
                    updateTradeInfoConsumer.accept(tradeInfo);
                }
            }
        }
    }

    private TradeInfo findTradeInfoById(long id) {
        for (int i = 0; i < tradesInfo.size(); i++) {
            TradeInfo tradeInfo = tradesInfo.get(i);
            if (tradeInfo.getTrade().getTradeId() == id) {
                return tradeInfo;
            }
        }
        return null;
    }

    public boolean verifyForwardedNuggetFile() {
        return true;
    }

    public boolean verifyArchivedNuggetFile() {
        return true;
    }

    public boolean verifyF46CSVFile() {
        return true;
    }

    public boolean verifyArchivedF46CSVFile() {
        return true;
    }

    public List<TradeInfo> getTradesInfo() {
        return this.tradesInfo;
    }

    private List<File> getNewFilesList(String path, Date lastCheck, String extension) {
        try {
            return FileUtil.getNewFiles(path, lastCheck.getTime(), extension);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
