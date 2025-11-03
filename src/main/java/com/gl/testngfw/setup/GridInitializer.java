package com.gl.testngfw.setup;

import com.gl.testngfw.common.Constants;
import com.gl.testngfw.enums.Browser;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.logging.ServerNotStarted;
import com.gl.testngfw.report.model.ConfigData;
import com.gl.testngfw.utility.CommandLineExecutor;
import com.gl.testngfw.utility.FileUtil;
import com.gl.testngfw.utility.Util;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Start Initialize  Grid server
 */
public class GridInitializer {
    private static final Logger LOGGER = Logger.getLogger(GridInitializer.class.getName());
    private static String osName = System.getProperty("os.name");
    private static ConfigData configData = TestExecutor.getConfigData();

    private GridInitializer() {
    }

    /**
     * Start Grid server
     */
    public synchronized static void startServer() throws IOException {
        if (configData.getExecuteFor().contains(Platforms.WEB) || configData.getExecuteFor().contains(Platforms.MOBILE)) {
            HubUtil.startCommandLineGrid();
        }

        if (configData.getExecuteFor().contains(Platforms.MOBILE)) {
            Util.createDirectory(System.getProperty("user.dir") + "/Reports/AppiumLogs");
            if (TestExecutor.getDeviceInfoMap().size() != 0) {
                for (String key : TestExecutor.getDeviceInfoMap().keySet()) {
                    registerMobileNode(key);
                }
            }
        }
        registerWebNode();
        TestExecutor.setIsGridStarted(true);
    }

    private static void registerMobileNode(String key) throws IOException {
        if (configData.isAppiumStartedUsingServices()) {
            int port = Integer.parseInt(Util.getAvailablePort());
            AppiumManager.writeNodeConfigFile(HubUtil.getHubURL().getHost(),
                    HubUtil.getHubURL().getPort(),
                    Constants.getLocalIp(), port, TestExecutor.getDeviceInfoMap().get(key));
            if (osName.contains("Windows")) {
                AppiumManager.appiumServerForAndroid(key, port);
            } else {
                if (key.length() >= 40) {
                    if (configData.getIosWebkitProxyPath().isEmpty()) {
                        AppiumManager.appiumServerForIOS(key, port);
                    }
                } else {
                    AppiumManager.appiumServerForAndroid(key, port);
                }
            }
        } else {
            String availablePort = Util.getAvailablePort();
            startAppiumNode(availablePort, key);
        }
    }

    private static void registerWebNode() throws IOException {
        if (configData.getExecuteFor().contains(Platforms.WEB)) {
            String iPAddress = Constants.getLocalIp();
            for (int i = 0; i < configData.getBrowserList().size(); i++) {
                if (configData.isAppiumStartedUsingServices()) {
                    NodeUtil.registerWebGridNode(configData.getBrowserList().get(i));
                } else {
                    String availablePort = Util.getAvailablePort();
                    registerWebGridNode(iPAddress, configData.getBrowserList().get(i), availablePort, configData.getInstances());
                }
            }
        }
    }

    public static void startCommandLineGrid() throws IOException {
        String driverPath = WebDriverManager.chromedriver().getDownloadedDriverPath();
        String availablePort = Util.getAvailablePort();
        String iPAddress = Constants.getLocalIp();

        System.out.println("------------------------------- Selenium Hub --------------------------");

        String seleniumJar = FileUtil.getFilePathFromResourcePath("selenium-server-standalone-3.141.59.jar");
        System.out.println("Selenium Jar Path:::: "+seleniumJar);
        String command = String.format
                ("java -jar %s -role hub -host %s -port %s", seleniumJar, Constants.getLocalIp(), availablePort);

        Util.executeCommand(command);
        Constants.getHubDetails().put(iPAddress, availablePort);
        Constants.setGridHubUrl(String.format("http://%s:%s/wd/hub", iPAddress, availablePort));
        waitTillAllServerIsLaunched(availablePort);
    }

    public static void registerWebGridNode(String hubIp, Browser browser, String port, int instances) {
        String driverPath = Util.getWebDriverBinaryPath(browser);
        System.out.println("-----------------------------Driver path------------------------------------------------\n");

        System.out.println("DriverPath::::: "+driverPath);

        String seleniumJar = FileUtil.getFilePathFromResourcePath("selenium-server-standalone-3.141.59.jar");
        System.out.println("Selenium Jar Path:::: "+seleniumJar);
        //String command = String.format("java %s -jar %s -role node " +
        //                "-hub http://%s:%s/grid/register -port %s -browser browserName=\"%s\"," +
        //                "maxInstances=%s -timeout 10000", driverPath, seleniumJar, hubIp,
        //        Constants.getHubDetails().get(hubIp), port, browser.getValue(), instances);

        String command = String.format(
                "java -Dwebdriver.chrome.driver=%s -jar %s -role node -hub http://%s:%s/grid/register -port %s -browser browserName=\"%s\",maxInstances=%s -timeout 10000",
                driverPath, seleniumJar, hubIp, Constants.getHubDetails().get(hubIp), port, browser.getValue(), instances
        );

        Util.executeCommand(command);
        waitTillAllServerIsLaunched(port);
        Constants.getBrowserPorts().put(browser.name(), Integer.valueOf(port));
    }


    public synchronized static void startAppiumNode(String port, String deviceID) throws IOException {
        String command;
        String chromeDriver = null;
        chromeDriver = Util.getAvailablePort();
        String bootStrap = Util.getAvailablePort();
        Util.createDirectory("Reports/toolLogs");
        if (System.getProperty("os.name").contains("Windows")) {
            command = String.format(Constants.APPIUM_PATH + "/appium" + " --udid %s -p %s  --bootstrap-port %s  --chromedriver-port %s > Reports/toolLogs/%s_appium.Log", deviceID, port,
                    bootStrap, chromeDriver, deviceID);
        } else {
            if (deviceID.length() >= 40) {
                if(Constants.APPIUM_PATH==null){
                    Constants.APPIUM_PATH = "/usr/local/bin/appium";
                }
                Constants.getWdaPorts().put(deviceID, Integer.valueOf(Util.getAvailablePort()));
                String tempFolderName = String.valueOf(new File(System.getProperty("user.dir") +
                        "/target/" + "tmp_" + deviceID));

                command = String.format(Constants.APPIUM_PATH + " --address %s --udid %s --port %s  --chromedriver-port %s  " +
                                "--bootstrap-port %s  --tmp /tmp/%s > Reports/toolLogs/%s_appium.Log", Util.getIPAddress(),
                        deviceID, port, chromeDriver, bootStrap, tempFolderName, deviceID);
            } else {
                if(Constants.APPIUM_PATH==null){
                    Constants.APPIUM_PATH = "/usr/local/bin/appium";
                }
                command = String.format(Constants.APPIUM_PATH + " --address %s --udid %s --port %s --chromedriver-port %s --bootstrap-port %s > Reports/toolLogs/%s_appium.Log",
                        Util.getIPAddress(), deviceID, port, chromeDriver, bootStrap, deviceID);
            }
        }
        Util.executeCommand(command);
        Constants.setAppiumUrl(String.format("http://%s:%s/wd/hub", Util.getIPAddress(), port));
        waitTillAllServerIsLaunched(port);
        LOGGER.log(Level.INFO, String.format("-------Device %s Port %s", deviceID, port));
        LOGGER.log(Level.INFO, Constants.getAppiumUrl());
        Constants.getMobilePorts().put(deviceID, Integer.valueOf(port));

    }

    /**
     * Method to wait till server is launched
     */
    public static void waitTillAllServerIsLaunched(String port) {
        long startTime = System.currentTimeMillis();
        String command;
        try {
            while (true) {
                List<String> list;
                Thread.sleep(3000L);
                if (osName.contains("Windows")) {
                    command = "netstat -anp tcp | findstr " + port;
                    list = Util.runProcess(command);
                } else {
                    command = "netstat -anp tcp | grep " + port;
                    list = CommandLineExecutor.runProcess(command);
                }

                if (list != null && !list.isEmpty() && (list.get(0).contains("LISTENING") || list.get(0).contains("LISTEN"))) {
                    LOGGER.log(Level.INFO, "Server wait Time " + (System.currentTimeMillis() - startTime));
                    break;
                }
                if ((System.currentTimeMillis() - startTime) > 50000L) {
                    throw new ServerNotStarted("Server Not Started on Given Time");
                }
            }
        } catch (InterruptedException | ServerNotStarted e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    static void killMobileNode(String device) {
        for (String each : getPIDByPort(String.valueOf(Constants.getMobilePorts().get(device)))) {
           try {
               if (!System.getProperty("os.name").contains("Windows")) {
                   CommandLineExecutor.executeCommand("kill " + each);

               } else {
                   Util.executeCommand(1, String.format("taskkill /pid %s /F", each));
               }
           }catch (Exception e){
               LOGGER.log(Level.WARNING, e.getMessage());
           }
        }
    }

    private static List<String> getPIDByPort(String port) {
        List<String> list = new ArrayList<>();
        List<String> pidList = new ArrayList<>();
        String pid = "";
        if (!System.getProperty("os.name").contains("Windows")) {
            list = CommandLineExecutor.runProcess("lsof -t -i :" + port);
        } else {
            list = Util.runProcess("netstat -aon | findstr " + port);
        }
        if (list != null && !list.isEmpty()) {
            for (String each : list) {
                if (each.contains(" ")) {
                    pid = each.substring(each.lastIndexOf(" ")).trim();
                }
                if (!pidList.contains(pid)) {
                    pidList.add(pid);
                }
            }
        }
        return pidList;
    }

    public static void killWebNode(String port) {
        for (String each : getPIDByPort(port)) {
            try {
                if (!System.getProperty("os.name").contains("Windows")) {
                    CommandLineExecutor.executeCommand("kill " + each);
                } else {
                    if (!"0".equals(each)) {
                        Util.executeCommand(1, String.format("taskkill /pid %s /F", each));
                    }
                }
            }catch (Exception e){
                LOGGER.log(Level.WARNING, e.getMessage());
            }
        }
    }
    public static void killAPINode() {
        if(!Constants.getProcessPorts().isEmpty()){

        for (String each : getPIDByPort(String.valueOf(Constants.getProcessPorts().get("API")))) {
            try {
                if (!System.getProperty("os.name").contains("Windows")) {
                    CommandLineExecutor.executeCommand("kill " + each);
                } else {
                    if (!"0".equals(each)) {
                        Util.executeCommand(1, String.format("taskkill /pid %s /F", each));
                    }
                }
            }catch (Exception e){
                LOGGER.log(Level.WARNING, e.getMessage());
            }
        }
        }
    }

    private static void killAllMobileNode() {
        for (String device : Constants.getMobilePorts().keySet()) {
            killMobileNode(device);
        }
    }

    private static void killAllBrowserNode() {
        for (String browser : Constants.getBrowserPorts().keySet()) {
            killWebNode(String.valueOf(Constants.getBrowserPorts().get(browser)));
            if ("chrome".equalsIgnoreCase(browser)) {
                try {
                    Util.executeCommand(1, "taskkill /F /IM chromedriver.exe /T");
                }catch (Exception e){
                    LOGGER.log(Level.WARNING, e.getMessage());
                }
            }
        }
    }

    public static void killAllHub() {
        try {
            if (!configData.isGafCloud()) {
                for (String port : Constants.getHubDetails().values()) {
                    killWebNode(port);
                }
            }
            Util.clearSeleniumProcess();
        }catch (Exception e){
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    public static void shutDownGrid() {
        try {
            if (!configData.isAppiumStartedUsingServices()) {
                killAllMobileNode();
            }
            killAPINode();
            killAllHub();
            killAllBrowserNode();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    private static int startIOSWebKit(String udid, int proxyPort) {
        executeCommandInNewThread(String.valueOf(proxyPort), configData.getIosWebkitProxyPath() + " -c " + udid + ":" + proxyPort + " -d");
        return proxyPort;
    }

    private static void executeCommandInNewThread(String port, String command) {
        Thread thread = new Thread(() -> CommandLineExecutor.executeCommand(command));
        thread.start();
        Constants.ThreadMap.put(port, thread);
    }
}