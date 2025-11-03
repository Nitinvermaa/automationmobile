package com.gl.testngfw.common;

import com.gl.testngfw.report.model.Execution;
import com.gl.testngfw.report.model.Result;
import com.gl.testngfw.report.model.Steps;
import com.gl.testngfw.utility.FileUtil;
import com.gl.testngfw.utility.Util;
import com.google.gson.Gson;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.response.ValidatableResponse;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.awt.Desktop;

public class ExtentReportHelper {
    private static final Logger LOGGER = Logger.getLogger(ExtentReportHelper.class.getName());
    private static String emailReportPath;
    private static DBUpdater dbUpdater;
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static String zipReportPath = System.getProperty("user.dir") + File.separator + "Reports" + File.separator + "reports.zip";


    private ExtentReportHelper() {
    }

    /**
     * Generate local execution report form Json report file
     */
    public static void generateLocalReport() {
        JSONArray finalArray;
        try {
            finalArray = Util.getExecutionResultList();
            emailReportPath = Util.getRecentExecutionDirectory(System.getProperty("user.dir") + File.separator + "Reports") + File.separator + "Execution_Report.html";
            ExtentReports extent = new ExtentReports(emailReportPath, true);
            extent.loadConfig(new File("extent-config.xml"));
            ExtentTest test, suite;
            for (Object obj : finalArray) {
                Gson gson = new Gson();
                Execution execution = gson.fromJson(obj.toString(), Execution.class);
                int pCount = 0, fCount = 0, sCount = 0;
                Map<String, List<ExtentTest>> extentTestList = new HashMap<>();
                String suiteName = null;
                for (Result result : execution.getResultsList()) {
                    suiteName = result.getPlatformName();
                    test = getExtentTest(extent, result);
                    setTestSteps(test, result);
                    ExtendResult extendResult = new ExtendResult(test, pCount, fCount, sCount, result).setStatus();
                    pCount = extendResult.getPassCount();
                    fCount = extendResult.getFailCount();
                    sCount = extendResult.getSkipCount();
                    extent.endTest(test);
                    setFeatureGroup(test, extentTestList, result);
                }
                String resultList = "<br><font color=\"#81C784\" >Pass : " + pCount +
                        "   </font><font color=\"#E57373\" >   Fail : " + fCount +
                        "</font><font color=\"#49A8F5\" >   Skip : " + sCount + "</font>";
                suite = getExtentSuiteNew(extent, execution, resultList, suiteName);
                setTestSuiteResults(extent, suite, extentTestList);
                extent.endTest(suite);
            }
            extent.flush();
            extent.close();
        } catch (IOException | ParseException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    /**
     * generate extend report with test steps from database
     *
     * @param executionId test execution Id
     * @throws ParseException parse exception
     */
    private static void generateCombinedReport(String executionId) throws ParseException {
        dbUpdater = new DBUpdater();
        List<List<Result>> resultList = getResultList(executionId);
        emailReportPath = System.getProperty("user.dir") + File.separator + "Reports" + File.separator + "Execution_Report.html";
        System.out.println("emailReportPath::::::: "+emailReportPath);
        ExtentReports extent = new ExtentReports(emailReportPath, true);
        extent.loadConfig(new File("extent-config.xml"));
        ExtentTest test, suite;
        for (List<Result> results : resultList) {
            String suiteName = null;
            Execution execution = dbUpdater.getExecutionInfo(executionId);
            int pCount = 0, fCount = 0, sCount = 0;
            Map<String, List<ExtentTest>> extentTestList = new HashMap<>();
            for (Result result : results) {
                suiteName = result.getPlatformName();
                test = getExtentTest(extent, result);
                setTestSteps(test, result);
                ExtendResult extendResult = new ExtendResult(test, pCount, fCount, sCount, result).setStatus();
                pCount = extendResult.getPassCount();
                fCount = extendResult.getFailCount();
                sCount = extendResult.getSkipCount();
                extent.endTest(test);
                setFeatureGroup(test, extentTestList, result);
            }
            String resultData = "<br><font color=\"#81C784\" >Pass : " + pCount +
                    "   </font><font color=\"#E57373\" >   Fail : " + fCount +
                    "</font><font color=\"#49A8F5\" >   Skip : " + sCount + "</font>";
            suite = getExtentSuiteNew(extent, execution, resultData, suiteName);
            setTestSuiteResults(extent, suite, extentTestList);
            extent.endTest(suite);
        }
        extent.flush();
        extent.close();
    }

    /**
     * generate extend report without test steps
     *
     * @param executionId test execution Id
     * @throws ParseException parse exception
     */
    private static void generateConsolidatedReport(String executionId) throws ParseException {
        dbUpdater = new DBUpdater();
        List<List<Result>> resultList = getResultList(executionId);
        emailReportPath = System.getProperty("user.dir") + File.separator + "Reports" + File.separator + "Execution_Report.html";
        ExtentReports extent = new ExtentReports(emailReportPath, true);
        extent.loadConfig(new File("extent-config.xml"));
        ExtentTest test, suite;
        for (List<Result> results : resultList) {
            int pCount = 0, fCount = 0, sCount = 0;
            Map<String, List<ExtentTest>> extentTestList = new HashMap<>();
            for (Result result : results) {
                test = getExtentTest(extent, result);
                test.assignCategory("Feature: " + result.getFeatureName());
                ExtendResult extendResult = new ExtendResult(test, pCount, fCount, sCount, result).setStatus();
                pCount = extendResult.getPassCount();
                fCount = extendResult.getFailCount();
                sCount = extendResult.getSkipCount();
                extent.endTest(test);
                setFeatureGroup(test, extentTestList, result);
            }
            String executionResultList = "<br><font color=\"#81C784\" >Pass : " + pCount +
                    "   </font><font color=\"#E57373\" >   Fail : " + fCount +
                    "</font><font color=\"#49A8F5\" >   Skip : " + sCount + "</font>";

            Execution execution = dbUpdater.getExecutionInfo(executionId);
            suite = getExtentSuite(extent, execution, executionResultList);
            setTestSuiteResults(extent, suite, extentTestList);
            extent.endTest(suite);
        }
        extent.flush();
        extent.close();
    }

    private static ExtentTest getExtentSuite(ExtentReports extent, Execution execution, String resultList) throws ParseException {
        ExtentTest suite;
        //  String suiteName = execution.getResultsList().get(0).getDevice() == null ? execution.getResultsList().get(0).getPlatformName() : execution.getResultsList().get(0).getDevice().getDeviceName();
        String suiteName = null;
        if (execution.getResultsList() != null) {
            suiteName = execution.getResultsList().get(0).getDevice() == null ? execution.getResultsList().get(0).getPlatformName() : execution.getResultsList().get(0).getDevice().getDeviceName();
        } else {
            suiteName = dbUpdater.getPlatformName(execution.getExecutionId());
        }
        suite = extent.startTest(suiteName + resultList);
        try {
            Date sDate = format.parse(execution.getStartTime());
            Date eDate = format.parse(execution.getEndTime());
            suite.setStartedTime(sDate);
            suite.setEndedTime(eDate);
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
        return suite;
    }

    private static ExtentTest getExtentSuiteNew(ExtentReports extent, Execution execution, String resultList, String suiteName) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        ExtentTest suite;
        suite = extent.startTest(suiteName + resultList);
        try {
            Date sDate = format.parse(execution.getStartTime());
            Date eDate = format.parse(execution.getEndTime());
            suite.setStartedTime(sDate);
            suite.setEndedTime(eDate);
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
        return suite;
    }

    private static ExtentTest getExtentTest(ExtentReports extent, Result result) {
        ExtentTest test;
        test = extent.startTest(result.getTestCaseId() + ": " + result.getDescription());
        Date sDate = null;
        try {
            sDate = format.parse(result.getStartTime());
            Date eDate = format.parse(result.getEndTime());
            test.setStartedTime(sDate);
            test.setEndedTime(eDate);
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
        return test;
    }

    private static void setTestSuiteResults(ExtentReports extent, ExtentTest suite, Map<String, List<ExtentTest>> extentTestList) {
        ExtentTest test;
        for (String feature : extentTestList.keySet()) {
            test = extent.startTest("</font><font size=\"4\", font color=\"blue\" > Feature: " + feature + "</font>");
            suite.appendChild(test);
            test.setStartedTime(extentTestList.get(feature).get(0).getStartedTime());
            test.setStartedTime(extentTestList.get(feature).get(extentTestList.get(feature).size() - 1).getEndedTime());
            List<String> status = new ArrayList<>();
            for (ExtentTest extentTest : extentTestList.get(feature)) {
                Date endDate = extentTest.getEndedTime();
                test.appendChild(extentTest);
                extentTest.setEndedTime(endDate);
                status.add(extentTest.getRunStatus().name());
            }
            LogStatus state = status.contains(LogStatus.FAIL.name()) ? LogStatus.FAIL : status.contains(LogStatus.PASS.name()) ? LogStatus.PASS : LogStatus.SKIP;
            test.log(state, "");
            extent.endTest(test);
        }
    }

    private static void setFeatureGroup(ExtentTest test, Map<String, List<ExtentTest>> extentTestList, Result result) {
        if (extentTestList.containsKey(result.getFeatureName())) {
            extentTestList.get(result.getFeatureName()).add(test);
        } else {
            List<ExtentTest> testList1 = new ArrayList<>();
            testList1.add(test);
            extentTestList.put(result.getFeatureName(), testList1);
        }
    }

    private static void setTestSteps(ExtentTest test1, Result result) throws ParseException {
        if (!result.getSteps().isEmpty()) {
            for (int i = 0; i < result.getSteps().size(); i++) {
                Steps step = result.getSteps().get(i);
                if ("Pass".equals(step.getStatus())) {
                    test1.log(LogStatus.PASS, step.getStep());
                } else if ("Fail".equals(step.getStatus())) {
                    test1.log(LogStatus.FAIL, step.getStep());
                }
                test1.getTest().getLogList().get(i).setTimestamp(format.parse(step.getTimeStamp()));
            }
        }
    }

    private static List<List<Result>> getResultList(String executionId) {
        ValidatableResponse response = dbUpdater.getExecutionPlatformReport(executionId);
        List<Result> executionResult = Arrays.asList(response.extract().body().as(Result[].class));
        LOGGER.log(Level.INFO, String.valueOf(response.extract().statusCode()));
        return executionResult.stream()
                .collect(Collectors.groupingBy(Result::getPlatformName))
                .entrySet().stream()
                .map(e -> new ArrayList<>(e.getValue()))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws ParseException {
        if ("false".equalsIgnoreCase(FileUtil.getConfigMAP("Project.Properties").get("useDB").getValue())) {
            generateLocalReport();
        } else {
            String executionId = System.getProperty("executionId");
            if (executionId == null || executionId.isEmpty()) {
                executionId = System.getenv("executionId");
            }
            LOGGER.log(Level.INFO, "Execution ID " + executionId);
            Constants.setExecutionId(executionId);
            generateCombinedReport(executionId);
            Util.zipFiles(zipReportPath);
            File file = new File(zipReportPath);

            //generateLocalReport();

            try {
                // Provide the path to your HTML file
                File htmlFile = new File(emailReportPath);
                System.out.println("Report file name is +>" + emailReportPath);
                dbUpdater.postReport(Constants.getExecutionId(), new File(emailReportPath));

                // Check if Desktop API is supported
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();

                    // Open the HTML file in the default browser
                    desktop.browse(htmlFile.toURI());
                } else {
                    System.out.println("Desktop is not supported on this platform.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static class ExtendResult {
        private ExtentTest test;
        private int passCount;
        private int failCount;
        private int skipCount;
        private Result result;

        ExtendResult(ExtentTest test, int passCount, int failCount, int skipCount, Result result) {
            this.test = test;
            this.passCount = passCount;
            this.failCount = failCount;
            this.skipCount = skipCount;
            this.result = result;
        }

        int getPassCount() {
            return passCount;
        }

        int getFailCount() {
            return failCount;
        }

        int getSkipCount() {
            return skipCount;
        }

        public ExtendResult setStatus() {
            switch (result.getStatus()) {
                case "Pass":
                    passCount += 1;
                    test.log(LogStatus.PASS, result.getDescription(), "Test Passed");
                    break;
                case "Fail":
                    failCount += 1;
                    setFailStatus();
                    break;
                case "Skip":
                    skipCount += 1;
                    test.log(LogStatus.SKIP, result.getDescription(), "Test Skipped");
                    break;
                default:
                    break;
            }
            return this;
        }

        private void setFailStatus() {
            if (result.getScreenshot() != null) {
                String screenshot = "true".equalsIgnoreCase(FileUtil.getConfigMAP("Project.Properties").get("useDB").getValue()) ?
                        dbUpdater.getScreenShot(result.getResultId()) : result.getScreenshot().getFile();
                test.log(LogStatus.FAIL, result.getDescription(), "Failure ScreenShot : "
                        + test.addBase64ScreenShot(screenshot) + "Exception : " + result.getErrorMessage());
            } else {
                test.log(LogStatus.FAIL, result.getDescription(), "Exception : " + result.getErrorMessage());
            }
        }
    }
}
