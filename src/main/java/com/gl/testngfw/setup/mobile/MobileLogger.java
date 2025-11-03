package com.gl.testngfw.setup.mobile;

import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.setup.BaseInitializer;
import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.setup.ResultContainer;
import com.gl.testngfw.setup.interfaces.LogManager;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MobileLogger implements LogManager {
    private static final Logger LOGGER = Logger.getLogger(MobileLogger.class.getName());
    private ResultContainer resultContainer = InitializerScript.getExecutionContainer().getLocalResultContainer();


    @Override
    public void initLogger() {
        try {
            resultContainer.initLogger(Platforms.MOBILE);
            BaseInitializer.initDeviceLogs();
        } catch (URISyntaxException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    @Override
    public void teardown() {
        InitializerScript.getNetworkLogs();
        InitializerScript.captureDeviceLogs();
    }
}

