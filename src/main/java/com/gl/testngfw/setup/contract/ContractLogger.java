package com.gl.testngfw.setup.contract;

import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.setup.ResultContainer;
import com.gl.testngfw.setup.interfaces.LogManager;

import java.util.logging.Logger;


public class ContractLogger implements LogManager {
    private static final Logger LOGGER = Logger.getLogger(ContractLogger.class.getName());
    private ResultContainer resultContainer = InitializerScript.getExecutionContainer().getLocalResultContainer();


    @Override
    public void initLogger() {
        System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");
        resultContainer.initLogger(Platforms.CONTRACT);
        FrameworkLogger.setEnableAPIFileLogging(true);
    }

    @Override
    public void teardown() {
        FrameworkLogger.setEnableAPIFileLogging(false);
    }
}

