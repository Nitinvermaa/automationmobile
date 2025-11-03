package com.gl.testngfw.report.model;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.io.Serializable;

public class UserDevice implements Serializable {
    /**
     * Auto generate primary key while inserting data
     */
    private String id;

    private String deviceId;

    private String projectUniqueKey;

    private String deviceName;

    private String deviceDisplayName;

    private String deviceVersion;

    private String deviceOS;

    private String manufacturer;

    private String proxyPort;

    private String status;

    private String hostName;

    private String hostIp;

    private String bluetooth;

    private MobileNetwork network;

    private String batteryStatus;

    public UserDevice() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getProjectUniqueKey() {
        return projectUniqueKey;
    }

    public void setProjectUniqueKey(String projectUniqueKey) {
        this.projectUniqueKey = projectUniqueKey;
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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getDeviceDisplayName() {
        return deviceDisplayName;
    }

    public void setDeviceDisplayName(String deviceDisplayName) {
        this.deviceDisplayName = deviceDisplayName;
    }

    public String getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(String bluetooth) {
        this.bluetooth = bluetooth;
    }

    public MobileNetwork getNetwork() {
        return network;
    }

    public void setNetwork(MobileNetwork network) {
        this.network = network;
    }

    public String getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(String batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserDevice) {
            UserDevice device = (UserDevice) o;
            int result = ComparisonChain.start()
                    .compare(deviceId, device.getDeviceId(), Ordering.natural().nullsFirst())
                    .compare(deviceName, device.getDeviceName(), Ordering.natural().nullsFirst())
                    .compare(deviceOS, device.getDeviceOS(), Ordering.natural().nullsFirst())
                    .compare(deviceVersion, device.getDeviceVersion(), Ordering.natural().nullsFirst())
                    .compare(hostName, device.getHostName(), Ordering.natural().nullsFirst())
                    .compare(hostIp, device.getHostIp(), Ordering.natural().nullsFirst())
                    .compare(manufacturer, device.getManufacturer(), Ordering.natural().nullsFirst())
                    .compare(status, device.getStatus(), Ordering.natural().nullsFirst())
                    .compare(proxyPort, device.getProxyPort(), Ordering.natural().nullsFirst())
                    .compare(bluetooth, device.getBluetooth(), Ordering.natural().nullsFirst())
                    .compare(batteryStatus, device.getBatteryStatus(), Ordering.natural().nullsFirst())
                    .compare(network, device.getNetwork(), Ordering.allEqual())
                    .compare(projectUniqueKey, device.getProjectUniqueKey(), Ordering.natural().nullsFirst())
                    .result();

            return result == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return deviceId.hashCode();
    }
}
