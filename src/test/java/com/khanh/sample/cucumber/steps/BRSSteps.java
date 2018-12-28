package com.khanh.sample.cucumber.steps;

import com.khanh.sample.BRS;
import com.khanh.sample.SIM;
import com.khanh.sample.TestUtil;
import com.khanh.sample.models.Trade;
import com.khanh.sample.utils.FileUtil;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.io.File;
import java.util.List;

public class BRSSteps {
    SIM sim;
    BRS brs;

    @Given("^that BRS read F365 CSV in folder \"(.*)\" and then save nugget file into folder \"(.*)\"$")
    public void givenSIMAndBRSFolder(String simFolder, String brsFolder) {
        FileUtil.deleteDirectory(simFolder);
        FileUtil.deleteDirectory(brsFolder);

        sim = new SIM(simFolder);
        brs = new BRS(simFolder, brsFolder);
    }

    @When("^SIM upload csv of (\\d+) trades$")
    public void whenSIMCreateCSVFile(int amountOfTrade) {
        List<Trade> trades = TestUtil.generateTrades(amountOfTrade, 1);
        sim.createF365CSVFile(trades);
    }

    @Then("BRS create (\\d+) nugget file ends with \"(.*)\" in folder \"(.*)\"")
    public void thenBRSCreateNuggetFile(int amountOfFile, String fileType, String folder) {
        brs.execute();
        File dir = new File(folder);

        int count = 0;
        File[] files = dir.listFiles();
        for(File file : files) {
            if(file.getName().endsWith(fileType)) {
                count++;
            }
        }

        Assert.assertEquals(count, amountOfFile);
    }
}
