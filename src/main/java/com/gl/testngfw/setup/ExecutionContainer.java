package com.gl.testngfw.setup;

import com.gl.testngfw.api.ConsumerClient;
import com.gl.testngfw.api.RestAPIExtension;
import com.gl.testngfw.api.StormTest;
import com.gl.testngfw.enums.ExecutorType;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.report.model.ConfigData;
import com.gl.testngfw.report.model.Execution;
import com.gl.testngfw.report.model.Result;
import com.gl.testngfw.report.model.TestContext;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ExecutionContainer {
    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<RemoteWebDriver> webDriver = new ThreadLocal<>();
    private static ThreadLocal<RestAPIExtension> apiDriver = new ThreadLocal<>();
    private static ThreadLocal<StormTest> stormTestDriver = new ThreadLocal<>();
    private static ThreadLocal<ConsumerClient> contractDriver = new ThreadLocal<>();
    private static ThreadLocal<AndroidDriver> androidDriver = new ThreadLocal<>();
    private static ThreadLocal<IOSDriver> iosDriver = new ThreadLocal<>();
    private static ThreadLocal<Result> ThreadLocalResult = new ThreadLocal<>();
    private static ThreadLocal<String> typeOfExecution = new ThreadLocal<>();
    private static ThreadLocal<String> executionPlatform = new ThreadLocal<>();
    private static ThreadLocal<Execution> localExecution = new ThreadLocal<>();
    private static ThreadLocal<ConfigData> localConfigData = new ThreadLocal<>();
    private static ThreadLocal<TestContext> localTestContext = new ThreadLocal<>();
    private static ThreadLocal<ResultContainer> localResultContainer = new ThreadLocal<>();

    /**
     * Method to get Appium Driver
     *
     * @return : appium driver
     */
    public AppiumDriver getDriver() {
        return driver.get();
    }

    public void setDriver(AppiumDriver appiumDriver) {
        driver.set(appiumDriver);
    }

    /**
     * Method to get Android Driver
     *
     * @return : android driver
     */
    public AndroidDriver getAndroidDriver() {
        return androidDriver.get();
    }

    public void setAndroidDriver(AndroidDriver driver) {
        androidDriver.set(driver);
    }

    /**
     * Method to get IOS Driver
     *
     * @return : ios driver
     */
    public IOSDriver getIOSDriverDriver() {
        return iosDriver.get();
    }

    /**
     * Method to get Web Driver
     *
     * @return : Web driver
     */
    public RemoteWebDriver getWebDriver() {
        return webDriver.get();
    }

    public void setWebDriver(RemoteWebDriver remoteWebDriver) {
        webDriver.set(remoteWebDriver);
    }

    public String getExecutionPlatform() {
        return executionPlatform.get();
    }

    public void setExecutionPlatform(String platform) {
        executionPlatform.set(platform);
    }

    public Execution getLocalExecution() {
        return localExecution.get();
    }

    public void setLocalExecution(Execution execution) {
        localExecution.set(execution);
    }

    public Result getLocalResult() {
        return ThreadLocalResult.get();
    }

    public String getTypeOfExecution() {
        return typeOfExecution.get();
    }

    public void setTypeOfExecution(String type) {
        typeOfExecution.set(type);
    }

    public ConfigData getConfigData() {
        return localConfigData.get();
    }

    public void setIosDriver(IOSDriver driver) {
        iosDriver.set(driver);
    }

    public void setThreadLocalResult(Result result) {
        ThreadLocalResult.set(result);
    }

    public void setLocalConfigData(ConfigData configData) {
        localConfigData.set(configData);
    }

    public TestContext getLocalTestContext() {
        return localTestContext.get();
    }

    public void setLocalTestContext(TestContext test) {
        localTestContext.set(test);
    }

    public StormTest getStormTestDriver() {
        return stormTestDriver.get();
    }

    public void setStormTestDriver(StormTest stormTest) {
        ExecutionContainer.stormTestDriver.set(stormTest);
    }

    /**
     * Method to get platform
     *
     * @return boolean
     */
    public boolean isAndroid() {
        return getTypeOfExecution().equalsIgnoreCase(Platforms.MOBILE.name()) && getExecutionPlatform().length() < 40;
    }

    /**
     * Method to get platform
     *
     * @return boolean
     */
    public boolean isAPI() {
        return getTypeOfExecution().equalsIgnoreCase(Platforms.API.name());
    }

    /**
     * Method to get platform
     *
     * @return boolean
     */
    public boolean isIOS() {
        return getTypeOfExecution().equalsIgnoreCase(Platforms.MOBILE.name()) && getExecutionPlatform().length() >= 40;
    }

    /**
     * Method to get platform
     *
     * @return boolean
     */
    public boolean isMobile() {
        return getTypeOfExecution().equalsIgnoreCase(Platforms.MOBILE.name());
    }

    /**
     * Method to get platform
     *
     * @return boolean
     */
    public boolean isWeb() {
        return getTypeOfExecution().equalsIgnoreCase(Platforms.WEB.name());
    }
    /**
     * Method to get platform
     *
     * @return boolean
     */
    public boolean isStormTest() {
        return getTypeOfExecution().equalsIgnoreCase(ExecutorType.STORMTEST.name());
    }

    /**
     * Method to get platform
     *
     * @return boolean
     */
    public boolean isROKU() {
        return getTypeOfExecution().equalsIgnoreCase(Platforms.ROKU.name());
    }
    /**
     * Method to get platform
     *
     * @return boolean
     */
    public boolean isContract() {
        return getTypeOfExecution().equalsIgnoreCase(Platforms.CONTRACT.name());
    }
    /**
     * Method to get platform
     *
     * @return boolean
     */
    public boolean isPerformance() {
        return getTypeOfExecution().equalsIgnoreCase(Platforms.PERFORMANCE.name());
    }
    /**
     * Method to get platform
     *
     * @return boolean
     */
    public boolean isDesktop() {
        return getTypeOfExecution().equalsIgnoreCase(Platforms.DESKTOP.name());
    }
    public ResultContainer getLocalResultContainer() {
        return localResultContainer.get();
    }

    public void setLocalResultContainer(ResultContainer resultContainer) {
        localResultContainer.set(resultContainer);
    }

    public RestAPIExtension getApiDriver() {
        return apiDriver.get();
    }

    public void setApiDriver(RestAPIExtension apiDriver1) {
        apiDriver.set(apiDriver1);
    }

    public ConsumerClient getContractDriver() {
        return contractDriver.get();
    }

    public void setContractDriver(ConsumerClient pactDriver) {
        contractDriver.set(pactDriver);
    }
}
