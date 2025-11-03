package com.gl.testngfw.execution;

import com.gl.testngfw.common.DBUpdater;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.setup.JunitInitializer;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JunitListener extends RunListener {
    private static final Logger LOGGER = Logger.getLogger(JunitListener.class.getName());
    private static int passCount = 0;
    private static int failCount = 0;
    private static int skipCount = 0;
    private DBUpdater dbUpdater = new DBUpdater();
    private int status = 1;

    /**
     * Called before any tests have been run.
     */
    public void testRunStarted(Description description) {
        LOGGER.log(Level.INFO, "Test Run Started");
    }

    /**
     * Called when all tests have finished
     */
    public void testRunFinished(Result result) {
        LOGGER.log(Level.INFO, "TestRun Completed : " + result.wasSuccessful());
    }

    /**
     * Called when an atomic test is about to be started.
     */
    public void testStarted(Description description) {
        LOGGER.log(Level.INFO, "Starting execution of test case : " + description.getMethodName());
    }

    /**
     * Called when an atomic test has finished, whether the test succeeds or fails.
     */
    public void testFinished(Description description) {
        LOGGER.log(Level.INFO, "Starting execution of test case finished : " + description.getMethodName());
        passCount++;
        JunitInitializer.platformManager.tearDown(status);
       /*
        JunitInitializer.getLocalResult().setStatus("Pass");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new java.util.Date(System.currentTimeMillis()));
        JunitInitializer.getLocalResult().setEndTime(currentDate);
        LOGGER.log(Level.INFO, "Finished execution of test case : " + description.getMethodName());
        JunitInitializer.getResultContainer().initResultData(1);
        JunitInitializer.getResultContainer().writeExecutionResult(true);*/
        if (JunitInitializer.getConfigData().isUseDB()) {
            dbUpdater.postExecutionStatus(JunitInitializer.getResultContainer().getExecutionPercentage(passCount, failCount, skipCount));
        }
    }

    /**
     * Called when an atomic test fails.
     */
    public void testFailure(Failure failure) {
        status=2;
        failCount++;
        String message = failure.getMessage();
        JunitInitializer.getLocalResult().setErrorMessage(message);
        JunitInitializer.getLocalResult().setStatus("Fail");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new java.util.Date(System.currentTimeMillis()));
        JunitInitializer.getLocalResult().setEndTime(currentDate);
        FrameworkLogger.logFail(message);
        LOGGER.log(Level.INFO, "Execution of test case failed : " +failure.getDescription().getMethodName()+ failure.getMessage());
    }

    /**
     * Called when a test will not be run, generally because a test method is annotated with Ignore.
     */
    public void testIgnored(Description description) {
        status=3;
        skipCount++;
        String currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new java.util.Date(System.currentTimeMillis()));
        JunitInitializer.getLocalResult().setEndTime(currentDate);
        JunitInitializer.getLocalResult().setStatus("Skip");
        LOGGER.log(Level.INFO, "Execution of test case ignored : " + description.getMethodName());
    }
}
