package com.gl.testngfw.execution;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

class RetryAnalyzer implements IRetryAnalyzer {
    private int counter = 0;
    private int retryLimit = TestExecutor.getConfigData().getRetryCount();

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (counter < retryLimit) {
            counter++;
            return true;
        }
        return false;
    }
}
