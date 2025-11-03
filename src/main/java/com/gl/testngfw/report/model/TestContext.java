package com.gl.testngfw.report.model;

import com.gl.testngfw.common.Constants;
import com.gl.testngfw.enums.DataType;
import com.gl.testngfw.enums.ExecutorType;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.execution.HeaderData;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Method;


/**
 * models Class for Test Object
 */
public class TestContext implements Serializable {
    private String testCaseID = "";
    private String testMethodName = "";
    private String requirementID = "";
    private String testDescription = "";
    private DataType dataType;
    private String testDataPath = "";
    private String testCaseUniqueID = "";
    private String className = "";
    private String feature = "";
    private ExecutorType executorType;

    public TestContext(HeaderData headerData, TestCase testCase, Method method,ExecutorType type) {
        dataType = headerData != null ? headerData.dataType() : DataType.EMPTY;
        testDataPath = headerData != null ? headerData.testDataPath() : HeaderData.EMPTY_VALUE;
        testCaseID = headerData != null ? headerData.testCaseId() : testCase.getTestCaseId();
        requirementID = headerData != null ? headerData.requirementID() : testCase.getRequirementId();
        testDescription = headerData != null ? headerData.testDescription() : testCase.getTestCaseDesc();
        feature = headerData != null ? headerData.featureName() : testCase.getFeature();
        testCaseUniqueID = headerData != null ? headerData.testCaseUniqueId() : testCase.getTestCaseUniqueKey();
        executorType = type;
    }

    public String getTestCaseID() {
        return testCaseID;
    }

    public String getRequirementID() {
        return requirementID;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public Integer getCommandTimeOut() {
        return Constants.APPIUM_COMMAND_TIMEOUT_SEC;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getTestDataPath() {
        return testDataPath;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTestMethodName() {
        return testMethodName;
    }

    public void setTestMethodName(String testMethodName) {
        this.testMethodName = testMethodName;
    }

    public String getFeature() {
        return feature;
    }

    public String getTestCaseUniqueID() {
        return testCaseUniqueID;
    }

    public ExecutorType getExecutorType() {
        return executorType;
    }

    public void setExecutorType(ExecutorType executorType) {
        this.executorType = executorType;
    }
}
