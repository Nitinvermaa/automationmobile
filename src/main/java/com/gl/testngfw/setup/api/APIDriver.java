package com.gl.testngfw.setup.api;

import com.gl.testngfw.api.RestAPIExtension;
import com.gl.testngfw.report.model.ConfigData;
import com.gl.testngfw.setup.ExecutionContainer;
import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.setup.interfaces.DriverManager;

import java.util.logging.Logger;


public class APIDriver implements DriverManager {
    private static final Logger LOGGER = Logger.getLogger(APIDriver.class.getName());
    private ConfigData configData = InitializerScript.getConfigData();
    private ExecutionContainer executionContainer = InitializerScript.getExecutionContainer();

    @Override
    public void initDriver() {
        RestAPIExtension restAPIExtension = new RestAPIExtension(false);
        executionContainer.setApiDriver(restAPIExtension);
        InitializerScript.setIsDriverCreated(true);
    }

    @Override
    public void teardown(int status) {
        InitializerScript.initResult(status);
    }
}

