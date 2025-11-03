package com.gl.testngfw.setup;

import com.gl.testngfw.common.Constants;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.report.model.UserDevice;
import com.gl.testngfw.utility.RandomUtils;
import com.gl.testngfw.utility.Util;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.service.local.flags.IOSServerFlag;
import io.appium.java_client.service.local.flags.ServerArgument;
import org.json.simple.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Appium Manager - this class contains method to start and stops appium server
 * To execute the tests from eclipse, you need to set PATH as
 * /usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin in run executableFor
 */
class AppiumManager {
    private static final Logger LOGGER = Logger.getLogger(AppiumManager.class.getName());

    private static Map<String, AppiumDriverLocalService> appiumServiceMap = new HashMap<>();
    private static Map<String, String> webKitPortMap = new HashMap<>();
    private static List<String> deviceList = new ArrayList<>(TestExecutor.getDeviceInfoMap().keySet());
    /**
     * start appium with auto generated ports : appium port, chrome port,
     * bootstrap port and device UDID
     */
    ServerArgument webKitProxy = new ServerArgument() {
        @Override
        public String getArgument() {
            return "--webkit-debug-proxy-port";
        }
    };

    /**
     * Start appium with auto generated ports : appium port, chrome port,
     * bootstrap port and device UDID
     */
    public static void appiumServerForAndroid(String deviceID, int nodePort) throws IOException {
        String nodeConfigPath = new File("nodeConfig").getAbsolutePath() + ".json";
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, Constants.Android_SystemPorts.get(deviceList.indexOf(deviceID) + RandomUtils.randomBetween(1, 20)));
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress(Constants.getLocalIp())
                .withArgument(AndroidServerFlag.CHROME_DRIVER_PORT, Util.getAvailablePort())
                .withArgument(GeneralServerFlag.CONFIGURATION_FILE, nodeConfigPath)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withCapabilities(capabilities)
                .withArgument(GeneralServerFlag.LOG_LEVEL, Constants.getLogLevel())
                .withArgument(GeneralServerFlag.LOG_TIMESTAMP)
                .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                .withLogFile(new File(System.getProperty("user.dir") + "/Reports/AppiumLogs/" + deviceID + ".txt"))
                .usingPort(nodePort);
        AppiumDriverLocalService appiumDriverLocalService = builder.build();
        appiumDriverLocalService.start();
        Constants.getMobilePorts().put(deviceID, appiumDriverLocalService.getUrl().getPort());
        appiumServiceMap.put(deviceID, appiumDriverLocalService);
    }

    public static void stopService(String device) throws InterruptedException {
        appiumServiceMap.get(device).stop();

        Thread.sleep(2000);
        if (appiumServiceMap.get(device).isRunning()) {
            LOGGER.log(Level.INFO, "AppiumServer didn't ShutDown... Trying to quit again....");
            GridInitializer.killMobileNode(device);
        }
    }

    /**
     * Method to Start Appium service for iOS device
     *
     * @param device     ios device
     * @param port       node port
     * @param webKitPort webkit port
     */
    public static void appiumServerForIOS(String device, int port, int webKitPort) {
        String nodeConfigPath = new File("nodeConfig").getAbsolutePath();
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withArgument(IOSServerFlag.WEBKIT_DEBUG_PROXY_PORT, String.valueOf(webKitPort))
                .withArgument(GeneralServerFlag.LOG_LEVEL, Constants.getLogLevel())
                .withArgument(GeneralServerFlag.CONFIGURATION_FILE, nodeConfigPath)
                .withArgument(GeneralServerFlag.TEMP_DIRECTORY,
                        String.valueOf(new File(System.getProperty("user.dir") + "/target/" + "tmp_" + device)))
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withLogFile(new File(System.getProperty("user.dir") + "/Reports/AppiumLogs/" + device + ".txt"))
                .usingPort(port);
        AppiumDriverLocalService appiumDriverLocalService = builder.build();
        appiumDriverLocalService.start();
        Constants.getMobilePorts().put(device, appiumDriverLocalService.getUrl().getPort());
        appiumServiceMap.put(device, appiumDriverLocalService);
    }

    /**
     * Method to Start Appium service for iOS device
     *
     * @param device   iOS device
     * @param nodePort appium port
     */
    public static void appiumServerForIOS(String device, int nodePort) {
        String nodeConfigPath = new File("nodeConfig").getAbsolutePath();
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withArgument(GeneralServerFlag.LOG_LEVEL, Constants.getLogLevel())
                .withArgument(GeneralServerFlag.CONFIGURATION_FILE, nodeConfigPath)
                .withArgument(GeneralServerFlag.TEMP_DIRECTORY,
                        String.valueOf(new File(System.getProperty("user.dir") + "/target/" + "tmp_" + device)))
                .withLogFile(new File(System.getProperty("user.dir") + "/Reports/AppiumLogs/" + device + ".txt"))
                .withArgument(GeneralServerFlag.LOG_TIMESTAMP)
                .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                .usingPort(nodePort);
        AppiumDriverLocalService appiumDriverLocalService = builder.build();
        appiumDriverLocalService.start();
        Constants.getMobilePorts().put(device, appiumDriverLocalService.getUrl().getPort());
        Constants.getWdaPorts().put(device, Constants.WDA_PORT_LIST.get(deviceList.indexOf(device) + RandomUtils.randomBetween(1, 20)));
        appiumServiceMap.put(device, appiumDriverLocalService);
    }

    /**
     * Method to generate node config file
     */
    public static void writeNodeConfigFile(String gridIP, int gridPort, String nodeIP, int nodePort, UserDevice device) {
        String platform = Constants.ANDROID.equals(device.getDeviceOS()) ? "android" : "Mac";
        try {
            JSONObject nodeConfig = new JSONObject();
            Map<String, Object> configuration = new HashMap<>();
            configuration.put("cleanUpCycle", 2000);
            configuration.put("timeout", 30000);
            configuration.put("proxy", "org.openqa.grid.selenium.proxy.DefaultRemoteProxy");
            configuration.put("host", nodeIP);
            configuration.put("port", nodePort);
            configuration.put("maxSession", 1);
            configuration.put("register", true);
            configuration.put("registerCycle", 5000);
            configuration.put("hubPort", gridPort);
            configuration.put("hubHost", gridIP);

            Map<String, Object> capability = new HashMap<>();
            capability.put("browserName", device.getDeviceName());
            capability.put("version", device.getDeviceVersion());
            capability.put("maxInstances", 1);
            capability.put("platform", platform);
            capability.put("udid", device.getDeviceId());

            List<Object> list = new ArrayList<>();
            list.add(capability);
            nodeConfig.put("capabilities", list);
            nodeConfig.put("configuration", configuration);

            FileWriter fw = new FileWriter("nodeConfig.json");
            fw.write(nodeConfig.toJSONString());
            fw.flush();
            fw.close();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "", ex);
        }
    }

    /**
     * Method to close IOSWebKitProxy service
     */
    public static void destroyIOSWebKitProxy() {
        for (String device : webKitPortMap.keySet()) {
            GridInitializer.killMobileNode(webKitPortMap.get(device));
        }
    }

    public URL getAppiumURL(String device) {
        return appiumServiceMap.get(device).getUrl();
    }

    /**
     * Method to close Appium Service
     */
    public int getMobileDriverPort(String device) {
        return appiumServiceMap.get(device).getUrl().getPort();

    }

    public URL getAppiumUrl(String device) {
        return appiumServiceMap.get(device).getUrl();
    }
}
