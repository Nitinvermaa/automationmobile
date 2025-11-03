package com.gl.testngfw.logging;

import org.apache.log4j.Level;

/**
 * The FAIL level designates fail events that are logged in verification statements when verification is failed
 */
class FailLevel extends Level {

    public static final int FAIL_INT = ERROR_INT;

    public static final Level FAIL = new PassLevel(FAIL_INT, "FAIL", 10);

    protected FailLevel(int levelIntValue, String name, int agr) {
        super(levelIntValue, name, agr);
    }
}
