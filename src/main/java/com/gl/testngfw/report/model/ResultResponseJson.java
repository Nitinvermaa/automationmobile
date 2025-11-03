package com.gl.testngfw.report.model;

import java.io.Serializable;
import java.util.List;

public class ResultResponseJson implements Serializable {

    private String executionId;
    private String suiteName;
    private String executionDate;
    private String startTime;
    private String endTime;
    private List<PlatformJson> platformJsonList;

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<PlatformJson> getPlatformJsonList() {
        return platformJsonList;
    }

    public void setPlatformJsonList(List<PlatformJson> platformJsonList) {
        this.platformJsonList = platformJsonList;
    }
}
