package com.gl.testngfw.report.model;

import java.io.Serializable;

public class Device implements Serializable {
    /**
     * Auto generate primary key while inserting data
     */
    private String deviceId;

    /**
     * Foreign key - primary key of Result table
     */
    private String resultId;

    private String deviceName;

    private String deviceVersion;

    private String deviceType;

    private String executionType;

    private String deviceOS;

    private String deviceIp;

    private String devicePort;

    private String slotNumber;

    private String hostName;

    public Device() {
        super();
    }

    public Device(String deviceId, String resultId, String deviceName, String deviceVersion, String deviceOS, String deviceIp, String devicePort, String slotNumber) {
        super();
        this.deviceId = deviceId;
        this.resultId = resultId;
        this.deviceName = deviceName;
        this.deviceVersion = deviceVersion;
        this.deviceOS = deviceOS;
        this.deviceIp = deviceIp;
        this.devicePort = devicePort;
        this.slotNumber = slotNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getDeviceOS() {
        return deviceOS;
    }

    public void setDeviceOS(String deviceOS) {
        this.deviceOS = deviceOS;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(String devicePort) {
        this.devicePort = devicePort;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
