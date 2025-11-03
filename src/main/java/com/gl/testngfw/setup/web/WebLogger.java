package com.gl.testngfw.setup.web;

import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.setup.ResultContainer;
import com.gl.testngfw.setup.interfaces.LogManager;

import java.util.logging.Logger;


public class WebLogger implements LogManager {
    private static final Logger LOGGER = Logger.getLogger(WebLogger.class.getName());
    private ResultContainer resultContainer = InitializerScript.getExecutionContainer().getLocalResultContainer();


    @Override
    public void initLogger() {
        resultContainer.initLogger(Platforms.WEB);
        //get browser logs
        if (InitializerScript.getConfigData().getCaptureDeviceLogs()) {
            InitializerScript.getWebDriver().manage().logs().get(org.openqa.selenium.logging.LogType.BROWSER);
        }
    }

    @Override
    public void teardown() {
        InitializerScript.getNetworkLogs();
        InitializerScript.captureBrowserLogs();
    }
}

