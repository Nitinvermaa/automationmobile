package com.gl.testngfw.report.model;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HubDetails implements Serializable {
    private String hubId;
    private String projectUniqueKey;

    private URL hubUrl;

    private URL hubRegistrationUrl;

    private String ipAddress;

    private Integer port;
    private List<GridNode> gridNodes = new ArrayList<GridNode>();

    public HubDetails() {
        super();
    }

    public String getProjectUniqueKey() {
        return projectUniqueKey;
    }

    public void setProjectUniqueKey(String projectUniqueKey) {
        this.projectUniqueKey = projectUniqueKey;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public URL getHubUrl() {
        return hubUrl;
    }

    public void setHubUrl(URL hubUrl) {
        this.hubUrl = hubUrl;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public URL getHubRegistrationUrl() {
        return hubRegistrationUrl;
    }

    public void setHubRegistrationUrl(URL hubRegistrationUrl) {
        this.hubRegistrationUrl = hubRegistrationUrl;
    }

    public String getHubId() {
        return hubId;
    }

    public void setHubId(String hubId) {
        this.hubId = hubId;
    }

    public List<GridNode> getGridNodes() {
        return gridNodes;
    }

    public void setGridNodes(List<GridNode> gridNodes) {
        this.gridNodes = gridNodes;
    }
}
