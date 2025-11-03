package com.gl.testngfw.report.model;

import java.io.Serializable;

public class Project implements Serializable {

    private String projectUniqueKey;

    private String projectId;

    private String projectName;

    // Base 64 encoded string format of the logo
    private String projectLogo;

    public Project(String projectUniqueKey, String projectId, String projectName, String projectLogo) {
        super();
        this.projectUniqueKey = projectUniqueKey;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectLogo = projectLogo;
    }

    public String getProjectUniqueKey() {
        return projectUniqueKey;
    }

    public void setProjectUniqueKey(String projectUniqueKey) {
        this.projectUniqueKey = projectUniqueKey;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectLogo() {
        return projectLogo;
    }

    public void setProjectLogo(String projectLogo) {
        this.projectLogo = projectLogo;
    }

}