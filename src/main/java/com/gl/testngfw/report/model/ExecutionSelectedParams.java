package com.gl.testngfw.report.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ExecutionSelectedParams implements Serializable {
    private String executionId;
    private String projectUniqueKey;
    private String suiteName;
    private List<Property> properties;
    private Map<String, Browser> testCases;

    public ExecutionSelectedParams() {
        super();
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public Map<String, Browser> getTestCases() {
        return testCases;
    }

    public void setTestCases(Map<String, Browser> testCases) {
        this.testCases = testCases;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getProjectUniqueKey() {
        return projectUniqueKey;
    }

    public void setProjectUniqueKey(String projectUniqueKey) {
        this.projectUniqueKey = projectUniqueKey;
    }
}
