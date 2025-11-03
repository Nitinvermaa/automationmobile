package com.gl.testngfw.report.model;

import java.io.Serializable;

public class Node implements Serializable {

    /**
     * Auto generate primary key while inserting data
     */
    private String id;

    private String nodeName;

    private String label;

    private String ipAddress;

    private SupportedPlatforms supportedPlatforms;

    public Node() {
        super();
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the nodeName
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * @param nodeName the nodeName to set
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return the supportedPlatforms
     */
    public SupportedPlatforms getSupportedPlatforms() {
        return supportedPlatforms;
    }

    /**
     * @param supportedPlatforms the supportedPlatforms to set
     */
    public void setSupportedPlatforms(SupportedPlatforms supportedPlatforms) {
        this.supportedPlatforms = supportedPlatforms;
    }
}
