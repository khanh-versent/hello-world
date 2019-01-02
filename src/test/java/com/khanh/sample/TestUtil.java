package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeDetails;
import com.khanh.sample.models.TradeMetadata;
import com.khanh.sample.utils.CompressUtil;
import com.khanh.sample.utils.XmlUtil;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestUtil {
    private static final double DELTA = 1e-15;

    public static void assertEqualsTrade(Trade expected, Trade actual) {
        Assert.assertEquals(expected.getVolume(), actual.getVolume(), DELTA);
        Assert.assertEquals(expected.getPrice(), actual.getPrice(), DELTA);
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
    }

    public static void assertEqualsTrades(List<Trade> expected, List<Trade> actual) {
        for(Trade expectedTrade : expected) {
            Trade actualTrade = findById(actual, expectedTrade.getTradeId());
            assertEqualsTrade(expectedTrade, actualTrade);
        }
    }

    public static void assertCheckNuggetFile(List<Trade> expectedTrades, File nuggetFile) throws IOException {
        TradeDetails details = null;
        TradeMetadata metadata = null;

        List<String> filePaths = CompressUtil.extractTarFile(nuggetFile, nuggetFile.getParentFile());
        for (String extractedFilePath : filePaths) {
            if (extractedFilePath.contains("details"))
                details = XmlUtil.readFromFile(extractedFilePath, TradeDetails.class);
            else if (extractedFilePath.contains("metadata")) {
                metadata = XmlUtil.readFromFile(extractedFilePath, TradeMetadata.class);
            }
        }

        Assert.assertNotNull(details);
        Assert.assertNotNull(metadata);

        assertEqualsTrades(expectedTrades, details.getTrades());
        assertEqualsTrades(expectedTrades, metadata.getTrades());
    }

    private static Trade findById(List<Trade> trades, long id) {
        for(Trade trade : trades) {
            if(trade.getTradeId() == id) {
                return trade;
            }
        }
        return null;
    }

    public static double getRandomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static List<Trade> generateTrades(int amount, long startId) {
        List<Trade> trades = new ArrayList<Trade>();
        for(int i = 0; i < amount; i++) {
            long id = i + startId;
            trades.add(new Trade(id, TestUtil.getRandomDouble(), TestUtil.getRandomDouble(), "Trade " + id));
        }
        return trades;
    }
}
