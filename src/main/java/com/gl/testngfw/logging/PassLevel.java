package com.gl.testngfw.logging;

import org.apache.log4j.Level;

/**
 * The PASS level designates pass events that are logged in verification statements when verification is passed
 */
class PassLevel extends Level {

    public static final int PASS_INT = WARN_INT + 20;

    public static final Level PASS = new PassLevel(PASS_INT, "PASS", 10);

    protected PassLevel(int levelIntValue, String name, int agr) {
        super(levelIntValue, name, agr);
    }
}
