package com.gl.testngfw.setup.stromtest;

import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.setup.ResultContainer;
import com.gl.testngfw.setup.interfaces.LogManager;
import io.cucumber.java.sl.In;

import java.util.logging.Logger;


public class StormTestLogger implements LogManager {
    private ResultContainer resultContainer = InitializerScript.getExecutionContainer().getLocalResultContainer();


    @Override
    public void initLogger() {
        resultContainer.initLogger(Platforms.STB);
        InitializerScript.getStormTestDriver().beginLogRegion(InitializerScript.getTestCase().getTestMethodName());
    }

    @Override
    public void teardown() {
        InitializerScript.getStormTestDriver().endLogRegion(InitializerScript.getTestCase().getTestMethodName());
    }
}

