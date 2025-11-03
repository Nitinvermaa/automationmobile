package com.gl.testngfw.report.model;

import java.io.Serializable;

public class MobileNetwork implements Serializable {
    /**
     * Auto generate primary key while inserting data
     */
    private String deviceId;

    /**
     * Foreign key - primary key of Result table
     */
    private String type;

    private String name;

    private String available;

    private String state;
    private String roaming;

    public MobileNetwork() {
        super();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRoaming() {
        return roaming;
    }

    public void setRoaming(String roaming) {
        this.roaming = roaming;
    }
}
