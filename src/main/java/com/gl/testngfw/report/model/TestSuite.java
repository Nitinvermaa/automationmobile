package com.gl.testngfw.report.model;

import java.io.Serializable;
import java.util.List;

public class TestSuite implements Serializable {

    /**
     * Auto generate primary key while inserting data
     */
    private String testSuiteId;

    /**
     * Foreign key
     **/
    private String projectUniqueKey;

    private String testSuiteName;

    /**
     * Contains a list of testCaseUniqueKey, each value acts as a reference to
     * the TestCase table
     */
    private List<String> testCaseUniqueKey;

    public TestSuite() {
        super();
    }

    public TestSuite(String testSuiteName, List<String> testCaseUniqueKey) {
        super();
        this.testSuiteName = testSuiteName;
        this.testCaseUniqueKey = testCaseUniqueKey;
    }

    public String getTestSuiteId() {
        return testSuiteId;
    }

    public void setTestSuiteId(String testSuiteId) {
        this.testSuiteId = testSuiteId;
    }

    public String getTestSuiteName() {
        return testSuiteName;
    }

    public void setTestSuiteName(String testSuiteName) {
        this.testSuiteName = testSuiteName;
    }

    public String getProjectUniqueKey() {
        return projectUniqueKey;
    }

    public void setProjectUniqueKey(String projectUniqueKey) {
        this.projectUniqueKey = projectUniqueKey;
    }

    public List<String> getTestCaseUniqueKey() {
        return testCaseUniqueKey;
    }

    public void setTestCaseUniqueKey(List<String> testCaseUniqueKey) {
        this.testCaseUniqueKey = testCaseUniqueKey;
    }

}
