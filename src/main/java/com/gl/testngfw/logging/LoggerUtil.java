package com.gl.testngfw.logging;

import java.io.IOException;
import java.util.logging.*;

public class LoggerUtil {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private LoggerUtil() {
    }

    static public void setup() throws IOException {

        // suppress the logging output to the console
        Handler[] handlers = LOGGER.getParent().getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            LOGGER.removeHandler(handlers[0]);
        }

        LOGGER.setLevel(Level.INFO);
        FileHandler fileTxt = new FileHandler("Logging.txt");

        // create a TXT formatter
        SimpleFormatter formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        LOGGER.addHandler(fileTxt);
    }
}
