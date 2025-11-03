package com.gl.testngfw.report.model;

import java.io.Serializable;
import java.util.List;

public class Execution implements Serializable {

    /**
     * Auto generate primary key while inserting data
     */
    private String executionId;

    /**
     * Foregin key to specify project for which project this execution is being
     * run
     */
    private String projectUniqueKey;

    private String suiteName;

    private String executionDate;

    private String startTime;

    private String endTime;

    /**
     * To be used when mapping object to a rest api call. Execution contains a
     * List<Result>
     */
    private List<Result> resultsList;

    public Execution() {
        super();
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

    public List<Result> getResultsList() {
        return resultsList;
    }

    public void setResultsList(List<Result> resultsList) {
        this.resultsList = resultsList;
    }

}
