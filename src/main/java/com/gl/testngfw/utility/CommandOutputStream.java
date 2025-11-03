package com.gl.testngfw.utility;

import org.apache.commons.exec.LogOutputStream;

import java.util.LinkedList;
import java.util.List;

/**
 * Log OutPut stream to get commandline output
 */
public class CommandOutputStream extends LogOutputStream {
    private final List<String> lines = new LinkedList<>();

    @Override
    protected void processLine(String line, int level) {
        lines.add(line);
    }

    public List<String> getLines() {
        return lines;
    }
}
