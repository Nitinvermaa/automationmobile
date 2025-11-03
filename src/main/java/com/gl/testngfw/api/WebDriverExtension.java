package com.gl.testngfw.api;

import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.setup.InitializerScript;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;

/**
 *
 */
public interface WebDriverExtension {
    WebDriver webDriver = (WebDriver) InitializerScript.getWebDriver();
    Actions action = new Actions(webDriver);

    /**
     * Hover mouse on element
     *
     * @param control WebElement
     */
    default void mouseHover(WebUiControl control) {
        WebElement webElement = (WebElement) control.findElements().get(0);
        FrameworkLogger.logStep("Mouse Hovering on Element" + webElement);
        action.moveToElement(webElement).build().perform();
    }

    /**
     * Hover mouse on MainMenu and Select SubMenu
     *
     * @param mainMenu WebElement
     * @param subMenu  WebElement
     */

    default void mouseHoverAndSelect(WebUiControl mainMenu, WebUiControl subMenu) throws InterruptedException {
        WebElement webElement1 = (WebElement) mainMenu.findElements().get(0);
        action.moveToElement(webElement1);
        Thread.sleep(8000);
        WebElement webElement2 = (WebElement) subMenu.findElements().get(0);
        action.moveToElement(webElement2).click().build().perform();
    }


    /**
     * Switch to frame using webElement
     *
     * @param control WebElement
     */

    default void switchToFrame(WebUiControl control) {
        WebElement webElement = (WebElement) control.findElements().get(0);
        webDriver.switchTo().frame(webElement);
    }

    /**
     * Switch to frame using frameName
     *
     * @param frameName of WebElement
     */
    default void switchToFrame(String frameName) {

        webDriver.switchTo().frame(frameName);
    }

    /**
     * Switch to frame using Index
     *
     * @param index of frame
     */
    default void switchToFrame(Integer index) {

        webDriver.switchTo().frame(index);
    }

    /**
     * Switch to Parent Frame
     */
    default void switchToParentFrame() {
        webDriver.switchTo().parentFrame();
    }


    /**
     * Hover mouse on MainMenu and Select SubMenu
     *
     * @param dragElementFrom WebElement
     * @param dropElementTo   WebElement
     */

    default void dragAndDrop(WebUiControl dragElementFrom, WebUiControl dropElementTo) {
        WebElement webElement1 = (WebElement) dragElementFrom.findElements().get(0);
        WebElement webElement2 = (WebElement) dropElementTo.findElements().get(0);
        Action dragAndDrop = action.clickAndHold(webElement1)
                .moveToElement(webElement2)
                .release(webElement2)
                .build();
        dragAndDrop.perform();
    }

    /**
     * Switch to Default Content
     */
    default void switchToDefaultContent() {

        webDriver.switchTo().defaultContent();
    }

    /**
     * Switch to Windows
     *
     * @param index of windows
     */
    default void switchToWindows(Integer index) {

        ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
        FrameworkLogger.logStep("No Of tabs::: " + tabs.size());
        webDriver.switchTo().window(tabs.get(index));
    }

    /**
     * Select Drop Down
     *
     * @param control WebElement
     * @param index   of WebElement to be selected
     */

    default void selectDropDown(WebUiControl control, Integer index) {
        WebElement webElement = (WebElement) control.findElements().get(0);
        Select dropDown = new Select(webElement);
        dropDown.selectByIndex(index);
    }

    /**
     * Select Drop Down
     *
     * @param control    WebElement
     * @param optionName of WebElement to be selected
     */

    default void selectDropDown(WebUiControl control, String optionName) {
        WebElement webElement = (WebElement) control.findElements().get(0);
        Select dropDown = new Select(webElement);
        dropDown.selectByVisibleText(optionName);
    }


    /**
     * Get Page Source
     */
    default String getPageSource() {
        return webDriver.getPageSource();
    }

    /**
     * Get Current URL
     */
    default String getCurrentURL() {
        return webDriver.getCurrentUrl();
    }

    /**
     * Get Window Size
     */
    default Dimension getWindowSize() {
        return webDriver.manage().window().getSize();

    }

    /**
     * Navigate to URL
     */
    default void navigateURL(String url) {
        webDriver.navigate().to(url);
    }

    /**
     * Add Cookie
     *
     * @param cookie name
     */
    default void addCookie(Cookie cookie) {
        webDriver.manage().addCookie(cookie);
    }

    /**
     * Delete Cookie
     *
     * @param cookie name
     */
    default void deleteCookie(Cookie cookie) {
        webDriver.manage().deleteCookie(cookie);
    }

    /**
     * Delete Cookie
     */
    default void deleteAllCookie() {
        webDriver.manage().deleteAllCookies();
    }
}
