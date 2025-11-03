package com.gl.testngfw.execution;

import com.gl.testngfw.common.Constants;
import com.gl.testngfw.common.ExtentReportHelper;
import com.gl.testngfw.email.MailSender;
import com.gl.testngfw.enums.ExecutorType;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.logging.NoTestFoundForExecution;
import com.gl.testngfw.report.model.*;
import com.gl.testngfw.setup.GridInitializer;
import com.gl.testngfw.utility.Util;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestExecutor {
    private static final Logger LOGGER = Logger.getLogger(TestExecutor.class.getName());
    public static List<String> record = new ArrayList<>();
    static Map<String, String> executionList = new HashMap<>();
    private static Map<String, Map<String, TestCase>> testCasesList = new HashMap<>();
    private static ConfigData configData = new ConfigData();
    private static Map<String, UserDevice> deviceInfoMap = new HashMap<>();
    private static boolean isGridStarted = false;
    private static List<String> execution;
    private static String baseReportDirPath;
    private static Map<String, List<Result>> reportMap = new HashMap<>();
    private static List<Class> classList;
    private static ExecutionSelectedParams executionRecord;

    public static boolean isIsGridStarted() {
        return isGridStarted;
    }

    public static void setIsGridStarted(boolean isGridStarted) {
        TestExecutor.isGridStarted = isGridStarted;
    }

    public static Map<String, Map<String, TestCase>> getTestCasesList() {
        return testCasesList;
    }

    public static ConfigData getConfigData() {
        return configData;
    }

    public static void setConfigData(ConfigData configData) {
        TestExecutor.configData = configData;
    }

    public static Map<String, UserDevice> getDeviceInfoMap() {
        return deviceInfoMap;
    }

    public static List<Class> getClassList() {
        return classList;
    }

    public static void setClassList(List<Class> classList) {
        TestExecutor.classList = classList;
    }

    public static void setDeviceInfoMap(Map<String, UserDevice> deviceInfoMap) {
        TestExecutor.deviceInfoMap = deviceInfoMap;
    }

    public static ExecutionSelectedParams getExecutionRecord() {
        return executionRecord;
    }

    public static void setExecutionRecord(ExecutionSelectedParams executionRecord) {
        TestExecutor.executionRecord = executionRecord;
    }

    public static List<String> getExecution() {
        return execution;
    }

    public static String getBaseReportDirPath() {
        if (baseReportDirPath == null) {
            initReportDir();
        }
        return baseReportDirPath;
    }

    public static Map<String, List<Result>> getReportMap() {
        return reportMap;
    }

    /**
     * Method to distribute test according to number of connected devices/browsers/API servers
     *
     * @param count : number of connected devices/browsers/API servers
     */
    void distributeTests(int count) {
        int executionCount = count;
        execution = new ArrayList<>(executionList.keySet());
        startServer();
        try {
            if (configData.getExecutionType().equalsIgnoreCase(Constants.LOAD_TESTING)) {
                executionCount = getApiInstanceCount();
            }
            Util.setStartTime();
            if (executionCount == 0) {
                throw new NoTestFoundForExecution("No Test Found for Execution");
            }
            initReportDir();
            ExecutorService executorService = Executors.newFixedThreadPool(executionCount);
            for (int i = 0; i < executionCount; i++) {
                final int finalI = i;
                LOGGER.log(Level.INFO, "Device number : " + finalI);
                executorService.submit(() -> testRunnerParallelTestNg(finalI));
            }
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            generateExtendReport();
            sendEmail();
            LOGGER.log(Level.INFO, "ending");
            if (!configData.isDebugMode() && !configData.isGafCloud()) {
                GridInitializer.shutDownGrid();
            }
        } catch (NoTestFoundForExecution | IOException | InterruptedException e) {
            LOGGER.log(Level.WARNING, "", e);
        }

    }

    private void generateExtendReport() {
        if (!configData.isUseDB() && !configData.isGafCloud()) {
            ExtentReportHelper.generateLocalReport();
        }
    }

    private void sendEmail() throws IOException {
        if (configData.isUseDB() && !configData.getUseJenkins() && configData.isSendMailReport()) {
            MailSender.sendMail(System.getProperty("user.dir") + File.separator + "Reports" +
                    File.separator + "Execution_Report.html");
        }
    }

    private int getApiInstanceCount() {
        for (String key : getTestCasesList().get(Constants.API).keySet()) {
            if (getTestCasesList().get(Constants.API).get(key) != null) {
                record.add(key);
                execution.add(Constants.API);
            }
        }
        return record.size();
    }

    private void startServer() {
        if (!configData.isGafCloud() && (configData.getExecuteFor().contains(Platforms.WEB) || configData.getExecuteFor().contains(Platforms.MOBILE))) {
            try {
                GridInitializer.startServer();
            } catch (Exception e) {
                LOGGER.log(Level.INFO, "", e);
            }
        }
    }

    /**
     * TestNG Runner Method
     *
     * @param count: number of connected devices/browsers/API servers
     */
    private void testRunnerParallelTestNg(int count) {
        String platform = execution.get(count);
        Map<String, TestCase> testList = testCasesList.get(platform);
        Map<String, TestCase> junitTestList = getExecutionTestCaseMap(testList, ExecutorType.JUNIT);
        Map<String, TestCase> testNgTestList = getExecutionTestCaseMap(testList, ExecutorType.TESTNG);
        Map<String, TestCase> cucumberTestList = getExecutionTestCaseMap(testList, ExecutorType.CUCUMBER);
        Map<String, TestCase> jmeterTestList = getExecutionTestCaseMap(testList, ExecutorType.JMETER);
        Map<String, TestCase> stormTestList = getExecutionTestCaseMap(testList, ExecutorType.STORMTEST);
        String type = !jmeterTestList.isEmpty() ? ExecutorType.JMETER.name() : !stormTestList.isEmpty() ? ExecutorType.STORMTEST.name() : executionList.get(platform);
        if (!junitTestList.isEmpty()) {
            JunitRunner junitRunner = new JunitRunner(platform, type, junitTestList);
            junitRunner.executeJunitTest();
        }

        if (!testNgTestList.isEmpty() || !cucumberTestList.isEmpty() || !stormTestList.isEmpty()) {
            TestNgRunner testNgRunner = new TestNgRunner(platform, type, testList);
            testNgRunner.ExecuteTests(count);
        }
    }

    @NotNull
    private Map<String, TestCase> getExecutionTestCaseMap(Map<String, TestCase> testList, ExecutorType executorType) {
        Map<String, TestCase> testCaseList = new HashMap<>();
        for (String key : testList.keySet()) {
            if (testList.get(key).getExecutorType().equals(executorType)) {
                testCaseList.put(key, testList.get(key));
            }
        }
        return testCaseList;
    }

    private static void initReportDir() {
        System.setProperty("currentDate", new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
        baseReportDirPath = "Reports" + File.separator + System.getProperty("currentDate");

        Util.deleteDirectory(System.getProperty("user.dir") + "/FinalReport");
        Util.createDirectory(baseReportDirPath);
    }
}