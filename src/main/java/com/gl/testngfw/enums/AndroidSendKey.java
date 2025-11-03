package com.gl.testngfw.enums;

/**
 * Android send keys.
 */
public enum AndroidSendKey {

    BACK(4),
    VOLUME_UP(24),
    VOLUME_DOWN(25),
    BACKSPACE(67),
    LOCK(26),
    UNLOCK(82),
    DEL(67),
    ENTER(66),
    HOME(3),
    MENU(82),
    SETTINGS(176),
    SPACE(62);

    private Integer value;

    AndroidSendKey(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
