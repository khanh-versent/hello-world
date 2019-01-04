package com.khanh.sample.cucumber.steps;

import com.khanh.sample.utils.SimpleHttpServer;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.util.List;

public class WebAddTradeSteps {
    private SimpleHttpServer server;
    private WebDriver webDriver;

    @When("Web server runs at (.*) and port (\\d+). User accesses add trade page at (.*)")
    public void givenUserAccessAddTradePage(String hostname, int port, String path) throws IOException {
        this.server = new SimpleHttpServer(hostname, port);

        this.webDriver = new ChromeDriver();
        this.webDriver.get(String.format("http://%s:%d%s", hostname, port, path));
    }

    @When("User enters (.*), \\+?(-?[\\d\\.]+), \\+?(-?[\\d\\.]+), (.*) and (.*) in add trade form")
    public void whenEnterUsernameAndPassword(String trade, float volume, float price, String type, String note) {
        Select select = new Select(this.webDriver.findElement(By.name("trade")));
        select.selectByVisibleText(trade);

        this.webDriver.findElement(By.name(("volume"))).sendKeys(String.valueOf(volume));
        this.webDriver.findElement(By.name(("price"))).sendKeys(String.valueOf(price));

        changeValue(this.webDriver.findElements(By.name("type")), type);

        this.webDriver.findElement(By.name(("note"))).sendKeys(note);

        this.webDriver.findElement(By.name(("submit"))).click();
    }

    @Then("User receives add trade response (.*)")
    public void thenReceiveAddTradeMessage(String expectedMessage) {
        String actualMessage = this.webDriver.findElement(By.id("message")).getText();
        Assert.assertEquals(expectedMessage, actualMessage);
        this.server.stop();
        this.webDriver.quit();
    }

    private void changeValue(List<WebElement> webElements, String newValue) {
        for (int i = 0; i < webElements.size(); i++) {
            String value = webElements.get(i).getAttribute("value");
            if (value.equals(newValue)) {
                webElements.get(i).click();
            }
        }
    }
}
