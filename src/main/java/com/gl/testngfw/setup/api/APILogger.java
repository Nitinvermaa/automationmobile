package com.gl.testngfw.setup.api;

import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.setup.ResultContainer;
import com.gl.testngfw.setup.interfaces.LogManager;

import java.util.logging.Logger;


public class APILogger implements LogManager {
    private static final Logger LOGGER = Logger.getLogger(APILogger.class.getName());
    private ResultContainer resultContainer = InitializerScript.getExecutionContainer().getLocalResultContainer();


    @Override
    public void initLogger() {
        resultContainer.initLogger(Platforms.API);
        FrameworkLogger.setEnableAPIFileLogging(true);
    }

    @Override
    public void teardown() {
        FrameworkLogger.setEnableAPIFileLogging(false);
    }
}

