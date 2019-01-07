package com.khanh.sample.cucumber.steps;

import com.khanh.sample.BNP;
import com.khanh.sample.BRS;
import com.khanh.sample.DMP;
import com.khanh.sample.TestUtil;
import com.khanh.sample.models.Trade;
import com.khanh.sample.utils.FileUtil;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BNPSteps {
    BRS brs;
    DMP dmp;
    BNP bnp;
    List<Trade> trades;

    @Given("DMP reads nugget file from \"(.*)\" and forwards processed nugget file to \"(.*)\" for BNP to read nugget data. BNP save matching trade data into \"(.*)\"")
    public void givenDMP(String nuggetPath, String forwardedNuggetPath, String f46CsvPath) {
        brs = new BRS("", nuggetPath);
        dmp = new DMP(nuggetPath, forwardedNuggetPath, "", f46CsvPath);
        bnp = new BNP(forwardedNuggetPath, f46CsvPath);

        FileUtil.deleteDirectory(nuggetPath);
        FileUtil.deleteDirectory(forwardedNuggetPath);
        FileUtil.deleteDirectory(f46CsvPath);
    }

    @When("DMP forwards a nugget file of (\\d+) trades")
    public void whenDMPForwardAnNugget(int tradeAmount) throws IOException {
        trades = TestUtil.generateTrades(tradeAmount, 1);
        brs.createNuggetFile(trades);
        dmp.executeNugget();
    }

    @Then("BNP creates (\\d+) F46 CSV file in \"(.*)\"")
    public void thenBNPCreateF46CSV(int fileAmount, String f46CsvPath) {
        bnp.execute();

        File file = new File(f46CsvPath);
        File[] files = file.listFiles();
        Assert.assertNotNull(files);
        Assert.assertEquals(fileAmount, files.length);
    }

    @And("DMP sees same (\\d+) trades from that new F46 CSV file")
    public void andDMPReadCsv(int tradeAmount) {
        dmp.executeF46CSV();
        Map<String, List<Trade>> trades = dmp.getCsvData();
        Assert.assertEquals(1, trades.size());

        Map.Entry<String, List<Trade>> entry = trades.entrySet().iterator().next();
        Assert.assertEquals(tradeAmount, entry.getValue().size());
        TestUtil.assertEqualsTrades(this.trades, entry.getValue());
    }
}
