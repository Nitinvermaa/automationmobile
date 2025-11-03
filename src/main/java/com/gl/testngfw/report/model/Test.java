package com.gl.testngfw.report.model;

import java.io.Serializable;

public class Test implements Serializable {
    private String testCaseUniqueID;
    private String instances;

    public Test() {
        super();
    }

    public String getTestCaseUniqueID() {
        return testCaseUniqueID;
    }

    public void setTestCaseUniqueID(String testCaseUniqueID) {
        this.testCaseUniqueID = testCaseUniqueID;
    }

    public String getInstances() {
        return instances;
    }

    public void setInstances(String instances) {
        this.instances = instances;
    }
}
