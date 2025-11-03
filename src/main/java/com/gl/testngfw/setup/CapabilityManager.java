package com.gl.testngfw.setup;

import com.gl.testngfw.api.BrowserProxyHelper;
import com.gl.testngfw.common.Constants;
import com.gl.testngfw.enums.Browser;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.report.model.UserDevice;
import com.gl.testngfw.utility.CommandLineExecutor;
import com.gl.testngfw.utility.RandomUtils;
import com.gl.testngfw.utility.Util;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is class is to manage capabilities
 */
class CapabilityManager {
    private static final Logger LOGGER = Logger.getLogger(CapabilityManager.class.getName());
    static Map<String, UserDevice> deviceInfoMap;
    private static final String WINDOW_SIZE = "window-size=1200x600";
    /**
     * Method to set appium capabilities for Android
     */
    public static DesiredCapabilities getAndroidCapabilities(String deviceID, String activity, String packageName,
                                                             boolean isBrowserMobProxyRequired) {
        UserDevice device = deviceInfoMap.get(deviceID);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (isBrowserMobProxyRequired) {
            enableProxy(Util.getProxyPort(device.getDeviceId()));

            Proxy seleniumProxy = ClientUtil.createSeleniumProxy(BrowserProxyHelper.getProxy());
            capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        }
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Constants.ANDROID);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, device.getDeviceVersion());
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, device.getDeviceName());
        capabilities.setCapability(MobileCapabilityType.UDID, device.getDeviceId());
        if (Constants.getMobileBrowsers().containsKey(device.getDeviceId()) && Constants.getMobileBrowsers().get(device.getDeviceId())) {
            capabilities.setCapability(AndroidMobileCapabilityType.BROWSER_NAME, Constants.CHROME);
        }
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, packageName);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, activity);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, Constants.APPIUM_COMMAND_TIMEOUT_SEC);
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
        capabilities.setCapability(MobileCapabilityType.CLEAR_SYSTEM_FILES, true);
        if (InitializerScript.getConfigData().isGafCloud()) {
            capabilities.setCapability("appium:accessToken", Constants.getAuthToken());
        }
        return capabilities;
    }

    /**
     * Method to set appium capabilities for IOS
     *
     * @param deviceName : device name
     */
    public static DesiredCapabilities getIOSCapabilities(String deviceName, String bundleID, double osVersion,
                                                         boolean isBrowserMobProxyRequired) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (isBrowserMobProxyRequired) {
            enableProxy(Util.getProxyPort(deviceName));
            Proxy seleniumProxy = ClientUtil.createSeleniumProxy(BrowserProxyHelper.getProxy());
            capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        }
        // Set up desired capabilities and pass the IOS app-activity and app-package to Appium
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Constants.IOS);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, deviceInfoMap.get(deviceName).getDeviceVersion());
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, Constants.APPIUM_COMMAND_TIMEOUT_SEC);
        capabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID, bundleID);
        capabilities.setCapability(MobileCapabilityType.UDID, deviceName);
        if (Constants.getMobileBrowsers().containsKey(deviceName) && Constants.getMobileBrowsers().get(deviceName)) {
            startWebKitProxy(deviceName, Constants.WEBKIT_PROXY_PORTS.get(TestExecutor.getExecution().indexOf(deviceName)) + RandomUtils.randomBetween(1, 10));
            capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, Constants.SAFARI);
        }
        if (osVersion >= 9.3) {
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, Constants.XCUITEST);
            capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT,
                    Constants.getWdaPorts().get(deviceName));
            // capabilities.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, true);
            capabilities.setCapability(IOSMobileCapabilityType.WDA_STARTUP_RETRIES, 6);
            //capabilities.setCapability(IOSMobileCapabilityType.WDA_LAUNCH_TIMEOUT, 240000);
            //capabilities.setCapability(IOSMobileCapabilityType.WDA_CONNECTION_TIMEOUT, 240000);
            capabilities.setCapability(MobileCapabilityType.CLEAR_SYSTEM_FILES, true);
        }
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        if (InitializerScript.getConfigData().isGafCloud()) {
            capabilities.setCapability("appium:accessToken", Constants.getAuthToken());
        }
        return capabilities;
    }

    /**
     * Method to set Chrome capabilities
     */
    protected static DesiredCapabilities getChromeBrowserCapabilitiesWithProxy(int proxyPort) {
        System.setProperty("webdriver.chrome.driver", Util.getWebBinaryPath(Browser.CHROME));

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities = initProxy(capabilities);
        BrowserProxyHelper.getProxy().enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
        if (InitializerScript.getConfigData().isGafCloud()) {
            capabilities.setCapability("accessToken", Constants.getAuthToken());
        }
        return capabilities;
    }

    /**
     * Method to set firefox capabilities
     */
    protected static DesiredCapabilities getFirefoxBrowserCapabilitiesWithProxy(int proxyPort) {
        System.setProperty("webdriver.gecko.driver", Util.getWebBinaryPath(Browser.FIREFOX));
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities = initProxy(capabilities);
        if (InitializerScript.getConfigData().isGafCloud()) {
            capabilities.setCapability("accessToken", Constants.getAuthToken());
        }
        return capabilities;
    }

    /**
     * Method to set Safari capabilities
     */
    protected static DesiredCapabilities getSafariBrowserCapabilitiesWithProxy(int proxyPort) {

        DesiredCapabilities capabilities = DesiredCapabilities.safari();
        capabilities = initProxy(capabilities);
        if (InitializerScript.getConfigData().isGafCloud()) {
            capabilities.setCapability("accessToken", Constants.getAuthToken());
        }
        return capabilities;
    }

    /**
     * Method to set IE Browser capabilities
     */
    protected static DesiredCapabilities getIEBrowserCapabilitiesWithProxy(int proxyPort) {
        System.setProperty("webdriver.ie.driver", Util.getWebBinaryPath(Browser.IE));
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        capabilities = initProxy(capabilities);
        if (InitializerScript.getConfigData().isGafCloud()) {
            capabilities.setCapability("accessToken", Constants.getAuthToken());
        }
        return capabilities;
    }


    protected static DesiredCapabilities getEdgeBrowserCapabilitiesWithProxy(int proxyPort) {
        System.setProperty("webdriver.edge.driver", Util.getWebBinaryPath(Browser.EDGE));
        DesiredCapabilities capabilities = DesiredCapabilities.edge();
        initProxy(capabilities);

        if (InitializerScript.getConfigData().isGafCloud()) {
            capabilities.setCapability("accessToken", Constants.getAuthToken());
        }
        return capabilities;
    }



    /**
     * Method to set Chrome capabilities
     */
    public static DesiredCapabilities getChromeBrowserCapabilities() {
        WebDriverManager.chromedriver().setup();
        System.out.println("\n------------------------------- Chrome Driver Setup -----------------------\n");

        // Create ChromeOptions
        ChromeOptions chromeOptions = new ChromeOptions();

        // Add your Chrome options here (e.g., disable notifications, headless, etc.)
        chromeOptions.addArguments("--start-maximized");  // Example argument
        chromeOptions.addArguments("--disable-notifications");  // Example argument
        chromeOptions.addArguments("--ignore-certificate-errors");  // Ignore SSL errors

        // You can add headless conditionally if required
        // if (InitializerScript.getConfigData().isHeadlessMode()) {
        //     chromeOptions.addArguments("--headless");
        // }

        // Create DesiredCapabilities and merge ChromeOptions
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        // SSL certificates acceptance
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

        // Handle proxy if needed
        if (InitializerScript.getConfigData().isBrowserMobProxyRequired()) {
            capabilities = initProxy(capabilities);
        }

        // GAF cloud token
        if (InitializerScript.getConfigData().isGafCloud()) {
            capabilities.setCapability("accessToken", Constants.getAuthToken());
        }

        // Handle headless execution
        setHeadlessExecution(capabilities, "Chrome");

        return capabilities;
    }



    /**
     * Method to set firefox capabilities
     */
    public static DesiredCapabilities getFirefoxBrowserCapabilities() {
        //System.setProperty("webdriver.gecko.driver", Util.getWebBinaryPath(Browser.FIREFOX));
        WebDriverManager.firefoxdriver().setup();
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        if(InitializerScript.getConfigData().isBrowserMobProxyRequired()){
            capabilities = initProxy(capabilities);
        }
        if (InitializerScript.getConfigData().isGafCloud()) {
            capabilities.setCapability("accessToken", Constants.getAuthToken());
        }
        setHeadlessExecution(capabilities, "FireFox");
        /*setHeadlessExecution(capabilities);*/
        return capabilities;
    }

    /**
     * Method to set Safari capabilities
     */
    public static DesiredCapabilities getSafariBrowserCapabilities() {
        DesiredCapabilities capabilities = DesiredCapabilities.safari();
        if(InitializerScript.getConfigData().isBrowserMobProxyRequired()){
            capabilities = initProxy(capabilities);
        }
        if (InitializerScript.getConfigData().isGafCloud()) {
            capabilities.setCapability("accessToken", Constants.getAuthToken());
        }
        return capabilities;
    }

    /**
     * Method to set IE Browser capabilities
     */
    public static InternetExplorerOptions getIEBrowserCapabilities() {
        //System.setProperty("webdriver.ie.driver", Util.getWebBinaryPath(Browser.IE));
        WebDriverManager.iedriver().setup();
        InternetExplorerOptions ieOptions = new InternetExplorerOptions();
        ieOptions.introduceFlakinessByIgnoringSecurityDomains();
        ieOptions.ignoreZoomSettings();
        ieOptions.setCapability("requireWindowFocus", true);
        ieOptions.setCapability("ie.ensureCleanSession", true);
        ieOptions.withInitialBrowserUrl("google.com");
        if(InitializerScript.getConfigData().isBrowserMobProxyRequired()){
            enableProxy(getProxyPort());
            Proxy seleniumProxy= ClientUtil.createSeleniumProxy(BrowserProxyHelper.getProxy());
            ieOptions.setCapability(CapabilityType.PROXY,seleniumProxy);
        }
        if (InitializerScript.getConfigData().isGafCloud()) {
            ieOptions.setCapability("accessToken", Constants.getAuthToken());
        }
        return ieOptions;
    }

    public static DesiredCapabilities getEdgeBrowserCapabilities() {
        //System.setProperty("webdriver.edge.driver", Util.getWebBinaryPath(Browser.EDGE));
        WebDriverManager.edgedriver().setup();
        DesiredCapabilities capabilities = DesiredCapabilities.edge();
        if(InitializerScript.getConfigData().isBrowserMobProxyRequired()){
            capabilities = initProxy(capabilities);
        }
        if (InitializerScript.getConfigData().isGafCloud()) {
            capabilities.setCapability("accessToken", Constants.getAuthToken());
        }
        return capabilities;
    }

    private static int enableProxy(int port) {
        if (InitializerScript.getConfigData().getIsBrowserMobProxyRequired()) {
            try {
//              NetworkLogCaptureHelper.startProxy(Util.getProxyPort(deviceName));
                BrowserProxyHelper.start(port);
                BrowserProxyHelper.useDefaultSSLCertificate();
                return BrowserProxyHelper.getPort();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "", e);
            }
        }
        return 0;
    }
    @NotNull
    private static DesiredCapabilities initProxy(DesiredCapabilities capabilities) {
        try {
            //            NetworkLogCaptureHelper.startProxy(proxyPort);
            enableProxy(getProxyPort());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "", e);
        }
       /* BrowserProxyHelper.start(Constants.BROWSER_PROXY_PORTS.get(indexOfBrowser), Util.getLocalHost());
        BrowserProxyHelper.setHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.REQUEST_HEADERS,
                CaptureType.RESPONSE_CONTENT, CaptureType.RESPONSE_HEADERS);*/
        // get the Selenium proxy object
        Proxy seleniumProxy= ClientUtil.createSeleniumProxy(BrowserProxyHelper.getProxy());
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        return capabilities;
    }
    private static void startWebKitProxy(String deviceId, Integer port) {
        CommandLineExecutor.executeCommand(String.format("./bin/ios-webkit-debug-proxy-launcher.js -c %s:%s -d", deviceId, port));
        Util.waitTillAllServerIsLaunched(String.valueOf(port), 60000L);
    }
    private static int getProxyPort() {
        int currentProxyPort;
        if (InitializerScript.isWeb()) {
            currentProxyPort = Integer.parseInt(Util.getAvailablePort());
        } else {
            currentProxyPort = Util.getProxyPort(InitializerScript.getExecutionContainer().getExecutionPlatform());
        }
        Constants.getBrowserProxyPorts().put(InitializerScript.getCurrentPort(), currentProxyPort);
        return currentProxyPort;
    }
//Old Method
   /* private static void setHeadlessExecution(DesiredCapabilities capabilities) {
        if(InitializerScript.getConfigData().isHeadless()) {
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(true);
            options.addArguments("window-size=1200x600");
            capabilities.merge(options);
        }
    }*/
    private static void setHeadlessExecution(DesiredCapabilities capabilities, String browserName) {
        if (BaseInitializer.getConfigData().isHeadless()) {
            switch (browserName.toUpperCase()) {
                case "CHROME":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setHeadless(true);
                    chromeOptions.addArguments(WINDOW_SIZE);
                    capabilities.merge(chromeOptions);
                    break;
                case "FIREFOX":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setHeadless(true);
                    firefoxOptions.addArguments(WINDOW_SIZE);
                    capabilities.merge(firefoxOptions);
                    break;
                default:
                    break;
            }
        }
    }
}
