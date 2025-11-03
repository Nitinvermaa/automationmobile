package com.gl.testngfw.enums;

public enum SuiteHeader {

    FEATURE("Feature Group"),
    TEST_CLASS("Test Class"),
    TEST_SCRIPT("Test Script"),
    TEST_DESCRIPTION("Test Script Description"),
    TEST_CASE_ID("Test Case ID"),
    REQUIREMENT_ID("Requirement ID"),
    IOS("iOS"),
    ANDROID("Android"),
    WEB("Web"),
    API("API"),
    INSTANCES("Instances"),
    FINAL_EXECUTION("Final Execution"),
    SUITE_NAME("Suite Name"),
    SUITE_TO_EXECUTE("Suite to execute"),
    TARGET_OEM("TargetOEM"),
    TARGET_DEVICE_MODEL("TargetDeviceModel"),
    TARGET_VERSION("TargetVersion");

    private String value;

    SuiteHeader(String value) {
        this.value = value;

    }

    public String getValue() {
        return value;
    }

}
