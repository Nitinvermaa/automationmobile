package com.gl.testngfw.logging;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;

/**
 * Logger class.
 */
public class FrameworkLogger {
    private static final String LOG_FILE_EXTENSION = ".log";
    private static final String LOG_FILE_NAME = "file";
    private static ThreadLocal<Logger> LOGGER = new ThreadLocal<Logger>();
    private static ThreadLocal<Boolean> enableAPIFileLogging = new ThreadLocal<Boolean>();
    private static PrintStream myPrintStream;

    private FrameworkLogger() {
    }

    public static void logStep(final String message) {
        LOGGER.get().info(message);
    }

    //This method should be used when any exception is taken a place
    public static void logError(final Exception exception) {
        if (exception != null) {
            String message = exception.getMessage();
            LOGGER.get().error(message != null ? message : "", exception);
        }
    }

    //This method is used when verification statement fails
    public static void logFail(final String failMessage) {
        LOGGER.get().log(FailLevel.FAIL, failMessage);
    }

    public static void logWarning(final String warningMessage) {
        LOGGER.get().warn(warningMessage);
    }

    public static void logDebug(final String debugMessage) {
        LOGGER.get().debug(debugMessage);
    }

    //This method is used when verification is pass
    public static void logPass(final String verifyMessage) {
        LOGGER.get().log(PassLevel.PASS, verifyMessage);
    }


    public synchronized static void config(Map<String, String> headerDataMap, String fileName, String testName) {
        LOGGER.set(Logger.getLogger(Thread.currentThread().getName() + testName));
        //Configure file appender
        LOGGER.get().addAppender(createFileAppender(headerDataMap, fileName));
    }

    private static synchronized FileAppender createFileAppender(Map<String, String> headerDataMap, String logFileName) {
        FileAppender fileAppender = new FileAppender();
        fileAppender.setName(LOG_FILE_NAME);
        fileAppender.setFile(logFileName + LOG_FILE_EXTENSION);
        fileAppender.setLayout(new CustomPatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-4p - %m%n", headerDataMap));
        fileAppender.setAppend(false);
        fileAppender.activateOptions();
        return fileAppender;
    }


    /**
     * @return printStream
     */
    public static PrintStream getPrintStream() {
        if (myPrintStream == null) {
            OutputStream output = new OutputStream() {
                private StringBuilder myStringBuilder = new StringBuilder();

                @Override
                public void write(int b) throws IOException {
                    this.myStringBuilder.append((char) b);
                }

                /**
                 * @see java.io.OutputStream#flush()
                 */
                @Override
                public void flush() {
                    try {
                        if (!this.myStringBuilder.toString().isEmpty() && !"\r\n".equals(myStringBuilder.toString())
                                && getLOGGER().get() != null && enableAPIFileLogging.get()) {
                            FrameworkLogger.logStep(this.myStringBuilder.toString());
                        }
                    }catch (Exception e){
                        FrameworkLogger.logStep(this.myStringBuilder.toString());
                    }
                    myStringBuilder = new StringBuilder();
                }
            };
            myPrintStream = new PrintStream(output, true);  // true: autoflush must be set!
        }
        return myPrintStream;
    }

    public static ThreadLocal<Logger> getLOGGER() {
        return LOGGER;
    }

    public static void setEnableAPIFileLogging(boolean enableFileLogging) {
        enableAPIFileLogging.set(enableFileLogging);
    }
}
