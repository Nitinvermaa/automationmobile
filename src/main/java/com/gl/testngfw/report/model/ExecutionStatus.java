package com.gl.testngfw.report.model;

import java.io.Serializable;
import java.util.Map;

public class ExecutionStatus implements Serializable {
    private String executionId;
    private Map<String, String> status;
    private String progress;
    private String suiteName;
    private String projectUniqueKey;

    public ExecutionStatus() {
        super();
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public Map<String, String> getStatus() {
        return status;
    }

    public void setStatus(Map<String, String> status) {
        this.status = status;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getProjectUniqueKey() {
        return projectUniqueKey;
    }

    public void setProjectUniqueKey(String projectUniqueKey) {
        this.projectUniqueKey = projectUniqueKey;
    }
}
