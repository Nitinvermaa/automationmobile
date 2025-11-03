package com.gl.testngfw.setup;

import com.gl.testngfw.api.*;
import com.gl.testngfw.api.ConsumerClient;
import com.gl.testngfw.common.Constants;
import com.gl.testngfw.common.DBUpdater;
import com.gl.testngfw.datadriven.model.DataContainer;
import com.gl.testngfw.datadriven.reader.*;
import com.gl.testngfw.enums.DataType;
import com.gl.testngfw.enums.ExecutorType;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.execution.HeaderData;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.report.model.*;
import com.gl.testngfw.utility.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.cucumber.testng.PickleWrapper;
import org.apache.commons.io.FileUtils;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Optional;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseInitializer extends CapabilityManager {
    private static final Logger LOGGER = Logger.getLogger(BaseInitializer.class.getName());
    static DBUpdater dbUpdater;
    private static ExecutionContainer executionContainer;
    private static boolean isDriverCreated = false;
    DataContainer dataContainer;
    private VideoRecord videoRecord = new VideoRecord();

    /**
     * Method to get platform
     *
     * @return boolean
     */
    public static boolean isAndroid() {
        //getExecutionContainer().getTypeOfExecution().equals(Constants.MOBILE) && getExecutionContainer().getExecutionPlatform().length() < 40;
        return getExecutionContainer().isAndroid();
    }

    /**
     * Method to get platform
     *
     * @return boolean
     */
    public static boolean isAPI() {
        return getExecutionContainer().isAPI();
    }

    /**
     * Method to get platform
     *
     * @return boolean
     */
    public static boolean isIOS() {
        return getExecutionContainer().isIOS();
    }

    /**
     * Method to get platform
     *
     * @return boolean
     */
    public static boolean isMobile() {
        return getExecutionContainer().isMobile();
    }

    public static ResultContainer getResultContainer() {
        return getExecutionContainer().getLocalResultContainer();
    }

    /**
     * Method to get platform
     *
     * @return boolean
     */
    public static boolean isWeb() {
        return getExecutionContainer().isWeb();
    }


    public static TestContext getTestCase() {
        return getExecutionContainer().getLocalTestContext();
    }

    static String getUserDir() {
        return System.getProperty("user.dir");
    }

    /**
     * Method to get Appium Driver
     *
     * @return : appium driver
     */
    public static AppiumDriver getDriver() {
        return getExecutionContainer().getDriver();
    }

    /**
     * Method to get Android Driver
     *
     * @return : android driver
     */
    public static AndroidDriver getAndroidDriver() {
        return getExecutionContainer().getAndroidDriver();
    }

    /**
     * Method to get IOS Driver
     *
     * @return : ios driver
     */
    public static IOSDriver getIOSDriverDriver() {
        return getExecutionContainer().getIOSDriverDriver();
    }

    /**
     * Method to get Web Driver
     *
     * @return : Web driver
     */
    public static RemoteWebDriver getWebDriver() {
        return getExecutionContainer().getWebDriver();
    }

    /**
     * Method to get API Driver
     *
     * @return : API driver
     */
    public static RestAPIExtension getApiDriver() {
        return getExecutionContainer().getApiDriver();
    }

    /**
     * Method to get Contract Driver
     *
     * @return : ConsumerClient
     */
    public static ConsumerClient getConsumerClient() {
        return getExecutionContainer().getContractDriver();
    }

    /**
     * Method to get Contract Driver
     *
     * @return : new instance of ConsumerClient
     */
    public static ConsumerClient getNewConsumerClient() {
        return new ConsumerClient();
    }

    /**
     * Method to get StormTest Driver
     *
     * @return : instance of StormTest
     */
    public static StormTest getStormTestDriver() {
        return getExecutionContainer().getStormTestDriver();
    }


    /**
     * Method to start Web driver
     *
     * @throws MalformedURLException: Throws Malformed URL Exception
     */
/*
    private synchronized void setWebDriver(String browser) throws IOException {
        RemoteWebDriver localDriver = null;
        URL hubURL = HubUtil.getHubURL() == null ? new URL(Constants.getGridHubUrl()) : HubUtil.getHubURL();
        LOGGER.log(Level.INFO, hubURL.toString());
        switch (Util.getBrowserName(browser)) {
            case CHROME:
                localDriver = getRemoteWebDriver(hubURL, getChromeBrowserCapabilitiesWithProxy(getProxyPort()), getChromeBrowserCapabilities());
                break;
            case FIREFOX:
                localDriver = getRemoteWebDriver(hubURL, getFirefoxBrowserCapabilitiesWithProxy(getProxyPort()), getFirefoxBrowserCapabilities());
                break;
            case SAFARI:
                localDriver = getRemoteWebDriver(hubURL, getSafariBrowserCapabilitiesWithProxy(getProxyPort()), getSafariBrowserCapabilities());
                break;
            case IE:
                localDriver = getRemoteWebDriver(hubURL, getIEBrowserCapabilitiesWithProxy(getProxyPort()), getIEBrowserCapabilities());
                localDriver.manage().window().setSize(new Dimension(1024, 768));
                break;
            case EDGE:
                localDriver = getRemoteWebDriver(hubURL, getEdgeBrowserCapabilitiesWithProxy(getProxyPort()), getEdgeBrowserCapabilities());
                break;
            default:
                FrameworkLogger.logWarning("Invalid Browser Type " + browser);
                break;
        }
        getExecutionContainer().setWebDriver(localDriver);
        getWebDriver().manage().window().maximize();
    }
*/

    public static ConfigData getConfigData() {
        return getExecutionContainer().getConfigData();
    }

   private static Execution initLocalExecution() {
        Execution execution = new Execution();
        execution.setStartTime(Util.getStartTime());
        execution.setExecutionDate(Util.getStartTime());
        execution.setSuiteName(Constants.getSuiteName());
        execution.setExecutionId(Constants.getExecutionId());
        execution.setProjectUniqueKey(TestExecutor.getConfigData().getProjectID());
        getExecutionContainer().setLocalExecution(execution);
        return execution;
    }

    protected static void setExecutionData(String platform, String executionType, Execution execution) {
        String currentPlatform = getExecutionPlatformName(platform);
        String type = executionType == null ? getConfigData().getExecuteFor().get(0).name() : executionType;
        getExecutionContainer().setLocalResultContainer(new ResultContainer());
        getExecutionContainer().setLocalExecution(execution);
        getExecutionContainer().setLocalConfigData(TestExecutor.getConfigData());
        getExecutionContainer().setTypeOfExecution(type);
        getExecutionContainer().setExecutionPlatform(currentPlatform);
        getExecutionContainer().setThreadLocalResult(new Result());
    }

    private static String getExecutionDate() {
        return System.getProperty("currentDate");
    }

    public static Map<String, UserDevice> getDeviceInfoMap() {
        return deviceInfoMap;
    }

    public static void exitGrid() {
        if (getConfigData().isDebugMode() && !getConfigData().isGafCloud()) {
            GridInitializer.shutDownGrid();
        }
    }

    public static void stopAppium() throws InterruptedException {
        if (isMobile() && !getConfigData().isGafCloud()) {
            if (isIOS() && !getConfigData().getIosWebkitProxyPath().isEmpty()) {
                AppiumManager.destroyIOSWebKitProxy();
            }
            if (getConfigData().isAppiumStartedUsingServices()) {
                AppiumManager.stopService(getExecutionContainer().getExecutionPlatform());
            }
        }
    }

    /**
     * Method for exception handling
     */
    protected static void exceptionHandling() {
        LOGGER.log(Level.INFO, "Exception Handling");
    }

    public synchronized static String getCurrentPlatform() {
        return getExecutionContainer().getExecutionPlatform();
    }

    public synchronized static void setCurrentPlatform(String currentPlatform) {
        getExecutionContainer().setExecutionPlatform(currentPlatform);
    }

    public static ExecutionContainer getExecutionContainer() {
        return executionContainer;
    }

    protected static void setExecutionContainer(ExecutionContainer executionContainer) {
        BaseInitializer.executionContainer = executionContainer;
    }

    public static Result getLocalResult() {
        return executionContainer.getLocalResult();
    }

    protected String getLogFile() {
        return getResultContainer().getLogFile();
    }

    protected String getVideoPath() {
        return getResultContainer().getVideoPath();
    }

    static String getReportDirPath() {
        return getResultContainer().getReportDirPath();
    }

    public String getSnapshotPath() {
        return getUserDir() + "/src/main/resources/NoImage.png";
    }

    static String getExecutionPlatformName(String platform) { String exePlatform = platform;
        if (platform == null) {
            if (getConfigData().getExecuteFor().contains(Platforms.WEB)) {
                exePlatform = getConfigData().getBrowserList().get(0).name();
            } else if (getConfigData().getExecuteFor().contains(Platforms.MOBILE)) {
                if (TestExecutor.getDeviceInfoMap().isEmpty()) {
                    TestExecutor.setDeviceInfoMap(DeviceHelper.initDeviceModelInfo());
                }
                List<UserDevice> deviceModels = new ArrayList<>(TestExecutor.getDeviceInfoMap().values());
                if (!deviceModels.isEmpty()) {
                    exePlatform = deviceModels.get(0).getDeviceId();
                } else {
                    LOGGER.log(Level.WARNING, "device not connected");
                }
            } else if (getConfigData().getExecuteFor().contains(Platforms.API)) {
                exePlatform = Constants.API;
            }else{
                exePlatform = getConfigData().getExecuteFor().get(0).name();
            }
        }
        return exePlatform;
    }
/*

    void initRecordVideo(String reportPath) {
        if (isWeb() && isDriverCreated) {
            String videoName = getExecutionContainer().getLocalTestContext().getTestMethodName() + Util.randomString(5);
            getExecutionContainer().getLocalResultContainer().setVideoPath(reportPath + "/Videos/" + videoName + ".avi");
            LOGGER.log(Level.INFO, "Video Path :: " + getReportFileDir() + "/Videos ----Test Case Name ::" + videoName);
            videoRecord.startRecording(new File(reportPath + "/Videos/"), videoName);
        } else if (isMobile()) {
            if (isAndroid()) {
                getAndroidDriver().startRecordingScreen(
                        new AndroidStartScreenRecordingOptions().
                                withUploadOptions(ScreenRecordingUploadOptions.uploadOptions()).
                                withBitRate(5000000).
                                withVideoSize("720x1280").
                                withTimeLimit(Duration.ofSeconds(180)));
            } else if (isIOS()) {
                getIOSDriverDriver().startRecordingScreen();
            }
        }
    }
*/

    protected static String getReportFileDir() {
        return getReportDirPath();
    }

    static Execution initExecution(@Optional String executionType, String currentPlatform) {
        setExecutionContainer(new ExecutionContainer());
        getExecutionContainer().setLocalResultContainer(new ResultContainer());
        getExecutionContainer().setLocalConfigData(TestExecutor.getConfigData());
        Execution execution = initLocalExecution();
        String type = executionType == null ? getConfigData().getExecuteFor().get(0).name() : executionType;
        getResultContainer().setReportMap(currentPlatform);
        getExecutionContainer().setTypeOfExecution(type);
        getExecutionContainer().setExecutionPlatform(currentPlatform);
        getResultContainer().initResultStatus(currentPlatform);
        return execution;
    }

    public static void initResult(int status) {
        getResultContainer().initResultData(status);
        getResultContainer().writeExecutionResult(true);
        exceptionHandling();
    }

    static void initTestCase(Method method, Object[] params) {
        HeaderData headerData = method.getAnnotation(HeaderData.class);
        TestCase testCase;
        String testId = headerData == null ? getTestId(params, headerData) : headerData.testCaseUniqueId();
        testCase = TestExecutor.getTestCasesList().containsKey(getCurrentPlatform())?TestExecutor.getTestCasesList()
                .get(getCurrentPlatform()).values().stream().filter(x -> x.getTestCaseUniqueKey().equals(testId)).findAny().orElse(null):null;
        getExecutionContainer().setLocalTestContext(new TestContext(headerData, testCase,method,Util.getExecutorType(method,headerData)));
        getTestCase().setClassName(method.getDeclaringClass().getSimpleName());
        getTestCase().setTestMethodName(method.getName());
    }

    private static String getTestId(Object[] params, HeaderData headerData) {
        String testId = "";
        if (headerData == null) {
            List<String> tags = ((PickleWrapper) (params[0])).getPickle().getTags();
            for (String tag : tags) {
                if (tag.contains("TestId")) {
                    testId = tag.substring(tag.indexOf("(") + 1, tag.indexOf(")"));
                }
            }
        }
        return testId;
    }

    private static int getProxyPort() {
        int currentProxyPort;
        if (isWeb()) {
            currentProxyPort = Integer.parseInt(Util.getAvailablePort());
        } else {
            currentProxyPort = Util.getProxyPort(getExecutionContainer().getExecutionPlatform());
        }
        Constants.getBrowserProxyPorts().put(getCurrentPort(), currentProxyPort);
        return currentProxyPort;
    }

    static Integer getCurrentPort() {
        if (Constants.getMobilePorts().containsKey(getExecutionContainer().getExecutionPlatform())) {
            return Constants.getMobilePorts().get(getExecutionContainer().getExecutionPlatform());
        } else if (Constants.getBrowserPorts().containsKey(getExecutionContainer().getExecutionPlatform())) {
            return Constants.getBrowserPorts().get(getExecutionContainer().getExecutionPlatform());
        }
        return -1;
    }

    static synchronized void startNetworkLogs(String methodName) {
        if (getConfigData().getIsBrowserMobProxyRequired()) {
            try {
                int currentProxyPort = isWeb() ? Constants.getBrowserProxyPorts().get(getCurrentPort()) : getProxyPort();
                if (!BrowserProxyHelper.isStarted()) {
                    BrowserProxyHelper.start(currentProxyPort);
                }
                BrowserProxyHelper.newHar(methodName);
            } catch (Exception e) {
                LOGGER.log(Level.INFO, "", e);
            }
        }
    }

    /**
     * Method to capture screenshots on failure.
     */
    public static void captureScreenshot() throws IOException {
        Util.createDirectory(getReportFileDir() + "/Snapshots/");
        String path = getUserDir() + "/";
        path = path + getReportFileDir() + "/Snapshots/";
        File screenshot = null;
        String random = Util.randomString(5);
        try {
            getResultContainer().setSnapshotPath(path + getTestCase().getClassName() + "_" + getTestCase().getTestCaseID() + "_" + random + ".png");
            if (isAndroid()) {
                screenshot = getAndroidDriver().getScreenshotAs(OutputType.FILE);
            } else if (isIOS()) {
                screenshot = getIOSDriverDriver().getScreenshotAs(OutputType.FILE);
            } else if (isWeb()) {
                screenshot = getWebDriver().getScreenshotAs(OutputType.FILE);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error while capturing screenshot", e);
            FrameworkLogger.logWarning("Error while capturing screenshot");
        }

        if (screenshot != null && screenshot.exists()) {
            FileUtils.copyFile(screenshot, new File(getResultContainer().getSnapshotPath()));
        }
    }
/*
    *//**
     * Method to set Header for logger
     *
     * @return header Map for logger
     *//*
    private Map<String, String> getLogHeaderData() {
        Map<String, String> headerDataMap = new HashMap<>();
        headerDataMap.put("TestClassName", getTestCase().getClassName());
        headerDataMap.put("Platform", getExecutionContainer().getExecutionPlatform());
        if (isMobile()) {
            headerDataMap.put("Device", deviceInfoMap.get(getExecutionContainer().getExecutionPlatform()).getDeviceName());
        } else if (isWeb()) {
            headerDataMap.put("Browser", getExecutionContainer().getExecutionPlatform());
        } else if (isAPI()) {
            headerDataMap.put("Server", getConfigData().getExecutionServer());
        }
        headerDataMap.put("RequirementId", getTestCase().getRequirementID());
        headerDataMap.put("Description", getTestCase().getTestDescription());
        headerDataMap.put("Execution Date", getExecutionDate());
        return headerDataMap;
    }*/

   /* *//**
     * Method to initialize Logger
     *//*
    synchronized void initLogger() {
        String file = "";
        Util.createDirectory(getReportFileDir());
        String random = Util.randomString(5);
        if (isMobile()) {
            file = getReportFileDir() + File.separator + Constants.LOG_PATH +
                    getTestCase().getClassName() + "_" + getTestCase().getTestCaseID() + "_" + getExecutionDate() + random;
        } else if (isWeb()) {
            file = getReportFileDir()
                    + Constants.LOG_PATH + getTestCase().getClassName() + "_" +
                    getTestCase().getTestCaseID() + "_" + getExecutionDate() + random;
        } else if (isAPI()) {
            file = getReportFileDir() + File.separator + Constants.LOG_PATH + getConfigData().getExecutionServer()
                    + "_" + getTestCase().getClassName() + "_" + getTestCase().getTestCaseID() + "_" + getExecutionDate() + random;
        }
        getResultContainer().setNetworkLogFilePath(file + ".har");
        getResultContainer().setDeviceLogsFilePath(file + "_DeviceLogs.log");
        getResultContainer().setAppiumLogsFilePath(file + "_AppiumLogs.log");
        getResultContainer().setIosCrashLogsFilePath(file + "_CrashLogs.log");

        LOGGER.log(Level.INFO, "FileName init log method::: " + file);
        FrameworkLogger.config(getLogHeaderData(), file, getTestCase().getTestMethodName() + random);

        if (isAPI()) {
            LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
            RestAssured.config = RestAssured.config().logConfig(
                    new LogConfig(FrameworkLogger.getPrintStream(), true));
        }
        startNetworkLogs(getTestCase().getTestMethodName());

        if (getConfigData().getCaptureDeviceLogs() && isAndroid()) {
            CommandLineExecutor.executeCommand(AdbCommandsFactory.getADBLogsMaxBufferSizeCommand(getExecutionContainer().getExecutionPlatform()));
        }
        getResultContainer().setLogFile(file);
    }*/

    /**
     * Initializes test data based on obtained data type and path to the test data
     *
     * @param dataType - a data type
     * @param path     - a path to the test data
     */
    void initTestData(DataType dataType, String path) throws ParseException {
        LOGGER.log(Level.INFO, "******Start**********initTestData***********");
        TestDataReader dataReader = getTestDataReaderByDataType(dataType);
        String dataPath = getUserDir() + "/Resources/" + path;
        boolean pathValid = !dataPath.isEmpty();
        dataContainer = dataReader != null ? (pathValid ? dataReader.readTestData(dataPath) :
                dataReader.readTestData(getClass())) : null;
    }

    /**
     * Method to select dataType
     *
     * @param dataType : Type of data (Excel, Json, XML, CSV)
     * @return : Test Data
     */
    TestDataReader getTestDataReaderByDataType(DataType dataType) {
        LOGGER.log(Level.INFO, "******Start**********getTestDataReaderByDataType***********");
        switch (dataType) {
            case JSON:
                return new JSONDataReader();
            case XML:
                return new XMLDataReader();
            case EXCEL:
                return new ExcelDataReader();
            case CSV:
                return new CSVDataReader();
            default:
                break;
        }
        return null;
    }

    public static boolean isDriverCreated() {
        return isDriverCreated;
    }

    public static void setIsDriverCreated(boolean isDriverCreated) {
        BaseInitializer.isDriverCreated = isDriverCreated;
    }

    private void captureAdbLogs() {
        if (getConfigData().getCaptureDeviceLogs() && isAndroid()) {
            CommandLineExecutor.executeCommand(
                    AdbCommandsFactory.getCaptureADBLogsCommand(getExecutionContainer().getExecutionPlatform(), getResultContainer().getDeviceLogsFilePath()));
        }
    }

    public static void captureDeviceLogs() {
        if (isDriverCreated && getConfigData().getCaptureDeviceLogs()) {
            //  getDriver().executeScript("mobile:stopLogsBroadcast");
            captureAndroidLogs();
            captureIosLogs();
            captureAppiumLogs();
            //captureBrowserLogs();
        }
    }

    private static void captureAppiumLogs() {
        Set<String> logTypes = getDriver().manage().logs().getAvailableLogTypes();
        if (isMobile() && logTypes.contains(org.openqa.selenium.logging.LogType.SERVER)) {
            LogEntries logEntries = getDriver().manage().logs().get(org.openqa.selenium.logging.LogType.SERVER);
            FileUtil.writeLogInFile(getResultContainer().getAppiumLogsFilePath(), logEntries.getAll());
        }
    }

    public static void captureBrowserLogs() {
        if (isDriverCreated && getConfigData().getCaptureDeviceLogs()) {
            Set<String> logTypes = getWebDriver().manage().logs().getAvailableLogTypes();
            if (isWeb() && logTypes.contains(org.openqa.selenium.logging.LogType.BROWSER)) {
                LogEntries logEntries = getWebDriver().manage().logs().get(org.openqa.selenium.logging.LogType.BROWSER);
                FileUtil.writeLogInFile(getResultContainer().getDeviceLogsFilePath(), logEntries.getAll());
            }
        }
    }

    private static void captureAndroidLogs() {
        // Util.writeInFile(deviceLogsFilePath,logClient.deviceLogStringBuilder.toString());
        Set<String> logTypes = getDriver().manage().logs().getAvailableLogTypes();
        if (isMobile() && logTypes.contains("logcat")) {
            LogEntries logEntries = getDriver().manage().logs().get("logcat");
            FileUtil.writeLogInFile(getResultContainer().getDeviceLogsFilePath(), logEntries.getAll());
        }
    }

    public static void initDeviceLogs() throws URISyntaxException {
        if (isIOS()) {
            /*logClient = new LogClient(new URI( "ws://"+getDriver().getRemoteAddress().getHost()+":"+getDriver().getRemoteAddress().getPort()+"/ws/session/" + getDriver().getSessionId() + "/appium/device/syslog"));
            getDriver().executeScript("mobile:startLogsBroadcast");
            logClient.connect();*/
            Set<String> logType = getDriver().manage().logs().getAvailableLogTypes();
            if (logType.contains("syslog")) {
                getDriver().manage().logs().get("syslog");
            }
            if (logType.contains("crashlog")) {
                getDriver().manage().logs().get("crashlog");
            }
        } else if (isAndroid()) {
           /* logClient = new LogClient(new URI( "ws://"+getDriver().getRemoteAddress().getHost()+":"+getDriver().getRemoteAddress().getPort()+"/ws/session/" + getDriver().getSessionId() + "/appium/device/logcat"));
            getDriver().executeScript("mobile:startLogsBroadcast");
            logClient.connect();*/
            Set<String> logType = getDriver().manage().logs().getAvailableLogTypes();
            if (logType.contains("logcat")) {
                getDriver().manage().logs().get("logcat");
            }
        } else if (isWeb()) {
            getWebDriver().manage().logs().get(org.openqa.selenium.logging.LogType.BROWSER);
        }
    }

    private static void captureIosLogs() {
        if (getConfigData().getCaptureDeviceLogs() && isIOS()) {
           /* Util.writeInFile(deviceLogsFilePath,logClient.deviceLogStringBuilder.toString());
            if(logClient.errorLogStringBuilder.length()!=0){
                Util.writeInFile(iosCrashLogsFilePath,logClient.errorLogStringBuilder.toString());
            }*/
            Set<String> logTypes = getDriver().manage().logs().getAvailableLogTypes();
            if (logTypes.contains("syslog")) {
                LogEntries logEntries = getDriver().manage().logs().get("syslog");
                FileUtil.writeLogInFile(getResultContainer().getDeviceLogsFilePath(), logEntries.getAll());
            }
            if (logTypes.contains("crashlog")) {
                LogEntries crashLogs = getDriver().manage().logs().get("crashlog");
                if (!crashLogs.getAll().isEmpty()) {
                    FileUtil.writeLogInFile(getResultContainer().getIosCrashLogsFilePath(), crashLogs.getAll());
                }
            }
        }
    }

    public static void captureFailedTestScreenShot(int testResult) {
        if (2 == testResult) {
            try {
                if (getWebDriver()!= null || getDriver() != null) {
                    captureScreenshot();
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "", e);
            }
        }
    }

   /* void getRecodedMobileVideo() {
        if (isMobile() && isDriverCreated) {
            try {
                if (getConfigData().isRecordVideo()) {
                    String path = getUserDir() + "/" + getReportFileDir() + "/Videos/";
                    Util.createDirectory(path);
                    getResultContainer().setVideoPath(path + getTestCase().getTestMethodName() + Util.randomString(5) + ".mp4");
                    if (isAndroid()) {
                        Util.getDecoded(getAndroidDriver().stopRecordingScreen(), new File(getResultContainer().getVideoPath()));
                    } else if (isIOS()) {
                        Util.getDecoded(getIOSDriverDriver().stopRecordingScreen(), new File(getResultContainer().getVideoPath()));
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "", e);
            }
        }
    }*/
/*
    void stopWebVideoRecording() throws IOException {
        if (isDriverCreated && isWeb() && getConfigData().isRecordVideo()) {
            videoRecord.stopRecording();
        }
    }*/
/*
    void quitDriver() {
        if (isAndroid() || isIOS()) {
            if (getDriver() != null) {
                getDriver().quit();
            } else {
                LOGGER.log(Level.INFO, "Quitting Driver");
            }
        }
        if (isDriverCreated && isWeb()) {
            getWebDriver().quit();
        }
    }*/

  /*  *//**
     * Initialise Header data and Logger
     *
     * @throws IOException io exception
     *//*
    private void setDriver() throws IOException {
        if (isMobile()) {
            initialiseAppiumDriver();
        } else if (isWeb()) {
            initialiseWebDriver();
        }
        LOGGER.log(Level.INFO, "@Initializer : After Initialising Driver");
    }*/

  /*  *//**
     * Method to Initialize Appium driver
     *
     * @throws MalformedURLException : Throws Exception
     *//*
    private void initialiseAppiumDriver() throws MalformedURLException {
        setMobileAppiumDriver();
        getDriver().manage().timeouts().implicitlyWait(Constants.getImplicitTimeout(), TimeUnit.SECONDS);
    }*/

  /*  *//**
     * Method to Initialize Web driver
     *
     * @throws IOException: Throws Exception
     *//*
    private void initialiseWebDriver() throws IOException {
        setWebDriver(getExecutionContainer().getExecutionPlatform());
        LOGGER.log(Level.INFO, "Driver Initialized");
        if (!getExecutionContainer().getExecutionPlatform().equalsIgnoreCase(Constants.SAFARI)) {
            getWebDriver().manage().timeouts().implicitlyWait(Constants.getImplicitTimeout(), TimeUnit.SECONDS);
        }
    }*/
/*

    */
/**
     * Method to start Appium driver
     *
     * @throws MalformedURLException: Throws Exception
     *//*

    private void setMobileAppiumDriver() throws MalformedURLException {
        AppiumDriver appiumDriver;
        URL appiumURL = getConfigData().isGafCloud() ? HubUtil.getHubURL() : getAppiumUrl();
        if (getExecutionContainer().getExecutionPlatform().length() >= Constants.IOS_UDID_SIZE) {
            double osVersion = Double.parseDouble(deviceInfoMap.get(getExecutionContainer().getExecutionPlatform()).getDeviceVersion().substring(0, 3));
            LOGGER.log(Level.INFO, "IOS URL:: " + appiumURL);
            appiumDriver = new IOSDriver(appiumURL,
                    getIOSCapabilities(getExecutionContainer().getExecutionPlatform(), getConfigData().getBundleID(), osVersion,
                            getConfigData().getIsBrowserMobProxyRequired()));
            getExecutionContainer().setIosDriver((IOSDriver) appiumDriver);
        } else {
            LOGGER.log(Level.INFO, "Android URL:: " + getExecutionContainer().getExecutionPlatform() + appiumURL);
            appiumDriver = new AndroidDriver(appiumURL,
                    getAndroidCapabilities(getExecutionContainer().getExecutionPlatform(), getConfigData().getActivityName(),
                            getConfigData().getPackageName(), getConfigData().getIsBrowserMobProxyRequired()));
            getExecutionContainer().setAndroidDriver((AndroidDriver) appiumDriver);
        }
        getExecutionContainer().setDriver(appiumDriver);
    }
*/
/*
    private URL getAppiumUrl() throws MalformedURLException {
        return new URL(String.format("http://%s:%s/wd/hub", Util.getIPAddress(), Constants.getMobilePorts().get(getExecutionContainer().getExecutionPlatform())));
    }*/

/*    private RemoteWebDriver getRemoteWebDriver(URL hubURL, DesiredCapabilities browserCapabilitiesWithProxy, DesiredCapabilities browserCapabilities) {
        RemoteWebDriver localDriver;
        if (getConfigData().getIsBrowserMobProxyRequired()) {
            localDriver = new RemoteWebDriver(hubURL, browserCapabilitiesWithProxy);
        } else {
            localDriver = new RemoteWebDriver(hubURL, browserCapabilities);
        }
        return localDriver;
    }

    private RemoteWebDriver getRemoteWebDriver(URL hubURL, DesiredCapabilities browserCapabilitiesWithProxy, InternetExplorerOptions browserCapabilities) {
        RemoteWebDriver localDriver;
        if (getConfigData().getIsBrowserMobProxyRequired()) {
            localDriver = new RemoteWebDriver(hubURL, browserCapabilitiesWithProxy);
        } else {
            localDriver = new RemoteWebDriver(hubURL, browserCapabilities);
        }
        return localDriver;
    }*/

    protected void step(String step) {
        Steps steps = new Steps();
        int callersLineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        List<Steps> stepsList = TestExecutor.getTestCasesList().get(getExecutionContainer().getExecutionPlatform()).get(getTestCase().getClassName() + "." +
                getTestCase().getTestMethodName()).getSteps();
        for (Steps ele : stepsList) {
            if (ele.getLineNumber() == callersLineNumber) {
                steps.setLabel(ele.getLabel());
                steps.setLineNumber(ele.getLineNumber());
            }
        }
        steps.setStep(steps.getLabel() + ": " + step);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date());
        steps.setTimeStamp(currentDate);
        steps.setStatus("Pass");
        FrameworkLogger.logStep(step);
        getExecutionContainer().getLocalResult().getSteps().add(steps);
    }

    public static void reInitialiseAppiumServer() {
        if (!isDriverCreated && isMobile() && !getConfigData().isGafCloud()) {
            try {
                if (getConfigData().isAppiumStartedUsingServices()) {
                    AppiumManager.stopService(getExecutionContainer().getExecutionPlatform());
                    Thread.sleep(5000L);
                    if (isIOS()) {
                        //TODO Add condition for iosSafari
                        AppiumManager.appiumServerForIOS(getExecutionContainer().getExecutionPlatform(),
                                Integer.parseInt(Util.getAvailablePort()));
                    } else if (isAndroid()) {
                        AppiumManager.appiumServerForAndroid(getExecutionContainer().getExecutionPlatform(),
                                Integer.parseInt(Util.getAvailablePort()));
                    }
                } else {
                    GridInitializer.killMobileNode(getExecutionContainer().getExecutionPlatform());
                    String newport = Util.getAvailablePort();
                    GridInitializer.startAppiumNode(newport,getExecutionContainer().getExecutionPlatform());
                    Constants.getMobilePorts().put(getExecutionContainer().getExecutionPlatform(), Integer.valueOf(newport));
                }
            } catch (Exception ex) {
                LOGGER.log(Level.INFO, "Exception in restarting services");
                LOGGER.log(Level.WARNING, "", ex);
            }
        }
    }

    protected void initScreens() {
        LOGGER.log(Level.INFO, "Method Not Implemented");
    }

    protected void updateConfiguration() {
        LOGGER.log(Level.INFO, "Method Not Implemented");
    }

    public static void getNetworkLogs() {
        LOGGER.log(Level.INFO, "Capture Network Logs");

        if (getConfigData().isBrowserMobProxyRequired()) {
            try {
                BrowserProxyHelper.getHar().writeTo(new File(getResultContainer().getNetworkLogFilePath()));
                Thread.sleep(5000L);
            } catch (InterruptedException | IOException e) {
                FrameworkLogger.logError(e);
            }
        }
    }

   /* *//**
     * Method to initialize driver
     *//*
    void initDriver() {
        LOGGER.log(Level.INFO, "CLass Name:::::::::::" + this.getClass().getSimpleName());
        try {
            setDriver();
            isDriverCreated = true;
            LOGGER.log(Level.INFO, "@Initializer : Success");
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "@Initializer : Failed", e);
        }
    }*/
}
