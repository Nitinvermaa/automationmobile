package com.gl.testngfw.enums;

public enum Platforms {
    IOS("iOS"),
    ANDROID("Android"),
    WEB("Web"),
    API("API"),
    ANDROID_CHROME("Android_Chrome"),
    IOS_SAFARI("iOS_Safari"),
    PERFORMANCE("Performance"),
    CONTRACT("Contract"),
    STB("STB"),
    MOBILE("Mobile"),
    ROKU("Roku"),
    LOAD_TESTING("loadTesting"),
    DESKTOP("Desktop");

    private final String value;

    Platforms(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
