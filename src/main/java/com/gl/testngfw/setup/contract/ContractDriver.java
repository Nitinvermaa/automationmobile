package com.gl.testngfw.setup.contract;

import com.gl.testngfw.api.ConsumerClient;
import com.gl.testngfw.setup.ExecutionContainer;
import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.setup.JunitInitializer;
import com.gl.testngfw.setup.interfaces.DriverManager;
import org.apache.http.client.fluent.Executor;

import java.text.SimpleDateFormat;


public class ContractDriver implements DriverManager {
    private ExecutionContainer executionContainer = InitializerScript.getExecutionContainer();

    @Override
    public void initDriver() {
        executionContainer.setContractDriver(new ConsumerClient());
        JunitInitializer.setIsDriverCreated(true);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new java.util.Date(System.currentTimeMillis()));
        executionContainer.getLocalResult().setStartTime(currentDate);
    }

    @Override
    public void teardown(int status) {
        InitializerScript.initResult(status);
        Executor.closeIdleConnections();
    }
}

