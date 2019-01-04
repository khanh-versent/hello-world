package com.khanh.sample.cucumber.steps;

import com.khanh.sample.utils.SimpleHttpServer;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

public class WebLoginSteps {
    SimpleHttpServer server;
    String hostname;
    int port;
    WebDriver webDriver;

    @Given("Web server runs at (.*) and port (\\d+)")
    public void givenWebServer(String hostname, int port) throws IOException {
        this.hostname = hostname;
        this.port = port;

        this.server = new SimpleHttpServer(hostname, port);
    }

    @Given("User accesses (.*) page")
    public void givenAccessLogin(String path) {
        this.webDriver = new ChromeDriver();
        this.webDriver.get(String.format("http://%s:%d%s", this.hostname, this.port, path));
    }

    @When("User enters (.*) and (.*)")
    public void whenEnterUsernameAndPassword(String username, String password) {
        this.webDriver.findElement(By.id("username")).sendKeys(username);
        this.webDriver.findElement(By.id(("password"))).sendKeys(password);
        this.webDriver.findElement(By.id(("submit"))).click();
    }

    @When("User receives (.*)")
    public void thenReceiveMessage(String expectedMessage) {
        String actualMessage = this.webDriver.findElement(By.id("message")).getText();
        //System.out.println(actualMessage);
        Assert.assertEquals(expectedMessage, actualMessage);
        this.server.stop();
        this.webDriver.quit();
    }
}
