package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeInfo;
import com.khanh.sample.models.TradeStatus;
import com.khanh.sample.utils.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class FlowTest {
    private static final String F365_CSV_PATH = "data" + File.separator + "FOLDER1";
    private static final String NUGGET_PATH = "data" + File.separator + "FOLDER2";
    private static final String FORWARDED_NUGGET_PATH = "data" + File.separator + "FOLDER3";
    private static final String ARCHIVED_PATH = "data" + File.separator + "FOLDER4";
    private static final String F46_CSV_PATH = "data" + File.separator + "FOLDER5";

    @Test
    public void testEndToEndFlow() {
        FileUtil.deleteDirectory(F365_CSV_PATH);
        FileUtil.deleteDirectory(NUGGET_PATH);
        FileUtil.deleteDirectory(FORWARDED_NUGGET_PATH);
        FileUtil.deleteDirectory(ARCHIVED_PATH);
        FileUtil.deleteDirectory(F46_CSV_PATH);

        SIM sim = new SIM(F365_CSV_PATH);
        Validator validator = new Validator(F365_CSV_PATH, NUGGET_PATH, FORWARDED_NUGGET_PATH, ARCHIVED_PATH, F46_CSV_PATH);
        BRS brs = new BRS(F365_CSV_PATH, NUGGET_PATH);
        DMP dmp = new DMP(NUGGET_PATH, FORWARDED_NUGGET_PATH, ARCHIVED_PATH, F46_CSV_PATH);
        BNP bnp = new BNP(FORWARDED_NUGGET_PATH, F46_CSV_PATH);

        List<Trade> trades = TestUtil.generateTrades(100, 1);

        sim.setTrades(trades);
        sim.execute();
        Assert.assertTrue(validator.verifyF365CSVFile());
        assertTradeInfo(validator, trades, TradeStatus.F365Created);

        brs.execute();
        Assert.assertTrue(validator.verifyNuggetFile());
        assertTradeInfo(validator, trades, TradeStatus.NuggetCreated);

        dmp.executeNugget();
        Assert.assertTrue(validator.verifyForwardedNuggetFile());
        assertTradeInfo(validator, trades, TradeStatus.NuggetForwarded);

        Assert.assertTrue(validator.verifyArchivedNuggetFile());
        assertTradeInfo(validator, trades, TradeStatus.NuggetForwardedAndArchived);

        bnp.execute();
        Assert.assertTrue(validator.verifyF46CSVFile());
        assertTradeInfo(validator, trades, TradeStatus.TradeMatching);

        dmp.executeF46CSV();
        Assert.assertTrue(validator.verifyF46CSVFile());
        Assert.assertTrue(validator.verifyArchivedF46CSVFile());
        assertTradeInfo(validator, trades, TradeStatus.F46Archived);

        Assert.assertEquals(0, validator.validateTrades().size());
    }

    void assertTradeInfo(Validator validator, List<Trade> trades, TradeStatus expectedStatus) {
        List<TradeInfo> tradesInfo = validator.getTradesInfo();
        Assert.assertEquals(trades.size(), tradesInfo.size());

        for (Trade trade : trades) {
            TradeInfo tradeInfo = findTradeInfoById(tradesInfo, trade.getTradeId());
            Assert.assertNotNull(tradeInfo);
            Assert.assertEquals(expectedStatus, tradeInfo.getTradeStatus());
        }
    }

    TradeInfo findTradeInfoById(List<TradeInfo> tradesInfo, long id) {
        for (int i = 0; i < tradesInfo.size(); i++) {
            TradeInfo tradeInfo = tradesInfo.get(i);
            if (tradeInfo.getTrade().getTradeId() == id) {
                return tradeInfo;
            }
        }
        return null;
    }
}
