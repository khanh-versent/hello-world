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
    private SimpleHttpServer server;
    private WebDriver webDriver;

    @Given("Web server runs at (.*) and port (\\d+). User accesses login page at (.*)")
    public void givenUserAccessLoginPage(String hostname, int port, String path) throws IOException {
        this.server = new SimpleHttpServer(hostname, port);

        this.webDriver = new ChromeDriver();
        this.webDriver.get(String.format("http://%s:%d%s", hostname, port, path));
    }

    @When("User enters (.*) and (.*) in login form")
    public void whenEnterUsernameAndPassword(String username, String password) {
        this.webDriver.findElement(By.id("username")).sendKeys(username);
        this.webDriver.findElement(By.id(("password"))).sendKeys(password);
        this.webDriver.findElement(By.id(("submit"))).click();
    }

    @When("User receives login page response (.*)")
    public void thenReceiveLoginMessage(String expectedMessage) {
        String actualMessage = this.webDriver.findElement(By.id("message")).getText();
        Assert.assertEquals(expectedMessage, actualMessage);
        this.server.stop();
        this.webDriver.quit();
    }
}
