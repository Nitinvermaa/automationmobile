package com.gl.testngfw.api;

import com.gl.testngfw.common.Constants;
import com.gl.testngfw.enums.ElementType;
import com.gl.testngfw.logging.FrameworkLogger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class WebUiControl extends UiControl{
    private int timeInSec = 5;

    WebUiControl(String id, String elementLabel, ElementType type) {
        super(id, elementLabel, type);
        ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return "complete".equals(((JavascriptExecutor) driver).executeScript("return document.readyState"));
            }
        };
        WebDriverWait wait = new WebDriverWait(webDriver, Constants.getImplicitTimeout());
        wait.until(pageLoadCondition);
    }

    public void click() {
        isFound(timeInSec);
        FrameworkLogger.logStep(String.format("Clicking on %s (%s)", elementLabel, element));
        webDriver.findElement(element).click();
    }

    public String getText() {
        isFound(timeInSec);
        FrameworkLogger.logStep(String.format("Getting text %s (%s)", elementLabel, element));
        return webDriver.findElement(element).getText();
    }

    public Dimension getSize() {
        isFound(timeInSec);
        FrameworkLogger.logStep(String.format("Getting size of %s (%s)", elementLabel, element));
        return webDriver.findElement(element).getSize();
    }

    public boolean isFound(Integer timeInSec) {
        FrameworkLogger.logStep(String.format("Checking if %s (%s) is found", elementLabel, element));
        Long timeOut = Long.valueOf(timeInSec);
        WebElement ele1;
        try {
            FluentWait<WebDriver> wait = new FluentWait<WebDriver>(webDriver)
                    .withTimeout(Duration.ofSeconds(timeOut))
                    .pollingEvery(Duration.ofMillis(500L))
                    .ignoring(NoSuchElementException.class);
            ele1 = wait.until(driver -> webDriver.findElement(element));
            return ele1 != null;
        } catch (Exception e) {
            FrameworkLogger.logError(e);
            return false;
        }
    }

    public void waitForNotVisible(Integer timeInSec) {
        FrameworkLogger.logStep(String.format("Checking if %s (%s) is not visible", elementLabel, element));
        Long timeOut = Long.valueOf(timeInSec);
        WebDriverWait driverWait = new WebDriverWait(driver, timeOut);
        try {
            driverWait.until(ExpectedConditions.invisibilityOfAllElements(webDriver.findElement(element)));
        } catch (WebDriverException ignored) {
        }
    }

    public boolean isEnabled() {
        FrameworkLogger.logStep(String.format("Checking if element %s (%s) is enabled", elementLabel, element));
        return webDriver.findElement(element).isEnabled();
    }

    public boolean isSelected() {
        FrameworkLogger.logStep(String.format("Checking if element %s (%s) is selected", elementLabel, element));
        return webDriver.findElement(element).isSelected();
    }

    public boolean isSelected(String value) {
        FrameworkLogger.logStep(String.format("Select element %s (%s)", elementLabel, element));
        try {
            WebElement selectedElement = webDriver.findElement(element);
            Select select = new Select(selectedElement);
            select.selectByValue(value);
            return true;
        } catch (Exception e) {
            FrameworkLogger.logError(e);
            return false;
        }
    }

    public boolean isSelected(int index) {
        FrameworkLogger.logStep(String.format("Select element %s (%s)", elementLabel, element));
        try {
            WebElement selectedElement = webDriver.findElement(element);
            Select select = new Select(selectedElement);
            select.selectByIndex(index);
            return true;

        } catch (Exception e) {
            FrameworkLogger.logError(e);
            return false;
        }
    }

    public String getAttribute(String key) {
        return null;
    }

    public void await(long timeout) throws InterruptedException {
        webDriver.wait(timeout);
    }


    public void tap(WebElement element) {
        throw new UnsupportedOperationException();
    }

    public void tap(int x, int y) {
        throw new UnsupportedOperationException();
    }

    public List<WebElement> findElements() {
        FrameworkLogger.logStep(String.format("Finding elements %s (%s)", elementLabel, element));
        return webDriver.findElements(element);
    }

    public void clearTextField() {
        FrameworkLogger.logStep(String.format("Clear text filed %s (%s)", elementLabel, element));
        webDriver.findElement(element).clear();
    }

    public void sendText(String text) {
        FrameworkLogger.logStep(String.format("Send %s to text filed %s (%s)", text, elementLabel, element));
        webDriver.findElement(element).sendKeys(text);
    }

    public void sendText(Keys text) {
        throw new UnsupportedOperationException();
    }
}