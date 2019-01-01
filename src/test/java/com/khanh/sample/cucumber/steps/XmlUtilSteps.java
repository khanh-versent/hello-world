package com.khanh.sample.cucumber.steps;

import com.khanh.sample.TestUtil;
import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeDetails;
import com.khanh.sample.models.TradeMetadata;
import com.khanh.sample.utils.XmlUtil;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class XmlUtilSteps {
    String directory;
    String detailsPath;
    String metadataPath;
    List<Trade> trades;

    @Given("^TradeDetails and TradeMetaData hold a list of Trade and system save xml data file into \"(.*)\" folder$")
    public void givenSaveFolder(String directory) {
        this.directory = directory;
    }

    @When("^system has a list of (\\d++) trades and save as \"(.*)\" and \"(.*)\"$")
    public void whenSaveIntoFiles(int tradeAmount, String detailsFile, String metadataFile) throws IOException {
        trades = TestUtil.generateTrades(tradeAmount, 1);

        TradeDetails details = new TradeDetails(trades);
        TradeMetadata metadata = new TradeMetadata(trades);

        detailsPath = this.directory + File.separator + detailsFile;
        metadataPath = this.directory + File.separator + metadataFile;

        XmlUtil.writeToFile(detailsPath, details);
        XmlUtil.writeToFile(metadataPath, metadata);
    }

    @Then("system reads back same identical list of (\\d++) trades")
    public void thenReadBackFiles(int tradeAmount) throws IOException {

        TradeDetails details = XmlUtil.readFromFile(detailsPath, TradeDetails.class);
        TradeMetadata metadata = XmlUtil.readFromFile(metadataPath, TradeMetadata.class);

        Assert.assertEquals(tradeAmount, details.getTrades().size());
        Assert.assertEquals(tradeAmount, metadata.getTrades().size());
    }
}
