package com.gl.testngfw.report.model;

import com.gl.testngfw.report.model.UserDevice;
import java.util.List;
import java.io.Serializable;

public class AcquireDevices implements Serializable {

    private String userID;

    private List<UserDevice> deviceList;

    public AcquireDevices() {
        super();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<UserDevice> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<UserDevice> deviceList) {
        this.deviceList = deviceList;
    }
}

