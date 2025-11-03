package com.gl.testngfw.report.model;

import com.gl.testngfw.enums.ExecutorType;
import com.gl.testngfw.enums.Platforms;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestCase implements Serializable {
    private String testCaseUniqueKey;

    private String projectUniqueKey;

    private String testCaseId;

    private String testCaseName;

    private String testCaseClassName;

    private String testCaseDesc;

    private String requirementId;

    private String feature;

    private ExecutorType executor;

    private List<Platforms> supportedPlatform = new ArrayList<>();

  /*  private boolean forAndroid;

    private boolean forAndroidBrowser;

    private boolean forIos;

    private boolean forIosBrowser;

    private boolean forWeb;

    private boolean forApi;*/

    private int apiInstance;

    private List<String> targetOem;

    private List<String> targetDeviceModel;

    private List<String> targetVersion;

    private List<String> targetBrowser;

    private List<Steps> steps;

    private String status;

    private String createdTime;

    private String modifiedTime;

    private String executionTime;

    public TestCase() {
        super();
    }

    public String getTestCaseUniqueKey() {
        return testCaseUniqueKey;
    }

    public void setTestCaseUniqueKey(String testCaseUniqueKey) {
        this.testCaseUniqueKey = testCaseUniqueKey;
    }

    public String getProjectUniqueKey() {
        return projectUniqueKey;
    }

    public void setProjectUniqueKey(String projectUniqueKey) {
        this.projectUniqueKey = projectUniqueKey;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public String getTestCaseClassName() {
        return testCaseClassName;
    }

    public void setTestCaseClassName(String testCaseClassName) {
        this.testCaseClassName = testCaseClassName;
    }

    public String getTestCaseDesc() {
        return testCaseDesc;
    }

    public void setTestCaseDesc(String testCaseDesc) {
        this.testCaseDesc = testCaseDesc;
    }

    public String getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(String requirementId) {
        this.requirementId = requirementId;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

   /* public boolean isForAndroid() {
        return forAndroid;
    }

    public void setForAndroid(boolean forAndroid) {
        this.forAndroid = forAndroid;
    }

    public boolean isForAndroidBrowser() {
        return forAndroidBrowser;
    }

    public void setForAndroidBrowser(boolean forAndroidBrowser) {
        this.forAndroidBrowser = forAndroidBrowser;
    }

    public boolean isForIos() {
        return forIos;
    }

    public void setForIos(boolean forIos) {
        this.forIos = forIos;
    }

    public boolean isForIosBrowser() {
        return forIosBrowser;
    }

    public void setForIosBrowser(boolean forIosBrowser) {
        this.forIosBrowser = forIosBrowser;
    }

    public boolean isForWeb() {
        return forWeb;
    }

    public void setForWeb(boolean forWeb) {
        this.forWeb = forWeb;
    }

    public boolean isForApi() {
        return forApi;
    }

    public void setForApi(boolean forApi) {
        this.forApi = forApi;
    }*/

    public int getApiInstance() {
        return apiInstance;
    }

    public void setApiInstance(int apiInstance) {
        this.apiInstance = apiInstance;
    }

    public List<String> getTargetOem() {
        return targetOem;
    }

    public void setTargetOem(List<String> targetOem) {
        this.targetOem = targetOem;
    }

    public List<String> getTargetDeviceModel() {
        return targetDeviceModel;
    }

    public void setTargetDeviceModel(List<String> targetDeviceModel) {
        this.targetDeviceModel = targetDeviceModel;
    }

    public List<String> getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersion(List<String> targetVersion) {
        this.targetVersion = targetVersion;
    }

    public List<String> getTargetBrowser() {
        return targetBrowser;
    }

    public void setTargetBrowser(List<String> targetBrowser) {
        this.targetBrowser = targetBrowser;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public List<Platforms> getSupportedPlatform() {
        return supportedPlatform;
    }

    public void setSupportedPlatform(List<Platforms> supportedPlatform) {
        this.supportedPlatform = supportedPlatform;
    }

    public ExecutorType getExecutorType() {
        return executor;
    }

    public void setExecutorType(ExecutorType executorType) {
        this.executor = executorType;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public TestCase clone() {
        TestCase testCase = null;
        try {
            testCase = (TestCase) super.clone();
        } catch (CloneNotSupportedException ignore) {
            testCase = new TestCase();
            testCase.setSupportedPlatform(this.supportedPlatform);
            testCase.setApiInstance(this.apiInstance);
            testCase.setStatus(this.status);
            testCase.setFeature(this.feature);
            testCase.setExecutorType(this.executor);
            /*testCase.setForAndroid(this.forAndroid);
            testCase.setForAndroidBrowser(this.forAndroidBrowser);
            testCase.setForApi(this.forApi);
            testCase.setForIos(this.forIos);
            testCase.setForIosBrowser(this.forIosBrowser);
            testCase.setForWeb(this.forWeb);*/
            testCase.setProjectUniqueKey(this.projectUniqueKey);
            testCase.setRequirementId(this.requirementId);
            testCase.setSteps(this.steps);
            testCase.setTargetBrowser(this.targetBrowser);
            testCase.setTargetDeviceModel(this.targetDeviceModel);
            testCase.setTestCaseName(this.testCaseName);
            testCase.setTargetOem(this.targetOem);
            testCase.setTargetVersion(this.targetVersion);
            testCase.setTestCaseClassName(this.testCaseClassName);
            testCase.setTestCaseDesc(this.testCaseDesc);
            testCase.setTestCaseId(this.testCaseId);
            testCase.setTestCaseUniqueKey(this.testCaseUniqueKey);
            testCase.setCreatedTime(this.createdTime);
            testCase.setModifiedTime(this.modifiedTime);
        }
        return testCase;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TestCase) {
            TestCase testCase = (TestCase) o;
            int result = ComparisonChain.start()
                    .compare(testCaseUniqueKey, testCase.testCaseUniqueKey, Ordering.natural().nullsFirst())
                    .compare(testCaseId, testCase.testCaseId, Ordering.natural().nullsFirst())
                    .compare(testCaseName, testCase.testCaseName, Ordering.natural().nullsFirst())
                    .compare(testCaseClassName, testCase.testCaseClassName, Ordering.natural().nullsFirst())
                    .compare(testCaseDesc, testCase.testCaseDesc, Ordering.natural().nullsFirst())
                    .compare(requirementId, testCase.requirementId, Ordering.natural().nullsFirst())
                    .compare(feature, testCase.feature, Ordering.natural().nullsFirst())
                    /*.compare(forAndroid, testCase.forAndroid, Ordering.natural().nullsFirst())
                    .compare(forAndroidBrowser, testCase.forAndroidBrowser, Ordering.natural().nullsFirst())
                    .compare(forIos, testCase.forIos, Ordering.natural().nullsFirst())
                    .compare(forIosBrowser, testCase.forIosBrowser, Ordering.natural().nullsFirst())
                    .compare(forWeb, testCase.forWeb, Ordering.natural().nullsFirst())
                    .compare(forApi, testCase.forApi, Ordering.natural().nullsFirst())*/
                    .compare(apiInstance, testCase.apiInstance, Ordering.natural().nullsFirst())
                    .compare(targetOem, testCase.targetOem, Ordering.allEqual())
                    .compare(targetDeviceModel, testCase.targetDeviceModel, Ordering.allEqual())
                    .compare(targetVersion, testCase.targetVersion, Ordering.allEqual())
                    .compare(targetBrowser, testCase.targetBrowser, Ordering.allEqual())
                    .compare(steps, testCase.steps, Ordering.allEqual())
                    .result();
            return result == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return testCaseUniqueKey.hashCode();
    }
}
