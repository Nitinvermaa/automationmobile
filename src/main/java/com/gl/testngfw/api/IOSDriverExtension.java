package com.gl.testngfw.api;


import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.setup.InitializerScript;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.Location;

import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.Set;

/**
 * IOS driver commands class.
 */
public interface IOSDriverExtension {

    IOSDriver iosDriver = (IOSDriver) InitializerScript.getDriver();


    /**
     * Hides the keyboard if it is showing.
     */
    default void hideKeyboard() {
        FrameworkLogger.logStep("hide KeyBoard");
        iosDriver.findElement(By.xpath("//UIAKeyboard[1]/UIAButton[4]")).click();
    }

    default void resetApp() {
        FrameworkLogger.logStep("Resetting Application");
        iosDriver.resetApp();
    }

    default boolean isAppInstalled(String bundleId) {
        FrameworkLogger.logStep("Check if Application is installed");
        return iosDriver.isAppInstalled(bundleId);
    }

    default void installApp(String appPath) {
        FrameworkLogger.logStep("Install Application");
        iosDriver.installApp(appPath);
    }

    default void removeApp(String bundleId) {
        FrameworkLogger.logStep("Removing Application");
        iosDriver.removeApp(bundleId);
    }

    default void launchApp() {
        FrameworkLogger.logStep("Launch Application");
        iosDriver.launchApp();
    }

    default void closeApp() {
        FrameworkLogger.logStep("Close Application");
        iosDriver.closeApp();
    }

    default void runAppInBackground(int seconds) {
        FrameworkLogger.logStep("Run Application in background for " + seconds);
        iosDriver.runAppInBackground(Duration.ofSeconds(seconds));
    }

    default byte[] pullFile(String remotePath) {
        FrameworkLogger.logStep("Pull File from " + remotePath);
        return iosDriver.pullFile(remotePath);
    }

    default byte[] pullFolder(String remotePath) {
        FrameworkLogger.logStep("Pull Folder from " + remotePath);
        return iosDriver.pullFolder(remotePath);
    }

    default TouchAction performTouchAction(TouchAction touchAction) {
        FrameworkLogger.logStep("Performing Touch action");
        return iosDriver.performTouchAction(touchAction);
    }

    default void performMultiTouchAction(MultiTouchAction multiAction) {
        FrameworkLogger.logStep("Performing MultiTouch action");
        iosDriver.performMultiTouchAction(multiAction);
    }

    default void swipe(int startx, int starty, int endx, int endy) {
        FrameworkLogger.logStep(String.format("Swipe screen from  %s %s to %s %s", startx, starty, endx, endy));
        new TouchAction(iosDriver).press(PointOption.point(startx, starty)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).
                moveTo(PointOption.point(endx - startx, endy - starty)).release().perform();
    }

    default void pinch(WebElement el) {
        FrameworkLogger.logStep(String.format("Perform Pinch action on %s ", el));
        FrameworkLogger.logStep(String.format("Perform Pinch action on %s ", el));
        MultiTouchAction multiTouch = new MultiTouchAction(iosDriver);
        TouchAction tAction0 = new TouchAction(iosDriver);
        TouchAction tAction1 = new TouchAction(iosDriver);
        int scrHeight = el.getRect().getHeight();
        int scrWidth = el.getSize().getWidth();
        //press finger center of the screen and then move y axis
        tAction0.press(PointOption.point(el.getRect().getX(), el.getRect().getY())).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1000))).moveTo(PointOption.point(-scrWidth / 2, -scrHeight / 2)).release();
        // press thumb slightly down on the center of the screen and then move y axis
        tAction1.press(PointOption.point(-el.getRect().getX() + scrWidth, -el.getRect().getY() + scrHeight)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1000))).moveTo(PointOption.point(scrWidth, scrHeight)).release();
        multiTouch.add(tAction0).add(tAction1);
        multiTouch.perform();
    }

    default void pinch() {
        FrameworkLogger.logStep("Perform Pinch action on screen");
        MultiTouchAction multiTouch = new MultiTouchAction(iosDriver);
        TouchAction tAction0 = new TouchAction(iosDriver);
        TouchAction tAction1 = new TouchAction(iosDriver);
        int scrHeight = iosDriver.manage().window().getSize().getHeight();
        int scrWidth = iosDriver.manage().window().getSize().getWidth();
        //press finger center of the screen and then move y axis
        tAction0.press(PointOption.point(10, 10)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1000))).moveTo(PointOption.point(-scrWidth / 2, -scrHeight / 2)).release();
        // press thumb slightly down on the center of the screen and then move y axis
        tAction1.press(PointOption.point(scrWidth, scrHeight)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1000))).moveTo(PointOption.point(-scrWidth / 2, -scrHeight / 2)).release();
        multiTouch.add(tAction0).add(tAction1);
        multiTouch.perform();
    }

    default void zoom() {
        FrameworkLogger.logStep("Perform zoom action on screen");
        MultiTouchAction multiTouch = new MultiTouchAction(iosDriver);
        TouchAction tAction0 = new TouchAction(iosDriver);
        TouchAction tAction1 = new TouchAction(iosDriver);
        int scrHeight = iosDriver.manage().window().getSize().getHeight();
        int scrWidth = iosDriver.manage().window().getSize().getWidth();
        //press finger center of the screen and then move y axis
        tAction0.press(PointOption.point(scrWidth / 4, scrHeight / 4)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1000))).moveTo(PointOption.point(scrWidth / 4, 60)).release();
        // press thumb slightly down on the center of the screen and then move y axis
        tAction1.press(PointOption.point(scrWidth / 4, (scrHeight / 4) + 40)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1000))).moveTo(PointOption.point(scrWidth / 4, 80)).release();
        multiTouch.add(tAction0).add(tAction1);
        multiTouch.perform();
    }

    default Map<String, Object> getSettings() {
        FrameworkLogger.logStep("Get Appium settings");
        return iosDriver.getSettings();
    }

    default WebDriver context(String name) {
        FrameworkLogger.logStep(String.format("Switch iosDriver context to %s", name));
        return iosDriver.context(name);
    }

    default Set getContextHandles() {
        return iosDriver.getContextHandles();
    }

    default String getContext() {
        FrameworkLogger.logStep("Getting iosDriver context");
        return iosDriver.getContext();
    }

    default Location location() {
        FrameworkLogger.logStep("Get device location");
        return iosDriver.location();
    }

    default void setLocation(Location location) {
        FrameworkLogger.logStep("Set device location");
        iosDriver.setLocation(location);
    }

    default Map getAppStrings() {
        FrameworkLogger.logStep("Getting application strings");
        return iosDriver.getAppStringMap();
    }

    default Map getAppStrings(String language) {
        FrameworkLogger.logStep(String.format("Getting application strings for %s language", language));
        return iosDriver.getAppStringMap(language);
    }

    default URL getRemoteAddress() {
        return iosDriver.getRemoteAddress();
    }
}
