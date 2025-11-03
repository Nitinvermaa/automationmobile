package com.gl.testngfw.report.model;

import java.io.Serializable;
import java.util.List;

/**
 * Wrapper class to hold Test Case and Test Suite data together
 *
 * @author saurabh.gour
 */
public class UpdateExecution implements Serializable {

    private List<TestCase> testCaseList;

    private List<TestSuite> testSuites;

    private List<Property> properties;
    private String projectUniqueKey;

    public UpdateExecution() {
        super();
    }

    public List<TestCase> getTestCaseList() {
        return testCaseList;
    }

    public void setTestCaseList(List<TestCase> testCaseList) {
        this.testCaseList = testCaseList;
    }

    public List<TestSuite> getTestSuites() {
        return testSuites;
    }

    public void setTestSuites(List<TestSuite> testSuites) {
        this.testSuites = testSuites;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public String getProjectUniqueKey() {
        return projectUniqueKey;
    }

    public void setProjectUniqueKey(String projectUniqueKey) {
        this.projectUniqueKey = projectUniqueKey;
    }
}