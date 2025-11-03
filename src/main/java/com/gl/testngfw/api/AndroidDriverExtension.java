package com.gl.testngfw.api;

import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.setup.InitializerScript;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.Location;

import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.Set;

/**
 * Android androidDriver commands class.
 */
public interface AndroidDriverExtension {

    AndroidDriver androidDriver = (AndroidDriver) InitializerScript.getDriver();

    /**
     * Send a key event to the device
     *
     * @param key code for the key pressed on the device
     */
    default void sendKeyEvent(AndroidKey key) {
        androidDriver.pressKey(new KeyEvent(key));
    }

    /**
     * Get the current network settings of the device.
     *
     * @return NetworkConnectionSetting objects will let you inspect the status of AirplaneMode, Wifi, Data connections
     */
    default boolean isAirplaneMode() {
        return androidDriver.getConnection().isAirplaneModeEnabled();
    }

    default boolean isWiFiEnabled() {
        return androidDriver.getConnection().isWiFiEnabled();
    }

    default boolean isDataEnabled() {
        return androidDriver.getConnection().isDataEnabled();
    }

    /**
     * Set the network connection of the device. This is an Android-only method
     *
     * @param connection The NetworkConnectionSetting executableFor to use for the device
     */
    default void setNetworkConnection(ConnectionState connection) {
        androidDriver.setConnection(connection);
    }


    /**
     * Save base64 encoded data as a file on the remote mobile device.
     *
     * @param remotePath Path to file to write data to on remote device
     * @param base64Data Base64 encoded byte array of data to write to remote device
     */
    default void pushFile(String remotePath, byte[] base64Data) {
        androidDriver.pushFile(remotePath, base64Data);
    }

    /**
     * This method should start arbitrary activity during a test. If the activity belongs to another application,
     * that application is started and the activity is opened.
     *
     * @param appPackage  The package containing the activity. [Required]
     * @param appActivity The activity to start. [Required]
     * @throws IllegalArgumentException
     */
    default void startActivity(String appPackage, String appActivity) throws IllegalArgumentException {
        Activity activity = new Activity(appPackage, appActivity);
        androidDriver.startActivity(activity);
    }

    /**
     * Get the current activity being run on the mobile device
     *
     * @return current activity
     */
    default String getCurrentActivity() {
        return androidDriver.currentActivity();
    }

    /**
     * Open the notification shade, on Android devices.
     */
    default void openNotifications() {
        androidDriver.openNotifications();
    }

    /**
     * Check if the device is locked.
     *
     * @return true if device is locked. False otherwise
     */
    default boolean isLocked() {
        return androidDriver.isDeviceLocked();
    }

    default void ignoreUnimportantViews(Boolean compress) {
        androidDriver.ignoreUnimportantViews(compress);
    }

    default void enableAirplaneMode() {
        FrameworkLogger.logStep("Turn on airplane mode of device");
        ConnectionState connectionState = new ConnectionState(ConnectionState.AIRPLANE_MODE_MASK);
        if (!connectionState.isWiFiEnabled()) {
            androidDriver.setConnection(connectionState);
        }
    }

    default void disableAirplaneMode() {
        FrameworkLogger.logStep("Turn Off airplane mode of device");
        ConnectionState connectionState = new ConnectionState(ConnectionState.AIRPLANE_MODE_MASK);
        if (connectionState.isWiFiEnabled()) {
            androidDriver.setConnection(connectionState);
        }
    }

    default void enableWifiMode() {
        FrameworkLogger.logStep("Turn On Wifi mode of device");
        ConnectionState connectionState = new ConnectionState(ConnectionState.WIFI_MASK);
        if (!connectionState.isWiFiEnabled()) {
            androidDriver.setConnection(connectionState);
        }
    }

    default void disableWifiMode() {
        FrameworkLogger.logStep("Turn Off Wifi mode of device");
        ConnectionState connectionState = new ConnectionState(ConnectionState.WIFI_MASK);
        if (connectionState.isWiFiEnabled()) {
            androidDriver.setConnection(connectionState);
        }
    }

    default void enableDataMode() {
        FrameworkLogger.logStep("Turn On Data mode of device");
        ConnectionState connectionState = new ConnectionState(ConnectionState.DATA_MASK);
        if (connectionState.isWiFiEnabled()) {
            androidDriver.setConnection(connectionState);
        }
    }

    default void disableDataMode() {
        FrameworkLogger.logStep("Turn Off Data mode of device");
        ConnectionState connectionState = new ConnectionState(ConnectionState.DATA_MASK);
        if (!connectionState.isWiFiEnabled()) {
            androidDriver.setConnection(connectionState);
        }
    }

    default void enableNetwork(boolean isDataDisabled, boolean isWifiDisabled) {
        if (isDataDisabled) {
            enableDataMode();
        } else if (isWifiDisabled) {
            enableWifiMode();
        }
    }

    default void back() {
        androidDriver.navigate().back();
    }

    default void resetApp() {
        FrameworkLogger.logStep("Resetting Application");
        androidDriver.resetApp();
    }

    default boolean isAppInstalled(String bundleId) {
        FrameworkLogger.logStep("Check if Application is installed");
        return androidDriver.isAppInstalled(bundleId);
    }

    default void installApp(String appPath) {
        FrameworkLogger.logStep("Install Application");
        androidDriver.installApp(appPath);
    }

    default void removeApp(String bundleId) {
        FrameworkLogger.logStep("Removing Application");
        androidDriver.removeApp(bundleId);
    }

    default void launchApp() {
        FrameworkLogger.logStep("Launch Application");
        androidDriver.launchApp();
    }

    default void closeApp() {
        FrameworkLogger.logStep("Close Application");
        androidDriver.closeApp();
    }

    default void runAppInBackground(int seconds) {
        FrameworkLogger.logStep("Run Application in background");
        androidDriver.runAppInBackground(Duration.ofSeconds(seconds));
    }

    default void hideKeyboard() {
        FrameworkLogger.logStep("Hide KeyBoard");
        androidDriver.hideKeyboard();
    }

    default byte[] pullFile(String remotePath) {
        FrameworkLogger.logStep("Pull File from " + remotePath);
        return androidDriver.pullFile(remotePath);
    }

    default byte[] pullFolder(String remotePath) {
        FrameworkLogger.logStep("Pull Folder from " + remotePath);
        return androidDriver.pullFolder(remotePath);
    }

    default TouchAction performTouchAction(TouchAction touchAction) {
        FrameworkLogger.logStep("Performing Touch action");
        return androidDriver.performTouchAction(touchAction);
    }

    default void performMultiTouchAction(MultiTouchAction multiAction) {
        FrameworkLogger.logStep("Performing MultiTouch action");
        androidDriver.performMultiTouchAction(multiAction);
    }

    default void swipe(int startx, int starty, int endx, int endy) {
        FrameworkLogger.logStep(String.format("Swipe screen from  %s %s to %s %s in seconds", startx, starty, endx, endy));
        TouchAction touchAction = new TouchAction(androidDriver);
        touchAction.press(PointOption.point(startx, starty)).moveTo(PointOption.point(endx, endy)).release().perform();
    }

    default void pinch(WebElement el) {
        FrameworkLogger.logStep(String.format("Perform Pinch action on %s ", el));
        MultiTouchAction multiTouch = new MultiTouchAction(androidDriver);
        TouchAction tAction0 = new TouchAction(androidDriver);
        TouchAction tAction1 = new TouchAction(androidDriver);
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
        MultiTouchAction multiTouch = new MultiTouchAction(androidDriver);
        TouchAction tAction0 = new TouchAction(androidDriver);
        TouchAction tAction1 = new TouchAction(androidDriver);
        int scrHeight = androidDriver.manage().window().getSize().getHeight();
        int scrWidth = androidDriver.manage().window().getSize().getWidth();
        //press finger center of the screen and then move y axis
        tAction0.press(PointOption.point(10, 10)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1000))).moveTo(PointOption.point(-scrWidth / 2, -scrHeight / 2)).release();
        // press thumb slightly down on the center of the screen and then move y axis
        tAction1.press(PointOption.point(scrWidth, scrHeight)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1000))).moveTo(PointOption.point(-scrWidth / 2, -scrHeight / 2)).release();
        multiTouch.add(tAction0).add(tAction1);
        multiTouch.perform();
    }

    default void zoom() {
        FrameworkLogger.logStep("Perform zoom action on screen");
        MultiTouchAction multiTouch = new MultiTouchAction(androidDriver);
        TouchAction tAction0 = new TouchAction(androidDriver);
        TouchAction tAction1 = new TouchAction(androidDriver);
        int scrHeight = androidDriver.manage().window().getSize().getHeight();
        int scrWidth = androidDriver.manage().window().getSize().getWidth();
        //press finger center of the screen and then move y axis
        tAction0.press(PointOption.point(scrWidth / 4, scrHeight / 4)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1000))).moveTo(PointOption.point(scrWidth / 4, 60)).release();
        // press thumb slightly down on the center of the screen and then move y axis
        tAction1.press(PointOption.point(scrWidth / 4, (scrHeight / 4) + 40)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1000))).moveTo(PointOption.point(scrWidth / 4, 80)).release();
        multiTouch.add(tAction0).add(tAction1);
        multiTouch.perform();
    }

    default Map<String, Object> getSettings() {
        FrameworkLogger.logStep("Get Appium settings");
        return androidDriver.getSettings();
    }

    default WebDriver context(String name) {
        FrameworkLogger.logStep(String.format("Switch androidDriver context to %s", name));
        return androidDriver.context(name);
    }

    default Set getContextHandles() {
        return androidDriver.getContextHandles();
    }

    default String getContext() {
        FrameworkLogger.logStep("Getting androidDriver context");
        return androidDriver.getContext();
    }

    default Location location() {
        FrameworkLogger.logStep("Get device location");
        return androidDriver.location();
    }

    default void setLocation(Location location) {
        FrameworkLogger.logStep("Set device location");
        androidDriver.setLocation(location);
    }

    default Map getAppStrings() {
        FrameworkLogger.logStep("Getting application strings");
        return androidDriver.getAppStringMap();
    }

    default Map getAppStrings(String language) {
        FrameworkLogger.logStep(String.format("Getting application strings for %s language", language));
        return androidDriver.getAppStringMap(language);
    }

    default URL getRemoteAddress() {
        return androidDriver.getRemoteAddress();
    }

    default void longClick(UiControl uiControl) {
        FrameworkLogger.logStep(String.format("Long Click on %s (%s)", uiControl.getElementLabel(), uiControl.getElement()));
        TouchAction touchAction = new TouchAction(androidDriver);
        touchAction.longPress(PointOption.point(androidDriver.findElement(uiControl.getElement()).getLocation()));
        touchAction.perform();
    }
}
