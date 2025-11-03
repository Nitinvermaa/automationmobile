package com.gl.testngfw.enums;

public enum Browser {

    CHROME("chrome"),
    FIREFOX("firefox"),
    IE("internet explorer"),
    SAFARI("safari"),
    EDGE("MicrosoftEdge");

    private String value;

    Browser(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
