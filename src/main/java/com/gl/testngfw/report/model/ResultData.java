package com.gl.testngfw.report.model;

import java.io.Serializable;

/**
 * models Class for Result Data
 */
public class ResultData implements Serializable {
    private String resultStatus;
    private String testCaseID = "";
    private String testCaseName = "";
    private String requirementID = "";
    private String testDescription = "";
    private String failureReason = "";
    private String networkErrorLogMessage = "";
    private String logPath = "";
    private long startTime;
    private long endTime;
    private String suiteName = "";
    private String platform = "";
    private String os = "";
    private String environment = "";
    private String buildVersion = "";
    private String category = "";
    private String executionDate = "0001-01-01 00:00:00";
    private String snapshotPath = "";
    private String triggerDateTime = "0001-01-01 00:00:00";
    private String expectedTime = "0001-01-01 00:00:00";
    private String actualTime = "0001-01-01 00:00:00";
    private String reportDir = "";


    public String getResultStatus() {
        return resultStatus;
    }


    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getTestCaseID() {
        return testCaseID;
    }

    public void setTestCaseID(String testCaseID) {
        this.testCaseID = testCaseID;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public String getRequirementID() {
        return requirementID;
    }

    public void setRequirementID(String requirementID) {
        this.requirementID = requirementID;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    public String getSnapshotPath() {
        return snapshotPath;
    }

    public void setSnapshotPath(String snapshotPath) {
        this.snapshotPath = snapshotPath;
    }

    public String getTriggerDateTime() {
        return triggerDateTime;
    }

    public void setTriggerDateTime(String triggerDateTime) {
        this.triggerDateTime = triggerDateTime;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(String expectedTime) {
        this.expectedTime = expectedTime;
    }

    public String getActualTime() {
        return actualTime;
    }

    public void setActualTime(String actualTime) {
        this.actualTime = actualTime;
    }

    public String getReportDir() {
        return reportDir;
    }

    public void setReportDir(String reportDir) {
        this.reportDir = reportDir;
    }

    public String getNetworkErrorLogMessage() {
        return networkErrorLogMessage;
    }

    public void setNetworkErrorLogMessage(String networkErrorLogMessage) {
        this.networkErrorLogMessage = networkErrorLogMessage;
    }
}
