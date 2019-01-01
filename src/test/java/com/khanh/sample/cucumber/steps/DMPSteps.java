package com.khanh.sample.cucumber.steps;

import com.khanh.sample.BRS;
import com.khanh.sample.DMP;
import com.khanh.sample.SIM;
import com.khanh.sample.TestUtil;
import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeDetails;
import com.khanh.sample.models.TradeMetadata;
import com.khanh.sample.utils.FileUtil;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DMPSteps {
    SIM sim;
    BRS brs;
    DMP dmp;
    List<Trade> trades;

    @Given("^that SIM creates CSV file to \"(.*)\" folder, BRS use that CSV to create nugget to \"(.*)\" folder, DMP read nugget file and forward to \"(.*)\" and \"(.*)\" folder$")
    public void givenFolders(String f365Csv, String nugget, String forwardedNugget, String archivedNugget) {
        FileUtil.deleteDirectory(f365Csv);
        FileUtil.deleteDirectory(nugget);
        FileUtil.deleteDirectory(forwardedNugget);
        FileUtil.deleteDirectory(archivedNugget);

        sim = new SIM(f365Csv);
        brs = new BRS(f365Csv, nugget);
        dmp = new DMP(nugget, forwardedNugget, archivedNugget, "");
    }

    @When("^SIM creates a CSV of (\\d+) trades$")
    public void whenSIMHasTrades(int amountOfTrade) {
        trades = TestUtil.generateTrades(amountOfTrade, 1);
        sim.createF365CSVFile(trades);
        brs.execute();
    }

    @Then("DMP sees (\\d+) new file ends with \"(.*)\" containing those (\\d+) trades in \"(.*)\", then send copy to \"([\\w\\d]+)\", and \"([\\w]+)\" folders")
    public void thenDMPReadNuggetFile(int amountOfFile, String fileType, int amountOfTrade, String brsFolder, String forwardedFolder, String archivedFolder) throws IOException {
        File dir = new File(brsFolder);

        int count = 0;
        File[] files = dir.listFiles();
        for(File file : files) {
            if(file.getName().endsWith(fileType)) {
                count++;
            }
        }
        Assert.assertEquals(count, amountOfFile);

        dmp.executeNugget();

        Map<String, Map.Entry<TradeDetails, TradeMetadata>> data = dmp.getNuggetData();
        Assert.assertEquals(amountOfFile, data.size());

        for(Map.Entry<String, Map.Entry<TradeDetails, TradeMetadata>> entry : data.entrySet()) {
            TradeDetails details = entry.getValue().getKey();
            TradeMetadata metadata = entry.getValue().getValue();

            Assert.assertEquals(amountOfTrade, details.getTrades().size());
            Assert.assertEquals(amountOfTrade, metadata.getTrades().size());
        }

        File file = new File(forwardedFolder);
        Assert.assertNotNull(file);
        Assert.assertNotNull(file.listFiles());
        Assert.assertEquals(amountOfFile, file.listFiles().length);

        file = new File(archivedFolder);
        Assert.assertNotNull(file);
        Assert.assertNotNull(file.listFiles());
        Assert.assertEquals(amountOfFile, file.listFiles().length);
    }
}