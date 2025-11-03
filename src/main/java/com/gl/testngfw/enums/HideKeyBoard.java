package com.gl.testngfw.enums;

/**
 * Hide key boards events or to press keys on keyboard
 */
public enum HideKeyBoard {
    PRESS_KEY("pressKey"),
    TAP_OUTSIDE("tapOutside");

    private String value;

    HideKeyBoard(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
