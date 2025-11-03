package com.gl.testngfw.report.model;

import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.utility.FileUtil;
import com.gl.testngfw.utility.Util;
import com.gl.testngfw.enums.Browser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * models class for Configuration Data
 */
public class ConfigData implements Serializable {
    private String bundleID;
    private String packageName;
    private String activityName;
    private String executionPlatform;
    private String executionServer;
    private String chromeDriverPath;
    private String geckoDriverPath;
    private String edgeDriverPath;
    private String safariDriverPath;
    private String ieDriverPath;
    private String executionType;
    private String iosWebkitProxyPath;
    private String browserMobProxyLocation;
    private String projectID;
    private String testSuiteName;
    private List<Platforms> executeFor= new ArrayList<>();
    private int retryCount;

    private boolean captureDeviceLogs;
    private boolean isBrowserMobProxyRequired;
    private boolean isAppiumStartedUsingServices;
    private boolean debugMode;
    private int instances = 1;
    private boolean sendMailReport;
    private boolean useJenkins;
    private boolean isLoadTesting;
    private boolean useDB;
    private boolean gafCloud;
    private boolean recordVideo;
    private boolean headless;
    private String gafUserName;
    private String gafPassword;
    private String frameworkDBUrl;
    private List<Browser> browserList = new ArrayList<>();

    public ConfigData() {
        Map<String, PropertyModel> projectProperties = FileUtil.getConfigMAP("Project.Properties");
        if (projectProperties != null) {
            initExecuteFor(projectProperties);
            initBrowserList(projectProperties);
            projectID = projectProperties.containsKey("projectID") ? projectProperties.get("projectID").getValue() : "";
            testSuiteName = projectProperties.containsKey("testSuiteName") ? projectProperties.get("testSuiteName").getValue() : "";
            bundleID = projectProperties.containsKey("bundleID") ? projectProperties.get("bundleID").getValue() : "";
            //packageName = projectProperties.containsKey("packageName") ? projectProperties.get("packageName").getValue() : "";
            packageName = " com.tmobile.dm.cmas";
            //activityName = projectProperties.containsKey("activityName") ? projectProperties.get("activityName").getValue() : "";
            activityName = "com.tmobile.dm.cmas.MainActivity";
            isBrowserMobProxyRequired = projectProperties.containsKey("isBrowserMobProxyRequired") && Boolean.parseBoolean(projectProperties.get("isBrowserMobProxyRequired").getValue());
            isAppiumStartedUsingServices = projectProperties.containsKey("isAppiumStartedUsingServices") && Boolean.parseBoolean(projectProperties.get("isAppiumStartedUsingServices").getValue());
            executionPlatform = projectProperties.containsKey("executionPlatform") ? projectProperties.get("executionPlatform").getValue() : "";
            iosWebkitProxyPath = projectProperties.containsKey("iosWebkitProxyPath") ? projectProperties.get("iosWebkitProxyPath").getValue() : "";
            executionServer = projectProperties.containsKey("executionServer") ? projectProperties.get("executionServer").getValue() : "";
            chromeDriverPath = projectProperties.containsKey("chromeDriverPath") ? projectProperties.get("chromeDriverPath").getValue() : "";
            geckoDriverPath = projectProperties.containsKey("geckoDriverPath") ? projectProperties.get("geckoDriverPath").getValue() : "";
            edgeDriverPath = projectProperties.containsKey("edgeDriverPath") ? projectProperties.get("edgeDriverPath").getValue() : "";
            ieDriverPath = projectProperties.containsKey("ieDriverPath") ? projectProperties.get("ieDriverPath").getValue() : "";
            safariDriverPath = projectProperties.containsKey("safariDriverPath") ? projectProperties.get("safariDriverPath").getValue() : "";
            executionType = projectProperties.containsKey("executionType") ? projectProperties.get("executionType").getValue() : "";
            instances = projectProperties.containsKey("instances") ? Integer.parseInt(projectProperties.get("instances").getValue()) : 0;
            debugMode = projectProperties.containsKey("debugMode") && Boolean.parseBoolean(projectProperties.get("debugMode").getValue());
            sendMailReport = projectProperties.containsKey("sendMailReport") && Boolean.parseBoolean(projectProperties.get("sendMailReport").getValue());
            isLoadTesting = projectProperties.containsKey("isLoadTesting") && Boolean.parseBoolean(projectProperties.get("isLoadTesting").getValue());
            useJenkins = projectProperties.containsKey("useJenkins") && Boolean.parseBoolean(projectProperties.get("useJenkins").getValue());
            browserMobProxyLocation = projectProperties.containsKey("browserMobProxyLocation") ? projectProperties.get("browserMobProxyLocation").getValue() : "";
            captureDeviceLogs = projectProperties.containsKey("captureDeviceLogs") && Boolean.parseBoolean(projectProperties.get("captureDeviceLogs").getValue());
            useDB = projectProperties.containsKey("useDB") && Boolean.parseBoolean(projectProperties.get("useDB").getValue());
            gafCloud = projectProperties.containsKey("gafCloud") && Boolean.parseBoolean(projectProperties.get("gafCloud").getValue());
            gafUserName = projectProperties.containsKey("gafUserName") ? projectProperties.get("gafUserName").getValue() : "";
            gafPassword = projectProperties.containsKey("gafPassword") ? projectProperties.get("gafPassword").getValue() : "";
            retryCount = projectProperties.containsKey("retryFailedTestCount") ? Integer.parseInt(projectProperties.get("retryFailedTestCount").getValue()) : 0;
            recordVideo = projectProperties.containsKey("recordVideo") && Boolean.parseBoolean(projectProperties.get("recordVideo").getValue());
            headless = projectProperties.containsKey("headless") && Boolean.parseBoolean(projectProperties.get("headless").getValue());
            //frameworkDBUrl = projectProperties.containsKey("frameworkDBUrl") ? projectProperties.get("frameworkDBUrl").getValue() : "";
            frameworkDBUrl = "http://127.0.0.1:8283/";
        }
    }

    public String getBundleID() {
        return bundleID;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getExecutionPlatform() {
        return executionPlatform;
    }

    public boolean getIsBrowserMobProxyRequired() {
        return isBrowserMobProxyRequired;
    }

    public List<Browser> getBrowserList() {
        return browserList;
    }

    public String getExecutionServer() {
        return executionServer;
    }

    public String getExecutionType() {
        return executionType;
    }

    public boolean isBrowserMobProxyRequired() {
        return isBrowserMobProxyRequired;
    }

    public String getIosWebkitProxyPath() {
        return iosWebkitProxyPath;
    }

    public String getChromeDriverPath() {
        return chromeDriverPath;
    }

    public String getGeckoDriverPath() {
        return geckoDriverPath;
    }

    public String getIeDriverPath() {
        return ieDriverPath;
    }

    public String getEdgeDriverPath() {
        return edgeDriverPath;
    }

    public String getSafariDriverPath() {
        return safariDriverPath;
    }

    public int getInstances() {
        return instances;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public boolean isAppiumStartedUsingServices() {
        return isAppiumStartedUsingServices;
    }

    public boolean isSendMailReport() {
        return sendMailReport;
    }

    public boolean getUseJenkins() {
        return useJenkins;
    }

    public String getBrowserMobProxyLocation() {
        return browserMobProxyLocation;
    }

    public boolean getCaptureDeviceLogs() {
        return captureDeviceLogs;
    }

    public boolean isLoadTesting() {
        return isLoadTesting;
    }

    public String getProjectID() {
        return projectID;
    }

    public String getTestSuiteName() {
        return testSuiteName;
    }

    public void setExecutionPlatform(String executionPlatform) {
        this.executionPlatform = executionPlatform;
    }

    public void setBundleID(String bundleID) {
        this.bundleID = bundleID;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setExecutionServer(String executionServer) {
        this.executionServer = executionServer;
    }

    public void setChromeDriverPath(String chromeDriverPath) {
        this.chromeDriverPath = chromeDriverPath;
    }

    public void setGeckoDriverPath(String geckoDriverPath) {
        this.geckoDriverPath = geckoDriverPath;
    }

    public void setEdgeDriverPath(String edgeDriverPath) {
        this.edgeDriverPath = edgeDriverPath;
    }

    public void setSafariDriverPath(String safariDriverPath) {
        this.safariDriverPath = safariDriverPath;
    }

    public void setIeDriverPath(String ieDriverPath) {
        this.ieDriverPath = ieDriverPath;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public void setIosWebkitProxyPath(String iosWebkitProxyPath) {
        this.iosWebkitProxyPath = iosWebkitProxyPath;
    }

    public void setBrowserMobProxyLocation(String browserMobProxyLocation) {
        this.browserMobProxyLocation = browserMobProxyLocation;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public void setTestSuiteName(String testSuiteName) {
        this.testSuiteName = testSuiteName;
    }

    public void setCaptureDeviceLogs(boolean captureDeviceLogs) {
        this.captureDeviceLogs = captureDeviceLogs;
    }

    public void setBrowserMobProxyRequired(boolean browserMobProxyRequired) {
        isBrowserMobProxyRequired = browserMobProxyRequired;
    }

    public void setAppiumStartedUsingServices(boolean appiumStartedUsingServices) {
        isAppiumStartedUsingServices = appiumStartedUsingServices;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void setInstances(int instances) {
        this.instances = instances;
    }

    public void setSendMailReport(boolean sendMailReport) {
        this.sendMailReport = sendMailReport;
    }

    public void setUseJenkins(boolean useJenkins) {
        this.useJenkins = useJenkins;
    }

    public void setLoadTesting(boolean loadTesting) {
        isLoadTesting = loadTesting;
    }

    public List<Platforms> getExecuteFor() {
        return executeFor;
    }

    public void setExecuteFor(Platforms platform) {
        this.executeFor.add(platform);
    }

    public boolean isUseDB() {
        return useDB;
    }

    public void setUseDB(boolean useDB) {
        this.useDB = useDB;
    }

    public boolean isGafCloud() {
        return gafCloud;
    }

    public void setGafCloud(boolean gafCloud) {
        this.gafCloud = gafCloud;
    }

    public String getGafUserName() {
        return gafUserName;
    }

    public void setGafUserName(String gafUserName) {
        this.gafUserName = gafUserName;
    }

    public String getGafPassword() {
        return gafPassword;
    }

    public void setGafPassword(String gafPassword) {
        this.gafPassword = gafPassword;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public boolean isRecordVideo() {
        return recordVideo;
    }

    public void setRecordVideo(boolean recordVideo) {
        this.recordVideo = recordVideo;
    }

    public String getFrameworkDBUrl() {
        return frameworkDBUrl;
    }

    public void setFrameworkDBUrl(String frameworkDBUrl) {
        this.frameworkDBUrl = frameworkDBUrl;
    }

    public boolean isHeadless() {
        return headless;
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }
    private void initExecuteFor(Map<String, PropertyModel> projectProperties) {
        String platforms=projectProperties.get("executeFor").getValue();
        if (platforms.contains(",")) {
            Arrays.stream(platforms.split(",")).forEach(x -> executeFor.add(Util.getPlatform(x)));
        } else {
            executeFor.add(Util.getPlatform(platforms));
        }
    }
    private void initBrowserList(Map<String, PropertyModel> projectProperties) {
        String browsers= projectProperties.get("browserList").getValue();
        if(browsers.contains(",")){
            Arrays.stream(browsers.split(",")).forEach(x->browserList.add(Util.getBrowserName(x)));
        }else{
            browserList.add(Util.getBrowserName(browsers));
        }
    }

}
