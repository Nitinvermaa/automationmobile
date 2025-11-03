package com.gl.testngfw.report.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Result implements Serializable {

    /**
     * Auto generate primary key while inserting data
     */
    private String resultId;

    /**
     * Foreign key - primary key of Execution table
     */
    private String executionId;

    /**
     * Primary key of Test Case table
     */
    private String testCaseUniqueKey;

    private String projectUniqueKey;

    private String platformName;

    private String status;

    private String errorMessage;

    private String description;

    private String testCaseId;

    private String featureName;

    private List<Steps> steps = new ArrayList<>();

    /**
     * Device, Log and Screenshot get persisted in their own tables, this is
     * used for object mapping in the rest api call
     */
    private Device device;

    private Log log;

    private Log networkLog;

    private Log adbLog;

    private Screenshot screenshot;

    private String startTime;

    private String endTime;

    private String executionTime;

    private boolean videoPresent;

    private Video video;

    private List<String> logs = new ArrayList<>();

    private String testCaseName;

    public Result() {
        super();
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getTestCaseUniqueKey() {
        return testCaseUniqueKey;
    }

    public void setTestCaseUniqueKey(String testCaseUniqueKey) {
        this.testCaseUniqueKey = testCaseUniqueKey;
    }

    public String getProjectUniqueKey() {
        return projectUniqueKey;
    }

    public void setProjectUniqueKey(String projectUniqueKey) {
        this.projectUniqueKey = projectUniqueKey;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public Screenshot getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(Screenshot screenshot) {
        this.screenshot = screenshot;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Log getNetworkLog() {
        return networkLog;
    }

    public void setNetworkLog(Log networkLog) {
        this.networkLog = networkLog;
    }

    public Log getAdbLog() {
        return adbLog;
    }

    public void setAdbLog(Log adbLog) {
        this.adbLog = adbLog;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public boolean isVideoPresent() {
        return videoPresent;
    }

    public void setVideoPresent(boolean videoPresent) {
        this.videoPresent = videoPresent;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }
}

