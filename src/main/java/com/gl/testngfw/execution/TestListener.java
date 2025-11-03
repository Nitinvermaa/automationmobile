package com.gl.testngfw.execution;

import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.setup.InitializerScript;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestListener implements ITestListener {
    private static final Logger LOGGER = Logger.getLogger(TestListener.class.getName());

    @Override
    public void onTestStart(ITestResult iTestResult) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new java.util.Date(iTestResult.getStartMillis()));
        InitializerScript.getLocalResult().setStartTime(currentDate);
    }


    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        InitializerScript.getLocalResult().setStatus("Pass");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new java.util.Date(iTestResult.getEndMillis()));
        InitializerScript.getLocalResult().setEndTime(String.valueOf(currentDate));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        String message = iTestResult.getThrowable().getMessage() == null ? iTestResult.getThrowable().toString() : iTestResult.getThrowable().getMessage();
        InitializerScript.getLocalResult().setErrorMessage(message);
        InitializerScript.getLocalResult().setStatus("Fail");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new java.util.Date(iTestResult.getEndMillis()));
        InitializerScript.getLocalResult().setEndTime(currentDate);
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new java.util.Date(iTestResult.getEndMillis()));
        InitializerScript.getLocalResult().setEndTime(currentDate);
        InitializerScript.getLocalResult().setStatus("Skip");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        LOGGER.log(Level.INFO, "***********Test failed*********");
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        LOGGER.log(Level.INFO, "***********Starting test*********");

    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        LOGGER.log(Level.INFO, "***********Inside On Listiner Finish*********");
    }
}
