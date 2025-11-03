package com.gl.testngfw.execution;

import com.gl.testngfw.api.DeviceHelper;
import com.gl.testngfw.common.Constants;
import com.gl.testngfw.common.DBUpdater;
import com.gl.testngfw.enums.ExecutorType;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.logging.LoggerUtil;
import com.gl.testngfw.logging.NoTestFoundForExecution;
import com.gl.testngfw.report.model.*;
import com.gl.testngfw.utility.FileUtil;
import com.gl.testngfw.utility.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.EnumUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.gl.testngfw.common.TestSuiteUpdater.initTestSuite;

public class Executor {
    private static final Logger LOGGER = Logger.getLogger(Executor.class.getName());
    private static ConfigData configData = TestExecutor.getConfigData();
    private static String filePath = System.getProperty("user.dir") + File.separator + "/TestSuite/ExecutionSuite.json";
    private static List<String> ipList = new ArrayList<>();
    private static List<String> hostList = new ArrayList<>();
    private static DBUpdater dbUpdater = new DBUpdater();

    private Executor() {
    }

    public static void executeScripts(boolean execute) {
        ExecutionSelectedParams executionRecord;
        UpdateExecution testRecords;
        try {
            executionRecord = getExecutionParams();
            TestExecutor.setExecutionRecord(executionRecord);
            Constants.setProjectId(executionRecord.getProjectUniqueKey());
            setExecutionProperties(executionRecord);
            configData = new ConfigData();
            TestExecutor.setConfigData(configData);
            testRecords = initTestSuite().get(configData.getProjectID());
            Map<String, TestCase> testMap = getTestCaseMap(testRecords);
            configData.getBrowserList().clear();
            configData.getExecuteFor().clear();
            List<Browser> list = new ArrayList<>();
            for (String key : executionRecord.getTestCases().keySet()) {
                addExecutionTests(executionRecord, testMap, list, key);
            }
            executeTests(execute);
        } catch (NoTestFoundForExecution | IOException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    private static void executeTests(boolean execute) throws NoTestFoundForExecution {
        if (execute) {
            if (!TestExecutor.getTestCasesList().isEmpty()) {
                MyTestExecutor.execute();
                if (configData.isUseDB()) {
                    dbUpdater.postExecutionStatus(getExecutionCompleteStatus());
                }
            } else {
                throw new NoTestFoundForExecution("No Test found for this Node system.Please check node ip address");
            }
        }
    }

    private static void addExecutionTests(ExecutionSelectedParams executionRecord, Map<String, TestCase> testMap, List<Browser> list, String key) throws NoTestFoundForExecution {
        extractHostInfo(executionRecord, key);
       /* if (executionRecord.getTestCases().get(key).getIp().equals(Constants.getLocalIp()) ||*/
               if(executionRecord.getTestCases().get(key).getHostName().equals(Util.getHostName())) {
            if (!executionRecord.getTestCases().get(key).getTestCaseIds().isEmpty()) {
                setExecutionRecords(executionRecord, testMap, list, key, Util.getPlatform(executionRecord.getTestCases().get(key).getExecutionType()));
            } else {
                throw new NoTestFoundForExecution("No Test Found for Execution");
            }
        }
    }

    private static void setExecutionMobileList(List<String> deviceList) {
        if (configData.getExecuteFor().contains(Platforms.MOBILE)) {
            List<String> devList = new ArrayList<>(TestExecutor.getDeviceInfoMap().keySet());
            for (String key : devList) {
                if (!deviceList.contains(key)) {
                    TestExecutor.getDeviceInfoMap().remove(key);
                }
            }
        }
    }

    private static void setExecutionRecords(ExecutionSelectedParams executionRecord, Map<String, TestCase> testMap, List<Browser> list, String key, Platforms platform) {
        if (platform != null) {
            Platforms executionPlatform = setPlatforms(platform, key);
            list.add(executionRecord.getTestCases().get(key));
            checkAndCreateRecords(key, executionPlatform, executionRecord.getTestCases().get(key)
                    , testMap, TestExecutor.getTestCasesList());
            setMobileList(executionRecord, key, executionPlatform);
        }
    }

    @NotNull
    private static Platforms setPlatforms(Platforms platform, String key) {
        if (!configData.getExecuteFor().contains(platform)) {
            configData.setExecuteFor(platform);
        }
        if (platform.equals(Platforms.WEB) && !configData.getBrowserList().contains(Util.getBrowserName(key))) {
            configData.getBrowserList().add(Util.getBrowserName(key));
        }
        return platform;
    }

    private static void setMobileList(ExecutionSelectedParams executionRecord, String key, Platforms platform) {
        if (platform.equals(Platforms.MOBILE)) {
            List<String> deviceList = new ArrayList<>();
            String keyMobile = executionRecord.getTestCases().get(key).getDevice().getDeviceId();
            deviceList.add(keyMobile);
            if (key.equals(Platforms.MOBILE.getValue())) {
                deviceList.addAll(TestExecutor.getDeviceInfoMap().keySet());
            }
            setExecutionMobileList(deviceList);
            TestExecutor.setDeviceInfoMap(DeviceHelper.initDeviceModelInfo());
        }
    }

    private static void extractHostInfo(ExecutionSelectedParams executionRecord, String key) {
        if (!ipList.contains(executionRecord.getTestCases().get(key).getIp())) {
            ipList.add(executionRecord.getTestCases().get(key).getIp());
        }
        if (!hostList.contains(executionRecord.getTestCases().get(key).getHostName())) {
            hostList.add(executionRecord.getTestCases().get(key).getHostName());
        }
    }

    private static void setExecutionProperties(ExecutionSelectedParams executionRecord) throws IOException {
        for (Property property : executionRecord.getProperties()) {
            if (property.getPropertyFileName().equals(Constants.PROJECT_PROPERTIES)) {
                FileUtil.setProjectProperty(property.getPropertyFileName(), property.getProperties());
            }
            if (property.getPropertyFileName().equals(Constants.MAIL_CONFIG_PROPERTIES)) {
                FileUtil.setProjectProperty(property.getPropertyFileName(), property.getProperties());
            }
            if (property.getPropertyFileName().equals(Constants.PROJECT_CONFIG_PROPERTIES)) {
                FileUtil.setProjectProperty(property.getPropertyFileName(), property.getProperties());
            }
        }
    }

    private static ExecutionSelectedParams getExecutionParams() throws FileNotFoundException, NoTestFoundForExecution {
        ExecutionSelectedParams executionRecord;
        Gson gson = new Gson();
        if (!configData.isUseDB()) {
            File jsonFile = new File(filePath);
            if (!jsonFile.exists()) {
                if (TestExecutor.getDeviceInfoMap().isEmpty() && configData.getExecuteFor().contains(Platforms.MOBILE)) {
                    TestExecutor.setDeviceInfoMap(DeviceHelper.initDeviceModelInfo());
                }
                createLocalExecutionSuite();
            }
            JsonReader reader = new JsonReader(new FileReader(jsonFile));
            executionRecord = gson.fromJson(reader, ExecutionSelectedParams.class);
        } else {
            ValidatableResponse response = dbUpdater.getExecutionParameters(Constants.getExecutionId());
            if (!response.extract().body().asString().isEmpty()) {
                executionRecord = response.extract().body().as(ExecutionSelectedParams.class);
            } else {
                throw new NoTestFoundForExecution("No Test Found for Execution");
            }
        }
        return executionRecord;
    }

    private static Map<String, TestCase> getTestCaseMap(UpdateExecution testRecords) {
        Map<String, TestCase> testMap = new HashMap<>();

        for (TestCase test : testRecords.getTestCaseList()) {
            testMap.put(test.getTestCaseUniqueKey(), test);
        }
        return testMap;
    }

    private static void checkAndCreateRecords(String device, Platforms platform, Browser tests, Map<String, TestCase> testCaseMap, Map<String, Map<String, TestCase>> testCasesList) {
        Map<String, TestCase> executionIDMap = new HashMap<>();
        List<Test> testUniqueID = tests.getTestCaseIds();
        for (Test testID : testUniqueID) {
            addTestCase(platform, tests, testCaseMap, executionIDMap, testID);
        }
        String executionPlatform = platform.equals(Platforms.WEB) ? Util.getBrowserName(device).name() : EnumUtils.isValidEnum(com.gl.testngfw.enums.Platforms.class, device.toUpperCase()) ? platform.name() : device;
        testCasesList.put(executionPlatform, executionIDMap);
    }

    private static void addTestCase(Platforms platform, Browser tests, Map<String, TestCase> testCaseMap, Map<String, TestCase> executionIDMap, Test testID) {
        TestCase testCase = testCaseMap.get(testID.getTestCaseUniqueID());
        TestCase testCaseId = testCase != null ? (TestCase) testCase.clone() : null;
        String testMethod;
        if (null != testCaseId) {
            testMethod = testCaseId.getExecutorType().equals(ExecutorType.CUCUMBER) ?
                    testCaseId.getTestCaseDesc().replaceAll(" ", "_")
                    : testCaseId.getTestCaseClassName() + "." + testCaseId.getTestCaseName();
            switch (platform) {
                case MOBILE:
                    addMobileTest(platform, tests, executionIDMap, testID, testCaseId, testMethod);
                    break;
                case WEB:
                    addTests(executionIDMap, testID, testCaseId, testMethod, testCaseId.getSupportedPlatform().contains(Platforms.WEB));
                    break;
                case API:
                    addTests(executionIDMap, testID, testCaseId, testMethod, testCaseId.getSupportedPlatform().contains(Platforms.API));
                    break;
                case CONTRACT:
                    addTests(executionIDMap, testID, testCaseId, testMethod, testCaseId.getSupportedPlatform().contains(Platforms.CONTRACT));
                    break;
                case LOAD_TESTING:
                    addTests(executionIDMap, testID, testCaseId, testMethod, configData.getExecutionType().equalsIgnoreCase(Platforms.LOAD_TESTING.getValue()));
                    break;
                case PERFORMANCE:
                    addTests(executionIDMap, testID, testCaseId, testMethod, testCaseId.getSupportedPlatform().contains(Platforms.PERFORMANCE));
                    break;
                case STB:
                    addTests(executionIDMap, testID, testCaseId, testMethod, testCaseId.getSupportedPlatform().contains(Platforms.STB));
                    break;
                case ROKU:
                    addTests(executionIDMap, testID, testCaseId, testMethod, testCaseId.getSupportedPlatform().contains(Platforms.ROKU));
                    break;
                case DESKTOP:
                    addTests(executionIDMap, testID, testCaseId, testMethod, testCaseId.getSupportedPlatform().contains(Platforms.DESKTOP));
                    break;
                default:
                    break;
            }
        }
    }

    private static void addTests(Map<String, TestCase> executionIDMap, Test testID, TestCase testCaseId, String testMethod, boolean addTests) {
        if (addTests) {
            testCaseId.setApiInstance(Integer.parseInt(testID.getInstances()));
            executionIDMap.put(testMethod, testCaseId);
        }
    }

    private static void addMobileTest(Platforms platform, Browser tests, Map<String, TestCase> executionIDMap, Test testID, TestCase testCaseId, String testMethod) {
        if (tests.getMobileBrowser() && testCaseId.getSupportedPlatform().contains(Platforms.ANDROID_CHROME) || testCaseId.getSupportedPlatform().contains(Platforms.IOS_SAFARI)) {
            executionIDMap.put(testMethod, testCaseId);
            Constants.getMobileBrowsers().put(platform.getValue(), true);
        } else if (testCaseId.getSupportedPlatform().contains(Platforms.ANDROID) || testCaseId.getSupportedPlatform().contains(Platforms.IOS)) {
            testCaseId.setApiInstance(Integer.parseInt(testID.getInstances()));
            executionIDMap.put(testMethod, testCaseId);
        }
    }

    private static void createLocalExecutionSuite() {
        ExecutionSelectedParams executionSelectedParams = new ExecutionSelectedParams();
        List<Property> properties = new ArrayList<>();
        List<Test> testIds = new ArrayList<>();
        Map<String, Browser> testCases = new HashMap<>();

        getPropertiesList(properties);

        for (Platforms platform : configData.getExecuteFor()) {
            if (platform.equals(Platforms.WEB)) {
                configData.getBrowserList().forEach(x -> addTest(x.name(), platform, testCases));
            }else if (platform.equals(Platforms.MOBILE)) {
                TestExecutor.getDeviceInfoMap().keySet().forEach(x -> addTest(x, platform, testCases));
            }else{
                addTest(platform.name(), platform, testCases);
            }
        }

        executionSelectedParams.setProperties(properties);
        executionSelectedParams.setSuiteName("LocalSuite");
        executionSelectedParams.setExecutionId("1234");
        executionSelectedParams.setProjectUniqueKey(Constants.getProjectId());
        executionSelectedParams.setTestCases(testCases);
        writeExecutionSuite(executionSelectedParams);
    }

    private static void addTest(String executionDevice, Platforms platform, Map<String, Browser> testCases) {
        List<Test> testIds = new ArrayList<>();
        Browser browser = new Browser();
        Device device = new Device();
        switch (platform) {
            case WEB:
                device.setDeviceName(executionDevice);
                device.setDeviceVersion("");
                device.setDeviceId("");
                browser.setExecutionType(platform.name());
                break;
            case MOBILE:
                device.setDeviceName(TestExecutor.getDeviceInfoMap().get(executionDevice).getDeviceName());
                device.setDeviceVersion(TestExecutor.getDeviceInfoMap().get(executionDevice).getDeviceVersion());
                device.setDeviceId(executionDevice);
                browser.setExecutionType(platform.name());
                break;
            case API:
            case CONTRACT:
                device.setDeviceName(platform.name());
                device.setDeviceVersion("");
                device.setDeviceId("");
                browser.setExecutionType(platform.name());
                break;
            case STB:
                device.setDeviceName(executionDevice);
                device.setDeviceVersion("");
                device.setDeviceId("");
                device.setDeviceIp("127.0.0.1");
                device.setDevicePort("8000");
                device.setSlotNumber("1");
                browser.setExecutionType(Platforms.STB.getValue());
                break;
            case ROKU:
                device.setDeviceName(executionDevice);
                device.setDeviceVersion("");
                device.setDeviceId("");
                browser.setExecutionType(Platforms.ROKU.getValue());
                break;
            case DESKTOP:
                device.setDeviceName(executionDevice);
                device.setDeviceVersion("");
                device.setDeviceId("");
                browser.setExecutionType(Platforms.DESKTOP.getValue());
                break;
            default:
                break;
        }
        browser.setDevice(device);
        browser.setIp(Constants.getLocalIp());
        browser.setHostName(Util.getHostName());
        Test testID = new Test();
        testID.setTestCaseUniqueID("1");
        testID.setInstances("1");
        testIds.add(testID);
        browser.setMobileBrowser(false);
        browser.setTestCaseIds(testIds);
        testCases.put(device.getDeviceId(), browser);
    }

    private static void getPropertiesList(List<Property> properties) {
        Property projectProperty = new Property();
        projectProperty.setProjectUniqueKey(configData.getProjectID());

        projectProperty.setPropertyFileName(Constants.PROJECT_PROPERTIES);
        projectProperty.setProperties(FileUtil.getConfigMAP(Constants.PROJECT_PROPERTIES));
        properties.add(projectProperty);

        Property projectConfigProperty = new Property();
        projectConfigProperty.setProjectUniqueKey(configData.getProjectID());

        projectConfigProperty.setPropertyFileName(Constants.PROJECT_CONFIG_PROPERTIES);
        projectConfigProperty.setProperties(FileUtil.getConfigMAP(Constants.PROJECT_CONFIG_PROPERTIES));
        properties.add(projectConfigProperty);

        Property mailConfigProperty = new Property();
        mailConfigProperty.setProjectUniqueKey(configData.getProjectID());

        mailConfigProperty.setPropertyFileName(Constants.MAIL_CONFIG_PROPERTIES);
        mailConfigProperty.setProperties(FileUtil.getConfigMAP(Constants.MAIL_CONFIG_PROPERTIES));
        properties.add(mailConfigProperty);
    }

    private static void writeExecutionSuite(ExecutionSelectedParams executionSelectedParams) {
        try {
            //write converted json data to a file
            File file = new File(System.getProperty("user.dir") + File.separator + "/TestSuite");
            if (!file.exists() && !file.mkdirs()) {
                LOGGER.log(Level.INFO, "Test Suite Directory Not Created");
            }
            FileWriter writer = new FileWriter(filePath);
            Gson gson = new GsonBuilder().create();
            writer.write(gson.toJson(executionSelectedParams));
            writer.close();

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    private static ExecutionStatus getExecutionCompleteStatus() {
        ExecutionStatus executionStatus = new ExecutionStatus();
        Map<String, String> status = new HashMap<>();
        executionStatus.setExecutionId(Constants.getExecutionId());
        executionStatus.setProjectUniqueKey(configData.getProjectID());
        executionStatus.setStatus(status);
        executionStatus.setProgress(Constants.COMPLETED);
        executionStatus.setSuiteName(Constants.getSuiteName());
        return executionStatus;
    }

    public static void main(String[] args) throws IOException {
        LoggerUtil.setup();
        String id = System.getProperty("executionId");
       // Constants.setExecutionId("62736b7ef41e94044f208156");
        if (id == null || id.isEmpty()) {
            id = System.getenv("executionId");
        }
        if (id != null) {
         // Constants.setExecutionId("62736b7ef41e94044f208156");
            Constants.setExecutionId(id);
            Constants.setSuiteName(dbUpdater.getExecutionParameters(Constants.getExecutionId()).extract().jsonPath().get("suiteName"));
        }
        LOGGER.log(Level.INFO, "Execution Id :: " + Constants.getExecutionId());
        System.out.println("Execution Id :: " + Constants.getExecutionId());
        executeScripts(true);
    }
}
