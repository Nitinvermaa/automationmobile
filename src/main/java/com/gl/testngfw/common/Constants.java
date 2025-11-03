package com.gl.testngfw.common;

import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.utility.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Constants class.
 */
public class Constants {
    public static final String SNAPSHOT_PATH = "/Snapshots/";
    public static final String VIDEO_PATH = "/Videos/";
    public static final String LOG_PATH = "/Logs/";
    public static final String EXECUTION_RESULT_PATH = "/ExecutionResult/";
    public static final String ALL_TESTS = "All Tests";
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String ANDROID = "Android";
    public static final String IOS = "iOS";
    public static final String CHROME = "chrome";
    public static final String SAFARI = "safari";
    public static final String IOS_SAFARI = "ios_Safari";
    public static final String ANDROID_CHROME = "android_Chrome";
    public static final String LOAD_TESTING = "loadTesting";
    public static final String API = "API";
    public static final String STB = "STB";
    public static final String ROKU = "ROKU";
    public static final String PERFORMANCE = "Performance";
    public static final String CONTRACT = "Contract";
    public static final String FIREFOX = "firefox";
    public static final String MAC = "MAC";
    public static final String WINDOWS = "Windows";
    public static final String EXCEL_FILE = "suiteExcelFilePath";
    public static final String MOBILE = "Mobile";
    public static final String WEB = "Web";
    public static final String IE = "ie";
    public static final String EDGE = "edge";
    public static final String INTERNET_EXPLORER = "internet explorer";
    public static final String MICROSOFT_EDGE = "MicrosoftEdge";
    public static final int IOS_UDID_SIZE = 40;
    public static final String SERVER_JAR_URL = "http://selenium-release.storage.googleapis.com/3.13/selenium-server-standalone-3.13.0.jar";
    public static final String SERVER_VER = "3.13.0";
    public static final String PROJECT_PROPERTIES = "Project.Properties";
    public static final String PROJECT_CONFIG_PROPERTIES = "ProjectConfig.properties";
    public static final String MAIL_CONFIG_PROPERTIES = "MailConfig.properties";
    public static final String PENDING = "pending";
    public static final String COMPLETED = "completed";
    public static final String IN_PROGRESS = "inProgress";
    public static final String CLOUD_URL = "http://172.16.16.209:8280/wd/hub";
    public static final String GIT_URL = "git@del.globallogic.com:gaf-console/gaf-dashboard.git - b DBIntegrationUpdate";
    public static final String PARALLEL = "Parallel";
    public static final String DISTRIBUTED = "distributed";
    public static final String XCUITEST = "XCUITest";
    public static final Map<String, Thread> ThreadMap = new HashMap<>();
    public static final List<Integer> WDA_PORT_LIST = IntStream.rangeClosed(8110, 8210).boxed().collect(Collectors.toList());
    public static final List<Integer> WEBKIT_PROXY_PORTS = IntStream.rangeClosed(27753, 27800).boxed().collect(Collectors.toList());
    public static final List<Integer> Android_SystemPorts = IntStream.rangeClosed(8201, 8299).boxed().collect(Collectors.toList());
    public static final List<String> REPORT_PATH = new ArrayList<String>();
    public static String APPIUM_PATH = System.getenv("APPIUM_HOME");
    public static int APPIUM_COMMAND_TIMEOUT_SEC = 160;
    public static String LOG_LEVEL = "info";
    public static Long COMMAND_EXECUTOR_TIMEOUT = 80 * 1000L;
    private static String EXECUTION_ID;
    private static String PROJECT_ID;
    private static int IMPLICIT_TIMEOUT = 120;
    private static String AUTH_TOKEN = "";
    private static boolean isTestGrouped = false;
    private static boolean isHubRunning = false;
    private static String GRID_HUB_URL = "http://127.0.0.1:4444/wd/hub";
    private static String APPIUM_URL = "http://127.0.0.1:4444/wd/hub";
    private static Map<String, Integer> BROWSER_PORTS = new HashMap<>();
    private static Map<Integer, Integer> BROWSER_PROXY_PORTS = new HashMap<>();
    private static Map<String, Integer> WDA_PORTS = new HashMap<>();
    private static Map<String, Integer> PROCESS_PORTS = new HashMap<>();
    private static Map<String, Integer> MOBILE_PORTS = new HashMap<>();
    private static Map<String, String> HUB_DETAILS = new HashMap<>();
    private static Map<String, Boolean> MOBILE_BROWSERS = new HashMap<>();
    private static String LOCAL_IP = Util.getIPAddress(); // "127.0.0.1";
    private static String SUITE_NAME = TestExecutor.getConfigData().getTestSuiteName();
    public static String FALSE = "false";
    public static String TRUE = "true";
    public static String NULL = "null";



    private Constants() {
    }


    public static String getExecutionId() {
        return EXECUTION_ID;
    }

    public static void setExecutionId(String executionId) {
        EXECUTION_ID = executionId;
    }

    public static String getSuiteName() {
        return SUITE_NAME;
    }

    public static void setSuiteName(String suiteName) {
        SUITE_NAME = suiteName;
    }

    public static int getImplicitTimeout() {
        return IMPLICIT_TIMEOUT;
    }

    public static void setImplicitTimeout(int implicitTimeout) {
        IMPLICIT_TIMEOUT = implicitTimeout;
    }

    public static Map<String, Integer> getBrowserPorts() {
        return BROWSER_PORTS;
    }

    public static Map<Integer, Integer> getBrowserProxyPorts() {
        return BROWSER_PROXY_PORTS;
    }

    public static Map<String, Integer> getWdaPorts() {
        return WDA_PORTS;
    }

    public static Map<String, Integer> getMobilePorts() {
        return MOBILE_PORTS;
    }

    public static Map<String, String> getHubDetails() {
        return HUB_DETAILS;
    }

    public static Map<String, Boolean> getMobileBrowsers() {
        return MOBILE_BROWSERS;
    }

    public static String getAuthToken() {
        return AUTH_TOKEN;
    }

    public static void setAuthToken(String authToken) {
        AUTH_TOKEN = authToken;
    }

    public static String getGridHubUrl() {
        return GRID_HUB_URL;
    }

    public static void setGridHubUrl(String gridHubUrl) {
        GRID_HUB_URL = gridHubUrl;
    }

    public static String getAppiumUrl() {
        return APPIUM_URL;
    }

    public static void setAppiumUrl(String appiumUrl) {
        APPIUM_URL = appiumUrl;
    }

    public static String getLocalIp() {
        return LOCAL_IP;
    }

    public static boolean isIsTestGrouped() {
        return isTestGrouped;
    }

    public static void setIsTestGrouped(boolean isTestGrouped) {
        Constants.isTestGrouped = isTestGrouped;
    }

    public static boolean isIsHubRunning() {
        return isHubRunning;
    }

    public static void setIsHubRunning(boolean isHubRunning) {
        Constants.isHubRunning = isHubRunning;
    }

    public static int getAppiumCommandTimeoutSec() {
        return APPIUM_COMMAND_TIMEOUT_SEC;
    }

    public static void setAppiumCommandTimeoutSec(int appiumCommandTimeoutSec) {
        APPIUM_COMMAND_TIMEOUT_SEC = appiumCommandTimeoutSec;
    }

    public static String getLogLevel() {
        return LOG_LEVEL;
    }

    public static void setLogLevel(String logLevel) {
        LOG_LEVEL = logLevel;
    }

    public static String getProjectId() {
        return PROJECT_ID;
    }

    public static void setProjectId(String projectId) {
        PROJECT_ID = projectId;
    }

    public static Map<String, Integer> getProcessPorts() {
        return PROCESS_PORTS;
    }

    public static void setProcessPorts(int processPorts) {
        PROCESS_PORTS.put("API", processPorts);
    }
}
