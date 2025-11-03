package com.gl.testngfw.api;

import com.gl.testngfw.enums.ElementType;
import com.gl.testngfw.setup.InitializerScript;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

/**
 * Ui control class.
 */
public abstract class UiControl extends Container{
    protected AppiumDriver driver;
    protected By element;
    RemoteWebDriver webDriver;
    protected String elementIdentifier;
    String elementLabel;
    ElementType selectorType;

    public UiControl(String id, String elementLabel, ElementType type) {
        driver = InitializerScript.getDriver();
        webDriver = InitializerScript.getWebDriver();
        this.selectorType = type;
        this.elementIdentifier = id;
        this.elementLabel = elementLabel;

        switch (selectorType) {
            case BY_ID:
                element = By.id(elementIdentifier);
                break;
            case BY_NAME:
                element = By.name(elementIdentifier);
                break;
            case BY_XPATH:
                element = By.xpath(elementIdentifier);
                break;
            case BY_CLASS:
                element = By.className(elementIdentifier);
                break;
            case BY_TAG_NAME:
                element = By.tagName(elementIdentifier);
                break;
            case BY_LINK_TEXT:
                element = By.linkText(elementIdentifier);
                break;
            case BY_CSS_SELECTOR:
                element = By.cssSelector(elementIdentifier);
                break;
            case BY_PARTIAL_LINK_TEXT:
                element = By.partialLinkText(elementIdentifier);

                break;
            default:
                break;
        }
    }

/*
    public abstract void click();

    public abstract String getText();

    public abstract boolean isFound(Integer timeInSec);

    public abstract void waitForNotVisible(Integer timeInSec);

    public abstract String getAttribute(String key);

    public abstract boolean isEnabled();

    public abstract void await(long timeout) throws InterruptedException;

    public abstract void tap(WebElement element);

    public abstract void tap(int x, int y);

    public abstract List findElements();

    public abstract void clearTextField();

    public abstract void sendText(String text);

    public abstract void sendText(Keys text);

    public abstract boolean isSelected();

    public abstract boolean isSelected(String val);

    public abstract boolean isSelected(int val);

    public abstract Dimension getSize();*/

    public String getElementIdentifier() {
        return elementIdentifier;
    }

    public String getElementLabel() {
        return elementLabel;
    }

    public By getElement() {
        return element;
    }

}
