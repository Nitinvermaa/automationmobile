package com.gl.testngfw.setup;

import com.gl.testngfw.api.AdbCommandsFactory;
import com.gl.testngfw.common.Constants;
import com.gl.testngfw.common.DBUpdater;
import com.gl.testngfw.enums.LogType;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.report.model.*;
import com.gl.testngfw.utility.CommandLineExecutor;
import com.gl.testngfw.utility.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.LogDetail;
import org.testng.ITestResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultContainer {
    private static final Logger LOGGER = Logger.getLogger(ResultContainer.class.getName());
    private static DBUpdater dbUpdater = new DBUpdater();
    private static Map<String, UserDevice> deviceInfoMap = TestExecutor.getDeviceInfoMap();
    private Integer totalTestCount = 0;
    private String logFile;
    private String networkLogFilePath = "";
    private String deviceLogsFilePath = "";
    private String appiumLogsFilePath = "";
    private String iosCrashLogsFilePath = "";
    private String videoPath = "";
    private String reportDirPath = "";
    private String snapshotPath = System.getProperty("user.dir") + "/src/main/resources/NoImage.png";
    private ExecutionContainer executionContainer = InitializerScript.getExecutionContainer();

    private static String getExecutionDate() {
        return System.getProperty("currentDate");
    }

    public List<Execution> writeExecutionResult(boolean includeResults) {
        List<Execution> suiteList = new ArrayList<>();
        try {
            if (includeResults) {
                executionContainer.getLocalExecution().setResultsList(getResultsListObj());
            } else {
                executionContainer.getLocalExecution().setResultsList(new ArrayList<>());
            }
            executionContainer.getLocalExecution().setEndTime(Util.getEndTime());
            suiteList.add(executionContainer.getLocalExecution());
            if (includeResults) {
                writeExecutionReport(suiteList);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "After writeExecutionResult Method failed", e);
        }
        return suiteList;
    }

    private synchronized void writeExecutionReport(List<Execution> suiteList) {
        try {
            //write converted json data to a file
            File reportFile = new File(System.getProperty("user.dir") + File.separator +
                    getReportDirPath() + File.separator + "ExecutionReport.json");
            if (!reportFile.exists() && !reportFile.createNewFile()) {
                LOGGER.log(Level.INFO, "Report File Not Created :: " + reportFile.toString());
            }
            FileWriter writer = new FileWriter(reportFile, false);
            Gson gson = new GsonBuilder().create();
            writer.write(gson.toJson(suiteList));
            writer.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    public void initResultData(int status) {
        Device device = new Device();
        Log log = new Log();
        Log deviceLog = new Log();
        Log netWorkLog = new Log();
        setExecutionPlatform(device, deviceLog);
        executionContainer.getLocalResult().setProjectUniqueKey(InitializerScript.getConfigData().getProjectID());
        executionContainer.getLocalResult().setDevice(device);
        executionContainer.getLocalResult().setTestCaseUniqueKey(executionContainer.getLocalTestContext().getTestCaseUniqueID());
        addExecutionLogs(log);
        addNetworkLogs(netWorkLog);
        executionContainer.getLocalResult().setExecutionId(Constants.getExecutionId());
        executionContainer.getLocalResult().setDescription(executionContainer.getLocalTestContext().getTestDescription());
        executionContainer.getLocalResult().setTestCaseId(executionContainer.getLocalTestContext().getTestCaseID());
        executionContainer.getLocalResult().setFeatureName(executionContainer.getLocalTestContext().getFeature());
        setStatus(status);

        addScreenShot();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date());
        executionContainer.getLocalResult().setEndTime(currentDate);
        addVideo();
        getResultsListObj().add(executionContainer.getLocalResult());
        postResults();
    }

    private void setExecutionPlatform(Device device, Log deviceLog) {
        Platforms platform = Util.getPlatform(executionContainer.getTypeOfExecution());
        switch (platform) {
            case MOBILE:
                device.setDeviceId(executionContainer.getExecutionPlatform());
                device.setDeviceName(deviceInfoMap.get(executionContainer.getExecutionPlatform()).getDeviceName());
                String os = executionContainer.isAndroid() ? Constants.ANDROID : Constants.IOS;
                device.setDeviceOS(os);
                device.setDeviceVersion(deviceInfoMap.get(executionContainer.getExecutionPlatform()).getDeviceVersion());
                executionContainer.getLocalResult().setPlatformName(device.getDeviceName());
                addDeviceLogs(deviceLog);
                break;
            case WEB:
                device.setDeviceName(executionContainer.getExecutionPlatform());
                executionContainer.getLocalResult().setPlatformName(executionContainer.getExecutionPlatform());
                break;
            case API:
                executionContainer.getLocalResult().setPlatformName(Platforms.API.name());
                break;
            case CONTRACT:
                executionContainer.getLocalResult().setPlatformName(Platforms.CONTRACT.name());
                break;
            case PERFORMANCE:
                executionContainer.getLocalResult().setPlatformName(Platforms.PERFORMANCE.name());
                break;
            case STB:
                executionContainer.getLocalResult().setPlatformName(Platforms.STB.name());
                break;
            case ROKU:
                executionContainer.getLocalResult().setPlatformName(Platforms.ROKU.name());
                break;
            case DESKTOP:
                executionContainer.getLocalResult().setPlatformName(Platforms.DESKTOP.name());
                break;
            default:
                break;
        }
    }

    private void addExecutionLogs(Log executionLog) {
        executionLog.setFile(Util.getEncoded(getLogFile() + ".log"));
        executionLog.setType(LogType.EXECUTION_LOG.name());
        executionContainer.getLocalResult().setLog(executionLog);
        executionContainer.getLocalResult().getLogs().add(LogType.EXECUTION_LOG.name());
    }

    private void addNetworkLogs(Log networkLog) {
        if (executionContainer.getConfigData().getIsBrowserMobProxyRequired()) {
            networkLog.setFile(Util.getEncoded(getNetworkLogFilePath()));
            networkLog.setType(LogType.NETWORK_LOG.name());
            executionContainer.getLocalResult().setLog(networkLog);
            executionContainer.getLocalResult().getLogs().add(LogType.NETWORK_LOG.name());
        }
    }

    private void addDeviceLogs(Log deviceLog) {
        if (executionContainer.getConfigData().getCaptureDeviceLogs()) {
            deviceLog.setFile(Util.getEncoded(getDeviceLogsFilePath()));
            deviceLog.setType(LogType.DEVICE_LOG.name());
            executionContainer.getLocalResult().setAdbLog(deviceLog);
            executionContainer.getLocalResult().getLogs().add(LogType.DEVICE_LOG.name());
        }
    }

    private void postResults() {
        if (InitializerScript.getConfigData().isUseDB()) {
            Result resultID = dbUpdater.postResultData(executionContainer.getLocalResult());
            if (resultID != null && resultID.isVideoPresent()) {
                dbUpdater.postVideo(resultID.getResultId(), new File(getVideoPath()));
            }
        }
    }

    private void setStatus(int status) {
        if (status == 1) {
            executionContainer.getLocalResult().setStatus("Pass");
        } else if (status == 2) {
            executionContainer.getLocalResult().setStatus("Fail");
            if (!executionContainer.getLocalResult().getSteps().isEmpty()) {
                executionContainer.getLocalResult().getSteps().get(executionContainer.getLocalResult().getSteps().size() - 1).setStatus("Fail");
            }
        } else if (status == 0) {
            executionContainer.getLocalResult().setStatus("Skip");
        }
    }

    private void addVideo() {
        File file = new File(getVideoPath());
        if (InitializerScript.getConfigData().isUseDB() && file.exists()) {
            executionContainer.getLocalResult().setVideoPresent(true);
        }
    }

    private void addScreenShot() {
        if (!executionContainer.isAPI() && "Fail".equals(executionContainer.getLocalResult().getStatus())) {
            Screenshot screenshot = new Screenshot();
            File snapshot = new File(getSnapshotPath());
            if (snapshot.exists()) {
                screenshot.setFile("data:image/png;base64," + Util.getEncoded(getSnapshotPath()));
            } else {
                screenshot.setFile("data:image/png;base64," + Util.getEncoded(System.getProperty("user.dir") + "/src/main/resources/NoImage.png"));
            }
            executionContainer.getLocalResult().setScreenshot(screenshot);
        }
    }

    private List<Result> getResultsListObj() {
        return TestExecutor.getReportMap().get(executionContainer.getExecutionPlatform());
    }

    public ExecutionStatus getExecutionPercentage(ITestResult testResult) {
        double totalTest = InitializerScript.getConfigData().getExecutionType().contains(Constants.LOAD_TESTING) ?
                testResult.getTestContext().getAllTestMethods().length * testResult.getMethod().getInvocationCount() :
                testResult.getTestContext().getAllTestMethods().length;
        double passTest = testResult.getTestContext().getPassedTests().size();
        double failedTest = testResult.getTestContext().getFailedTests().size();
        double skipTest = testResult.getTestContext().getSkippedTests().size();
        int percentage = (int) (((passTest + failedTest + skipTest) / totalTest) * 100);
        ExecutionStatus executionStatus = setExecutionStatus();
        Map<String, String> status = new HashMap<>();
        String platform = executionContainer.isWeb() || executionContainer.isAPI() ? executionContainer.getExecutionPlatform() :
                deviceInfoMap.get(executionContainer.getExecutionPlatform()).getDeviceName();
        status.put(platform, String.valueOf(percentage));
        executionStatus.setStatus(status);
        return executionStatus;
    }

    public ExecutionStatus getExecutionPercentage(int passCount, int failCount, int skipCount) {
        int percentage = (int) (((passCount + failCount + skipCount) / executionContainer.getLocalResultContainer().totalTestCount) * 100);
        ExecutionStatus executionStatus = setExecutionStatus();
        Map<String, String> status = new HashMap<>();
        String platform = executionContainer.isWeb() || executionContainer.isAPI() ? executionContainer.getExecutionPlatform() :
                deviceInfoMap.get(executionContainer.getExecutionPlatform()).getDeviceName();
        status.put(platform, String.valueOf(percentage));
        executionStatus.setStatus(status);
        return executionStatus;
    }

    private ExecutionStatus setExecutionStatus() {
        ExecutionStatus executionStatus = new ExecutionStatus();
        executionStatus.setExecutionId(Constants.getExecutionId());
        executionStatus.setProjectUniqueKey(InitializerScript.getConfigData().getProjectID());
        executionStatus.setProgress(Constants.IN_PROGRESS);
        executionStatus.setSuiteName(Constants.getSuiteName());
        return executionStatus;
    }

    public void initResultStatus(String platform) {
        if (InitializerScript.getConfigData().isUseDB()) {
            ExecutionStatus executionStatus = setExecutionStatus();
            Map<String, String> status = new HashMap<>();
            if (executionContainer.isMobile()) {
                status.put(deviceInfoMap.get(platform).getDeviceName(), "0");
            } else {
                status.put(platform, "0");
            }
            executionStatus.setStatus(status);
            dbUpdater.postExecutionStatus(executionStatus);
        }
    }

    public void postExecutionStatus() {
        boolean isExecutionCompleted = isExecutionCompleted();
        if (isExecutionCompleted) {
            ExecutionStatus executionStatus = getExecutionCompleteStatus();
            dbUpdater.postExecutionStatus(executionStatus);
        }
    }

    public ExecutionStatus getExecutionCompleteStatus() {
        ExecutionStatus executionStatus = new ExecutionStatus();
        Map<String, String> status = new HashMap<>();
        executionStatus.setExecutionId(Constants.getExecutionId());
        executionStatus.setProjectUniqueKey(executionContainer.getConfigData().getProjectID());
        executionStatus.setStatus(status);
        executionStatus.setProgress(Constants.COMPLETED);
        executionStatus.setSuiteName(Constants.getSuiteName());
        return executionStatus;
    }

    private boolean isExecutionCompleted() {
        Map<String, String> devicesStatus =
                dbUpdater.getExecutionStatus(Constants.getExecutionId()).get(0).getStatus();
        Set<String> key = devicesStatus.keySet();
        boolean isExecutionCompleted = true;
        for (String eachDevice : key) {
            if (!"100".equalsIgnoreCase(devicesStatus.get(eachDevice))) {
                isExecutionCompleted = false;
                break;
            }
        }
        return isExecutionCompleted;
    }

    public void setReportDir() {
        String reportPath = "";
        String baseReportDirPath = TestExecutor.getBaseReportDirPath();
        if (executionContainer.isWeb()) {
            reportPath = baseReportDirPath + File.separator + "WEB" + File.separator + executionContainer.getExecutionPlatform().replaceAll(" ", "_");
        } else if (executionContainer.isMobile()) {
            reportPath = baseReportDirPath + File.separator + "Mobile" +
                    File.separator + deviceInfoMap.get(executionContainer.getExecutionPlatform()).getDeviceName().replaceAll(" ", "_");
        } else if (executionContainer.isAPI()) {
            reportPath = baseReportDirPath + File.separator + executionContainer.getExecutionPlatform();
        }
        Util.createDirectory(reportPath);
        setReportDirPath(reportPath);
    }

    public void initLogger(Platforms platform) {
        String reportPath = "";
        String file = "";
        String random = Util.randomString(5);
        TestContext testCase = InitializerScript.getTestCase();
        String baseReportDirPath = TestExecutor.getBaseReportDirPath();
        switch (platform) {
            case MOBILE:
                String device = deviceInfoMap.get(executionContainer.getExecutionPlatform()).getDeviceName().replaceAll(" ", "_");
                reportPath = baseReportDirPath + File.separator + platform.name() + File.separator + device;
                file = reportPath + File.separator + Constants.LOG_PATH +
                        testCase.getClassName() + "_" + testCase.getTestCaseID() + "_" + getExecutionDate() + random;


                if (executionContainer.getConfigData().getCaptureDeviceLogs() && InitializerScript.isAndroid()) {
                    CommandLineExecutor.executeCommand(AdbCommandsFactory.getADBLogsMaxBufferSizeCommand(executionContainer.getExecutionPlatform()));
                }
                getLogHeaderData(testCase, platform).put("Device", device);
                InitializerScript.startNetworkLogs(testCase.getTestMethodName());
                break;
            case WEB:
                reportPath = baseReportDirPath + File.separator + platform.name() + File.separator + executionContainer.getExecutionPlatform().replaceAll(" ", "_");
                file = reportPath + Constants.LOG_PATH + testCase.getClassName() + "_" +
                        testCase.getTestCaseID() + "_" + getExecutionDate() + random;
                getLogHeaderData(testCase, platform).put("Browser", executionContainer.getExecutionPlatform());
                InitializerScript.startNetworkLogs(testCase.getTestMethodName());
                break;
            case API:
                reportPath = baseReportDirPath + File.separator + platform.name();
                file = reportPath + File.separator + Constants.LOG_PATH + executionContainer.getConfigData().getExecutionServer()
                        + "_" + testCase.getClassName() + "_" + testCase.getTestCaseID() + "_" + getExecutionDate() + random;
                configureRestAssuredLog();
                getLogHeaderData(testCase, platform).put("Server", executionContainer.getConfigData().getExecutionServer());
                break;
            case CONTRACT:
                reportPath = baseReportDirPath + File.separator + platform.name();
                file = reportPath + File.separator + Constants.LOG_PATH + executionContainer.getConfigData().getExecutionServer()
                        + "_" + testCase.getClassName() + "_" + testCase.getTestCaseID() + "_" + getExecutionDate() + random;
                configureRestAssuredLog();
                getLogHeaderData(testCase, platform).put("Server", executionContainer.getConfigData().getExecutionServer());
                break;
            case LOAD_TESTING:
                reportPath = baseReportDirPath + File.separator + executionContainer.getExecutionPlatform();
                getLogHeaderData(testCase, platform).put("Server", executionContainer.getConfigData().getExecutionServer());
                break;
            case PERFORMANCE:
                reportPath = baseReportDirPath + File.separator + executionContainer.getExecutionPlatform();
                break;
            case STB:
                reportPath = baseReportDirPath + File.separator + platform.name() + File.separator + executionContainer.getExecutionPlatform().replaceAll(" ", "_");
                getLogHeaderData(testCase, platform).put("Device", platform.name());
                break;
            case ROKU:
                reportPath = baseReportDirPath + File.separator + platform.name() + File.separator + executionContainer.getExecutionPlatform().replaceAll(" ", "_");
                getLogHeaderData(testCase, platform).put("Device", platform.name());
                break;
            case DESKTOP:
                reportPath = baseReportDirPath + File.separator + platform.name() + File.separator + executionContainer.getExecutionPlatform().replaceAll(" ", "_");
                getLogHeaderData(testCase, platform).put("Device", platform.name());
                break;
            default:
                break;
        }
        Util.createDirectory(reportPath);
        setReportDirPath(reportPath);
        setNetworkLogFilePath(file + ".har");
        setDeviceLogsFilePath(file + "_DeviceLogs.log");
        setAppiumLogsFilePath(file + "_AppiumLogs.log");
        setIosCrashLogsFilePath(file + "_CrashLogs.log");
        LOGGER.log(Level.INFO, "FileName init log method::: " + file);
        FrameworkLogger.config(getLogHeaderData(testCase, platform), file, testCase.getTestMethodName() + random);
        setLogFile(file);
    }

    private void configureRestAssuredLog() {
        LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
        RestAssured.config = RestAssured.config().logConfig(
                new LogConfig(FrameworkLogger.getPrintStream(), true));
    }

    /**
     * Method to set Header for logger
     *
     * @return header Map for logger
     */
    public static Map<String, String> getLogHeaderData(TestContext testCase, Platforms platform) {
        Map<String, String> headerDataMap = new HashMap<>();
        headerDataMap.put("TestClassName", testCase.getClassName());
        headerDataMap.put("TestCase", testCase.getTestMethodName());
        headerDataMap.put("TestCaseID", testCase.getTestCaseID());
        headerDataMap.put("Platform", platform.name());
        headerDataMap.put("RequirementId", testCase.getRequirementID());
        headerDataMap.put("Description", testCase.getTestDescription());
        headerDataMap.put("Execution Date", getExecutionDate());
        return headerDataMap;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public String getNetworkLogFilePath() {
        return networkLogFilePath;
    }

    public void setNetworkLogFilePath(String networkLogFilePath) {
        this.networkLogFilePath = networkLogFilePath;
    }

    public String getDeviceLogsFilePath() {
        return deviceLogsFilePath;
    }

    public void setDeviceLogsFilePath(String deviceLogsFilePath) {
        this.deviceLogsFilePath = deviceLogsFilePath;
    }

    public String getAppiumLogsFilePath() {
        return appiumLogsFilePath;
    }

    public void setAppiumLogsFilePath(String appiumLogsFilePath) {
        this.appiumLogsFilePath = appiumLogsFilePath;
    }

    public String getIosCrashLogsFilePath() {
        return iosCrashLogsFilePath;
    }

    public void setIosCrashLogsFilePath(String iosCrashLogsFilePath) {
        this.iosCrashLogsFilePath = iosCrashLogsFilePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getReportDirPath() {
        return reportDirPath;
    }

    public void setReportDirPath(String reportDirPath) {
        this.reportDirPath = reportDirPath;
    }

    public String getSnapshotPath() {
        return snapshotPath;
    }

    public void setSnapshotPath(String snapshotPath) {
        this.snapshotPath = snapshotPath;
    }

    public Map<String, List<Result>> getReportMap() {
        return TestExecutor.getReportMap();
    }

    public void setReportMap(String platform) {
        if (!TestExecutor.getReportMap().containsKey(platform)) {
            TestExecutor.getReportMap().put(platform, new ArrayList<Result>());
        }
    }

    public Integer getTotalTestCount() {
        return totalTestCount;
    }

    public void setTotalTestCount(Integer totalTestCount) {
        this.totalTestCount = totalTestCount;
    }
}