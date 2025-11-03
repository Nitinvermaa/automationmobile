package com.gl.testngfw.report.model;

import java.io.Serializable;
import java.util.List;

public class PlatformJson implements Serializable {

    private String platformName;
    private String totalTestCase;
    private String passTestCase;
    private String failTestCase;
    private String skipTestCase;
    private List<FeatureJson> featureJsons;


    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
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

    public List<FeatureJson> getFeatureJsons() {
        return featureJsons;
    }

    public void setFeatureJsons(List<FeatureJson> featureJsons) {
        this.featureJsons = featureJsons;
    }

    @Override
    public String toString() {
        return "PlatformJson [platformName=" + platformName + ", totalTestCase=" + totalTestCase + ", passTestCase="
                + passTestCase + ", failTestCase=" + failTestCase + ", skipTestCase=" + skipTestCase + "]";
    }

}
