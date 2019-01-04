package com.khanh.sample.cucumber.steps;

import com.khanh.sample.TestUtil;
import com.khanh.sample.models.Trade;
import com.khanh.sample.utils.JsonServer;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RESTGetTradesSteps {
    JsonServer jsonServer;
    RestTemplate restTemplate;
    String hostname;
    int port;

    ResponseEntity<List<Trade>> results;

    @Given("RESTful server runs at (.*), port (\\d+)")
    public void givenJsonServer(String hostname, int port) throws IOException {
        this.hostname = hostname;
        this.port = port;
        jsonServer = new JsonServer(hostname, port);
    }

    @And("^System has (\\d+) trades that id is starting from (\\d+)$")
    public void andTradeSetup(int tradeAmount, int startId) {
        List<Trade> trades = TestUtil.generateTrades(tradeAmount, startId);
        jsonServer.getDatabase().setTrades(trades);
    }


    @When("^API request to /trades/list\\?pageSize=(\\d+)&pageIndex=(\\d+)$")
    public void whenRequestToServer(int pageSize, int pageIndex) {
        restTemplate = new RestTemplate();

        String url = String.format("http://{host}:{port}/trades/list?pageSize={pageSize}&pageIndex={pageIndex}");

        Map<String, String> params = new HashMap<>();
        params.put("host", hostname);
        params.put("port", String.valueOf(port));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("pageIndex", String.valueOf(pageIndex));

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

        URI uri = builder.buildAndExpand(params).toUri();
        results = restTemplate.exchange(uri, HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<List<Trade>>() {});
    }

    @Then("RESTful server returns (\\d+) trades")
    public void thenReceiveResponse(int tradeAmount) {
        List<Trade> trades = results.getBody();
        Assert.assertEquals(tradeAmount, trades.size());
        jsonServer.stop();
    }
}



