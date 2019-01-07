package com.khanh.sample;

import com.khanh.sample.models.*;
import com.khanh.sample.utils.CSVUtil;
import com.khanh.sample.utils.FileUtil;
import com.khanh.sample.utils.NuggetUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Validator {
    private String f365CsvPath;
    private String nuggetPath;
    private String forwardedNuggetPath;
    private String archivedPath;
    private String f46CsvPath;

    private Date lastCheckF365Csv;
    private Date lastCheckNugget;
    private Date lastCheckForwardedNugget;
    private Date lastCheckArchivedNugget;
    private Date lastCheckF46Csv;
    private Date lastCheckArchivedF46Csv;

    private List<TradeInfo> tradesInfo;

    public Validator(String f365CsvPath, String nuggetPath, String forwardedNuggetPath,
                     String archivedPath, String f46CsvPath) {
        this.f365CsvPath = f365CsvPath;
        this.nuggetPath = nuggetPath;
        this.forwardedNuggetPath = forwardedNuggetPath;
        this.archivedPath = archivedPath;
        this.f46CsvPath = f46CsvPath;

        this.tradesInfo = new ArrayList<>();

        lastCheckF365Csv = new Date();
        lastCheckNugget = new Date();
        lastCheckForwardedNugget = new Date();
        lastCheckArchivedNugget = new Date();
        lastCheckF46Csv = new Date();
        lastCheckArchivedF46Csv = new Date();
    }

    public boolean verifyF365CSVFile() {
        List<File> newFiles = FileUtil.getNewFiles(f365CsvPath, lastCheckF365Csv.getTime(), "csv");
        lastCheckF365Csv = new Date();

        for (File file : newFiles) {
            List<Trade> newTrades = null;
            try {
                newTrades = CSVUtil.readFromFile(file, Trade.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (newTrades == null) {
                return false;
            }


            for (Trade newTrade : newTrades) {
                // TODO: extra F365 verification should be added here

                TradeInfo tradeInfo = new TradeInfo(newTrade);
                tradesInfo.add(tradeInfo);
            }
        }
        return true;
    }

    public boolean verifyNuggetFile() {
        List<File> newFiles = FileUtil.getNewFiles(nuggetPath, lastCheckNugget.getTime(), "tar.gz");
        lastCheckNugget = new Date();

        for (File nuggetFile : newFiles) {
            Map.Entry<TradeDetails, TradeMetadata> nuggetData = NuggetUtil.readNugget(nuggetFile);
            if (nuggetData == null) {
                return false;
            }
            // TODO: do the verification here to ensure the TradeDetails and TradeMetadata is correct
            boolean success = checkAndUpdateTradeDetailsAndMetaData(nuggetData, tradeInfo -> {
                tradeInfo.updateTradeStatus(TradeStatus.NuggetCreated);
            });

            if (!success) {
                return false;
            }

        }
        return true;
    }

    public boolean verifyForwardedNuggetFile() {
        List<File> newFiles = FileUtil.getNewFiles(forwardedNuggetPath, lastCheckForwardedNugget.getTime(), "tar.gz");
        lastCheckForwardedNugget = new Date();

        for (File nuggetFile : newFiles) {
            Map.Entry<TradeDetails, TradeMetadata> nuggetData = NuggetUtil.readNugget(nuggetFile);
            if (nuggetData == null) {
                return false;
            }
            // TODO: do the verification here to ensure the TradeDetails and TradeMetadata is correct
            boolean success = checkAndUpdateTradeDetailsAndMetaData(nuggetData, tradeInfo -> {
                switch (tradeInfo.getTradeStatus()) {
                    case NuggetCreated:
                        tradeInfo.updateTradeStatus(TradeStatus.NuggetForwarded);
                        break;
                    case NuggetArchived:
                        tradeInfo.updateTradeStatus(TradeStatus.NuggetForwardedAndArchived);
                        break;
                }
            });

            if (!success) {
                return false;
            }
        }
        return true;
    }

    public boolean verifyArchivedNuggetFile() {
        List<File> newFiles = FileUtil.getNewFiles(archivedPath, lastCheckArchivedNugget.getTime(), "tar.gz");
        lastCheckArchivedNugget = new Date();

        for (File nuggetFile : newFiles) {
            Map.Entry<TradeDetails, TradeMetadata> nuggetData = NuggetUtil.readNugget(nuggetFile);
            if (nuggetData == null) {
                return false;
            }
            // TODO: do the verification here to ensure the TradeDetails and TradeMetadata is correct
            boolean success = checkAndUpdateTradeDetailsAndMetaData(nuggetData, tradeInfo -> {
                switch (tradeInfo.getTradeStatus()) {
                    case NuggetCreated:
                        tradeInfo.updateTradeStatus(TradeStatus.NuggetArchived);
                        break;
                    case NuggetForwarded:
                        tradeInfo.updateTradeStatus(TradeStatus.NuggetForwardedAndArchived);
                        break;
                }
            });

            if (!success) {
                return false;
            }
        }
        return true;
    }

    public boolean verifyF46CSVFile() {
        List<File> newFiles = FileUtil.getNewFiles(f46CsvPath, lastCheckF46Csv.getTime(), "csv");
        lastCheckF46Csv = new Date();

        for (File file : newFiles) {
            List<Trade> trades = null;
            try {
                trades = CSVUtil.readFromFile(file, Trade.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (trades == null) {
                return false;
            }

            for (Trade trade : trades) {
                TradeInfo tradeInfo = findTradeInfoById(trade.getTradeId());

                if (tradeInfo != null) {
                    // TODO: do the verification here to ensure the data read from CSV is correct


                    // TODO: depends on the progress, update the status to Matching or Matched?
                    tradeInfo.updateTradeStatus(TradeStatus.TradeMatching);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean verifyArchivedF46CSVFile() {
        List<File> newFiles = FileUtil.getNewFiles(archivedPath, lastCheckArchivedF46Csv.getTime(), "csv");
        lastCheckArchivedF46Csv = new Date();

        for (File file : newFiles) {
            List<Trade> trades = null;
            try {
                trades = CSVUtil.readFromFile(file, Trade.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (trades == null) {
                return false;
            }

            for (Trade trade : trades) {
                TradeInfo tradeInfo = findTradeInfoById(trade.getTradeId());

                if (tradeInfo != null) {
                    // TODO: do the verification here to ensure the data read from CSV is correct


                    // TODO: depends on the progress, update the status to Matching or Matched?
                    tradeInfo.updateTradeStatus(TradeStatus.F46Archived);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public List<String> validateTrades() {
        List<String> alerts = new ArrayList<>();
        Date current = new Date();

        for (TradeInfo tradeInfo : tradesInfo) {

            long minutes = TimeUnit.MILLISECONDS.toMinutes(current.getTime() - tradeInfo.getLastUpdate().getTime());
            if(minutes > 60) {
                // If trade is not being updated in 60 minutes, we should raise alert
                alerts.add(String.format("Trade with id %s has not been updated for more than 60 minutes.", tradeInfo.getTrade().getTradeId()));
            }

        }
        return alerts;
    }

    public List<TradeInfo> getTradesInfo() {
        return this.tradesInfo;
    }

    private boolean checkAndUpdateTradeDetailsAndMetaData(Map.Entry<TradeDetails, TradeMetadata> nuggetData,
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
                } else {
                    return false;
                }
            }
        }
        return true;
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
}
