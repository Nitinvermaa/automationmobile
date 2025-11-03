package com.gl.testngfw.report.model;

import java.io.Serializable;

public class FeatureJson implements Serializable {

    private String featureName;
    private String totalTestCase;
    private String passTestCase;
    private String failTestCase;
    private String skipTestCase;


    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String platformName) {
        this.featureName = platformName;
    }

    public String getTotalTestCase() {
        return totalTestCase;
    }

    public void setTotalTestCase(String totalTestCase) {
        this.totalTestCase = totalTestCase;
    }

    public String getPassTestCase() {
        return passTestCase;
    }

    public void setPassTestCase(String passTestCase) {
        this.passTestCase = passTestCase;
    }

    public String getFailTestCase() {
        return failTestCase;
    }

    public void setFailTestCase(String failTestCase) {
        this.failTestCase = failTestCase;
    }

    public String getSkipTestCase() {
        return skipTestCase;
    }

    public void setSkipTestCase(String skipTestCase) {
        this.skipTestCase = skipTestCase;
    }

    @Override
    public String toString() {
        return "FeatureJson [platformName=" + featureName + ", totalTestCase=" + totalTestCase + ", passTestCase="
                + passTestCase + ", failTestCase=" + failTestCase + ", skipTestCase=" + skipTestCase + "]";
    }

}
