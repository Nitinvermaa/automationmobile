package com.gl.testngfw.setup.mobile;

import com.gl.testngfw.common.Constants;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.report.model.ConfigData;
import com.gl.testngfw.setup.ExecutionContainer;
import com.gl.testngfw.setup.HubUtil;
import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.setup.interfaces.DriverManager;
import com.gl.testngfw.utility.Util;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MobileDriver implements DriverManager {
    private static final Logger LOGGER = Logger.getLogger(MobileDriver.class.getName());
    private ConfigData configData = InitializerScript.getConfigData();
    private ExecutionContainer executionContainer = InitializerScript.getExecutionContainer();

    @Override
    public void initDriver() {
      boolean driverCreated=  setMobileAppiumDriver();
        executionContainer.getDriver().manage().timeouts().implicitlyWait(Constants.getImplicitTimeout(), TimeUnit.SECONDS);
        InitializerScript.setIsDriverCreated(driverCreated);
    }

    @Override
    public void teardown(int status) {
        InitializerScript.captureFailedTestScreenShot(status);
        InitializerScript.initResult(status);
        if (InitializerScript.getDriver() != null) {
            InitializerScript.getDriver().quit();
        }
        InitializerScript.reInitialiseAppiumServer();
    }

    /**
     * Method to start Appium driver
     *
     */
    private boolean setMobileAppiumDriver() {
        AppiumDriver appiumDriver;
        URL appiumURL = null;
        try {
            appiumURL = configData.isGafCloud() ? HubUtil.getHubURL() : getAppiumUrl();

        if (executionContainer.getExecutionPlatform().length() >= Constants.IOS_UDID_SIZE) {
            double osVersion = Double.parseDouble(TestExecutor.getDeviceInfoMap().get(executionContainer.getExecutionPlatform()).getDeviceVersion().substring(0, 3));
            LOGGER.log(Level.INFO, "IOS URL:: " + appiumURL);
            appiumDriver = new IOSDriver(appiumURL,
                    InitializerScript.getIOSCapabilities(executionContainer.getExecutionPlatform(), configData.getBundleID(), osVersion,
                            configData.getIsBrowserMobProxyRequired()));
            executionContainer.setIosDriver((IOSDriver) appiumDriver);
        } else {
            LOGGER.log(Level.INFO, " Device ID:: " + executionContainer.getExecutionPlatform()+" Driver URL:: " + appiumURL);
            appiumDriver = new AndroidDriver(appiumURL,
                    InitializerScript.getAndroidCapabilities(executionContainer.getExecutionPlatform(), configData.getActivityName(),
                            configData.getPackageName(), configData.getIsBrowserMobProxyRequired()));
            executionContainer.setAndroidDriver((AndroidDriver) appiumDriver);
        }
        executionContainer.setDriver(appiumDriver);
        return true;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING,e.getMessage(),e);
            return false;
        }

    }
    private URL getAppiumUrl() throws MalformedURLException {
        return new URL(String.format("http://%s:%s/wd/hub", Util.getIPAddress(), Constants.getMobilePorts().get(executionContainer.getExecutionPlatform())));
       // return new URL(String.format("http://%s:%s/wd/hub", Constants.getLocalIp(), Constants.getMobilePorts().get(executionContainer.getExecutionPlatform())));
      }

}

