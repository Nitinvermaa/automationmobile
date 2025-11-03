package com.gl.testngfw.setup.stromtest;

import com.gl.testngfw.api.StormTest;
import com.gl.testngfw.setup.ExecutionContainer;
import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.setup.interfaces.DriverManager;

public class StormTestDriver implements DriverManager {
    private ExecutionContainer executionContainer = InitializerScript.getExecutionContainer();

    @Override
    public void initDriver() {
        StormTest stormTest = new StormTest();
        executionContainer.setStormTestDriver(stormTest);
        InitializerScript.setIsDriverCreated(true);
    }

    @Override
    public void teardown(int status) {
        InitializerScript.initResult(status);
    }
}
