package com.khanh.sample.cucumber.steps;

import com.khanh.sample.BRS;
import com.khanh.sample.SIM;
import com.khanh.sample.TestUtil;
import com.khanh.sample.models.Trade;
import com.khanh.sample.utils.CSVUtil;
import com.khanh.sample.utils.FileUtil;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SIMSteps {
    SIM sim;

    @Given("^that SIM save F365 CSV File into \"(.*)\" folder$")
    public void givenSIMFolder(String simFolder) {
        FileUtil.deleteDirectory(simFolder);

        sim = new SIM(simFolder);
    }

    @When("^SIM has (\\d+) trades$")
    public void whenSIMHasTrades(int amountOfTrade) {
        List<Trade> trades = TestUtil.generateTrades(amountOfTrade, 1);
        sim.createF365CSVFile(trades);
    }

    @Then("SIM creates (\\d+) CSV file of (\\d+) trades in \"(.*)\" folder")
    public void thenBRSCreateNuggetFile(int amountOfFile, int amountOfTrade, String folder) throws IOException {
        File dir = new File(folder);

        int count = 0;
        File[] files = dir.listFiles();

        Assert.assertEquals(files.length, amountOfFile);
        List<Trade> savedTrades = CSVUtil.readFromFile(files[0], Trade.class);

        Assert.assertEquals(amountOfTrade, savedTrades.size());
    }
}
