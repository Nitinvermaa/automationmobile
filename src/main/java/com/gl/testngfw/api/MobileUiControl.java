package com.gl.testngfw.api;

import com.gl.testngfw.enums.ElementType;
import com.gl.testngfw.logging.FrameworkLogger;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;


public class MobileUiControl extends UiControl {
    MobileUiControl(String id, String elementLabel, ElementType type) {
        super(id, elementLabel, type);
    }

    public void click() {
        FrameworkLogger.logStep(String.format("Clicking on %s (%s)", elementLabel, element));
        if (selectorType.equals(ElementType.BY_NAME)) {
            driver.findElementByAccessibilityId(elementIdentifier).click();
        } else {
            driver.findElement(element).click();
        }
    }

    public String getText() {
        FrameworkLogger.logStep(String.format("Getting text from %s (%s)", elementLabel, element));
        if (selectorType.equals(ElementType.BY_NAME)) {
            return driver.findElementByAccessibilityId(elementIdentifier).getText();
        } else {
            return driver.findElement(element).getText();
        }
    }

    public boolean isFound(Integer timeInSec) {
        FrameworkLogger.logStep(String.format("Checking if %s (%s) is found", elementLabel, element));
        boolean isElementPresent = false;
        Long timeOut = Long.valueOf(timeInSec);
        WebDriverWait driverWait = new WebDriverWait(driver, timeOut);
        try {
            driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(element));
            isElementPresent = true;
        } catch (WebDriverException ignored) {
            FrameworkLogger.logWarning(String.format("%s (%s) NOT FOUND !!", elementLabel, element));
        }
        return isElementPresent;
    }

    public void waitForNotVisible(Integer timeInSec) {
        FrameworkLogger.logStep(String.format("Checking if %s (%s) is not visible", elementLabel, element));
        Long timeOut = Long.valueOf(timeInSec);
        WebDriverWait driverWait = new WebDriverWait(driver, timeOut);
        try {
            driverWait.until(ExpectedConditions.invisibilityOfElementLocated(element));
        } catch (WebDriverException ignored) {
        }
    }

    public String getAttribute(String key) {
        FrameworkLogger.logWarning(String.format("Getting property %s from element %s (%s)", key,
                elementLabel, element));
        if (selectorType.equals(ElementType.BY_NAME)) {
            return driver.findElementByAccessibilityId(elementIdentifier).getAttribute(key);
        } else {
            return driver.findElement(element).getAttribute(key);
        }
    }

    public boolean isEnabled() {
        FrameworkLogger.logWarning(String.format("Checking if element %s (%s) is enabled", elementLabel, element));
        if (selectorType.equals(ElementType.BY_NAME)) {
            return driver.findElementByAccessibilityId(elementIdentifier).isEnabled();
        } else {
            return driver.findElement(element).isEnabled();
        }
    }

    public void await(long timeout) throws InterruptedException {
        FrameworkLogger.logWarning(String.format("Waiting for %s (%s)", elementLabel, element));
        driver.wait(timeout);

    }

    public void tap(WebElement element) {
        FrameworkLogger.logStep(String.format("Tapping on %s", element));
        TouchAction touchAction = new TouchAction(driver);
        touchAction.tap(new TapOptions().withElement(ElementOption.element(element)));
        touchAction.perform();
    }

    public void tap(int x, int y) {
        FrameworkLogger.logStep(String.format("Tapping on %s %s", x, y));
        TouchAction touchAction = new TouchAction(driver);
        touchAction.tap(PointOption.point(x, y));
        touchAction.perform();
    }

    public List<WebElement> findElements() {
        FrameworkLogger.logStep(String.format("Finding elements %s %s", elementLabel, element));
        List list;
        List<WebElement> webElements = new ArrayList<>();
        if (selectorType.equals(ElementType.BY_NAME)) {
            list = driver.findElementsByAccessibilityId(elementIdentifier);
        } else {
            list = driver.findElements(element);
        }
        for (Object element : list) {
            webElements.add((WebElement) element);
        }
        return webElements;
    }

    public void clearTextField() {
        FrameworkLogger.logStep(String.format("Clear text filed %s %s", elementLabel, element));
        if (selectorType.equals(ElementType.BY_NAME)) {
            driver.findElementByAccessibilityId(elementIdentifier).clear();
        } else {
            driver.findElement(element).clear();
        }
    }

   // @Override
    public void sendText(String text) {
        FrameworkLogger.logStep(String.format("Send %s to text filed %s %s", text, elementLabel, element));
        if (selectorType.equals(ElementType.BY_NAME)) {
            driver.findElementByAccessibilityId(elementIdentifier).clear();
            driver.findElement(element).sendKeys(text);
        } else {
            driver.findElement(element).sendKeys(text);
        }
    }

   // @Override
    public void sendText(Keys text) {
        FrameworkLogger.logStep(String.format("Send %s to text filed %s %s", text, elementLabel, element));
        if (selectorType.equals(ElementType.BY_NAME)) {
            driver.findElementByAccessibilityId(elementIdentifier).clear();
            driver.findElement(element).sendKeys(text);
        } else {
            driver.findElement(element).sendKeys(text);
        }
    }

    //@Override
    public boolean isSelected() {
        return false;
    }

    //@Override
    public boolean isSelected(String val) {
        return false;
    }

   // @Override
    public boolean isSelected(int val) {
        return false;
    }

    //Get sizes height and width of any elements on screen
    //@Override
    public Dimension getSize() {
        FrameworkLogger.logStep(String.format("Getting size %s", elementLabel));
        return driver.findElement(element).getSize();
    }
}
