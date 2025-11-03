package com.gl.testngfw.report.model;

import java.io.Serializable;
import java.util.Map;

public class Property implements Serializable {
    /**
     * Auto generate primary key while inserting data
     */
    private String propertyId;

    /**
     * Foreign key
     **/
    private String projectUniqueKey;

    private String propertyFileName;

    /**
     * Contains map of all properties that the property file has
     */
    private Map<String, PropertyModel> properties;

    public Property() {
        super();
    }

    public String getPropertiesId() {
        return propertyId;
    }

    public void setPropertiesId(String propertiesId) {
        this.propertyId = propertiesId;
    }

    public String getProjectUniqueKey() {
        return projectUniqueKey;
    }

    public void setProjectUniqueKey(String projectUniqueKey) {
        this.projectUniqueKey = projectUniqueKey;
    }

    public String getPropertyFileName() {
        return propertyFileName;
    }

    public void setPropertyFileName(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    public Map<String, PropertyModel> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, PropertyModel> properties) {
        this.properties = properties;
    }

}
