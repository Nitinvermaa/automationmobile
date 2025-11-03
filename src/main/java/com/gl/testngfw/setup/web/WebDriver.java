package com.gl.testngfw.setup.web;

import com.gl.testngfw.common.Constants;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.report.model.ConfigData;
import com.gl.testngfw.setup.ExecutionContainer;
import com.gl.testngfw.setup.HubUtil;
import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.setup.interfaces.DriverManager;
import com.gl.testngfw.utility.Util;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebDriver implements DriverManager {
    private static final Logger LOGGER = Logger.getLogger(WebDriver.class.getName());
    private ExecutionContainer executionContainer = InitializerScript.getExecutionContainer();

    @Override
    public void initDriver() {
      boolean driverCreated=  setWebDriver(executionContainer.getExecutionPlatform());
        InitializerScript.setIsDriverCreated(driverCreated);
    }

    @Override
    public void teardown(int status) {
        InitializerScript.captureFailedTestScreenShot(status);
        InitializerScript.initResult(status);
        if (InitializerScript.isDriverCreated()) {
            InitializerScript.getWebDriver().quit();
        }
    }

    /**
     * Method to start Web driver
     */
    private synchronized boolean setWebDriver(String browser) {
        try {
            RemoteWebDriver localDriver = null;
            URL hubURL = getUrl();
            LOGGER.log(Level.INFO, hubURL.toString());
            switch (Util.getBrowserName(browser)) {
                case CHROME:
                    localDriver = new RemoteWebDriver(hubURL, InitializerScript.getChromeBrowserCapabilities());
                    break;
                case FIREFOX:
                    localDriver = new RemoteWebDriver(hubURL, InitializerScript.getFirefoxBrowserCapabilities());
                    break;
                case SAFARI:
                    localDriver = new RemoteWebDriver(hubURL, InitializerScript.getSafariBrowserCapabilities());
                    break;
                case IE:
                    localDriver = new RemoteWebDriver(hubURL, InitializerScript.getIEBrowserCapabilities());
                    localDriver.manage().window().setSize(new Dimension(1024, 768));
                    break;
                case EDGE:
                    localDriver = new RemoteWebDriver(hubURL, InitializerScript.getEdgeBrowserCapabilities());
                    break;
                default:
                    FrameworkLogger.logWarning("Invalid Browser Type " + browser);
                    break;
            }
            executionContainer.setWebDriver(localDriver);
            localDriver.manage().window().maximize();
            return true;
        }catch (Exception e){
           LOGGER.log(Level.WARNING ,e.getMessage(),e);
           return false;
        }
    }

    @NotNull
    private URL getUrl() {
        try {
            return HubUtil.getHubURL() == null ? new URL(Constants.getGridHubUrl()) : HubUtil.getHubURL();
        } catch (MalformedURLException e) {
            LOGGER.log(Level.WARNING, "Invalid URl", e);
        }
        return null;
    }
}
