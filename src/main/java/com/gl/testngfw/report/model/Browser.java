package com.gl.testngfw.report.model;

import java.io.Serializable;
import java.util.List;

public class Browser implements Serializable {
    private String ip;
    private String hostName;
    private String executionType;
    private List<Test> testCaseIds;
    private Device device;
    private boolean mobileBrowser;

    public Browser() {
        super();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<Test> getTestCaseIds() {
        return testCaseIds;
    }

    public void setTestCaseIds(List<Test> testCaseIds) {
        this.testCaseIds = testCaseIds;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public boolean getMobileBrowser() {
        return mobileBrowser;
    }

    public void setMobileBrowser(boolean mobileBrowser) {
        this.mobileBrowser = mobileBrowser;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public boolean isMobileBrowser() {
        return mobileBrowser;
    }
}
