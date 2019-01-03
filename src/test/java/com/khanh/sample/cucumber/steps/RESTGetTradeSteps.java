package com.khanh.sample.cucumber.steps;

import com.khanh.sample.TestUtil;
import com.khanh.sample.models.Trade;
import com.khanh.sample.utils.JsonServer;
import com.khanh.sample.utils.JsonUtil;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RESTGetTradeSteps {
    JsonServer jsonServer;
    List<Trade> trades;
    RestTemplate restTemplate;
    String hostname;
    int port;
    String apiResponseBody;

    @Given("RESTful server runs at (.*), port (\\d+)")
    public void givenJsonServer(String hostname, int port) throws IOException {
        this.hostname = hostname;
        this.port = port;
        jsonServer = new JsonServer(hostname, port);
    }

    @And("^System has (\\d+) trades that id is starting from (\\d+)$")
    public void andTradeSetup(int tradeAmount, int startId) {
        trades = TestUtil.generateTrades(tradeAmount, startId);
        jsonServer.getDatabase().setTrades(trades);
    }


    @When("^API request to /trades/list\\?pageSize=(\\d+)&pageIndex=(\\d+)$")
    public void whenRequestToServer(int pageSize, int pageIndex) {
        restTemplate = new RestTemplate();

        String uri = String.format("http://{host}:{port}/trades/list?pageSize={pageSize}&pageIndex={pageIndex}");

        Map<String, String> params = new HashMap<>();
        params.put("host", hostname);
        params.put("port", String.valueOf(port));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("pageIndex", String.valueOf(pageIndex));

        apiResponseBody = restTemplate.getForObject(uri, String.class, params);

    }

    @Then("RESTful server returns (\\d+) trades")
    public void thenReceiveResponse(int tradeAmount) throws IOException, ClassNotFoundException {
        Trade[] trades = JsonUtil.jsontoArray(apiResponseBody, Trade.class);
        Assert.assertEquals(tradeAmount, trades.length);
        jsonServer.stop();
    }
}



