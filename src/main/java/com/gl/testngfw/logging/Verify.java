package com.gl.testngfw.logging;


import org.testng.Assert;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class holds a set of methods to verify different kinds of objects
 */
public class Verify {
    private static final Logger LOGGER = Logger.getLogger(Verify.class.getName());

    private Verify() {
    }

    /**
     * Verifies if the object is true, raise an exception otherwise
     */
    public static void verifyTrue(Boolean object) throws VerificationFailException {
        verifyEquals(object, true);
    }

    /**
     * Verifies if the object is true, raise an exception otherwise
     * Add description message in the log
     */
    public static void verifyTrue(Boolean object, String message) throws VerificationFailException {
        verifyEquals(object, true, message);
    }


    /**
     * Verifies if the object is false, raise an exception otherwise
     */
    public static void verifyFalse(Boolean object) throws VerificationFailException {
        verifyEquals(object, false);
    }

    /**
     * Verifies if the object is false, raise an exception otherwise
     * Add description message in the log
     */
    public static void verifyFalse(Boolean object, String message) throws VerificationFailException {
        verifyEquals(object, false, message);
    }

    /**
     * Verifies if 2 objects are equal
     * Raise an exception in case when the objects are not equal, if it is not suppressed by suppressException parameter
     * Add description message in the log
     */
    public static void verifyEquals(Object actual, Object expected, String message, Boolean suppressException) throws VerificationFailException {
        String assertMessage = String.format("%s EXPECTED: '%s' ACTUAL: '%s'", message, expected.toString(), actual.toString());
        try {
            Assert.assertEquals(actual, expected);
            FrameworkLogger.logPass(assertMessage);
        } catch (AssertionError error) {
            fail(message, error, suppressException);
            FrameworkLogger.logFail(assertMessage);
            LOGGER.log(Level.WARNING, "Assert Fail:", error);
        }
    }

    /**
     * Verifies if 2 objects are equal,
     * Always raise an exception in case when the objects are not equal
     */
    public static void verifyEquals(Object actual, Object expected) throws VerificationFailException {
        verifyEquals(actual, expected, "", false);
    }

    /**
     * Verifies if 2 objects are equal
     * Raise an exception in case when the objects are not equal, if it is not suppressed by suppressException parameter
     */
    public static void verifyEquals(Object actual, Object expected, Boolean suppressException) throws VerificationFailException {
        verifyEquals(actual, expected, "", suppressException);
    }

    /**
     * Verifies if 2 objects are equal,
     * Always raise an exception in case when the objects are not equal
     * Add description message in the log
     */
    public static void verifyEquals(Object actual, Object expected, String message) throws VerificationFailException {
        verifyEquals(actual, expected, message, false);
    }


    /**
     * Verifies if 2 objects are NOT equal
     * Raise an exception in case when the objects are equal, if it is not suppressed by suppressException parameter
     * Add description message in the log
     */
    private static void verifyNotEquals(Object actual, Object expected, String message, Boolean suppressException) throws VerificationFailException {
        try {
            Assert.assertNotEquals(expected, actual);
            FrameworkLogger.logPass(String.format("%s ACTUAL: '%s' and EXPECTED: '%s'.",
                    message, actual.toString(), expected.toString()));
        } catch (AssertionError error) {
            fail(message, error, suppressException);
            LOGGER.log(Level.WARNING, "Assert Fail:", error);
        }
    }

    /**
     * Verifies if 2 objects are NOT equal,
     * Always raise an exception in case when the objects are equal
     */
    public static void verifyNotEquals(Object actual, Object expected) throws VerificationFailException {
        verifyNotEquals(actual, expected, "", false);
    }

    /**
     * Verifies if 2 objects are NOT equal
     * Raise an exception in case when the objects are equal, if it is not suppressed by suppressException parameter
     */
    public static void verifyNotEquals(Object actual, Object expected, Boolean suppressException) throws VerificationFailException {
        verifyNotEquals(actual, expected, "", suppressException);
    }

    /**
     * Verifies if 2 objects are NOT equal,
     * Always raise an exception in case when the objects are equal
     * Add description message in the log
     */
    public static void verifyNotEquals(Object actual, Object expected, String message) throws VerificationFailException {
        verifyNotEquals(actual, expected, message, false);
    }

    /**
     * The method is used when verification if fail
     * Capture an device screen image with unique sequence number in the name
     * Create and write a fail message into the log
     * Raise an exception if suppressException parameter is true
     */
    private static void fail(String descriptionMessage, AssertionError errorMessage, Boolean suppressException) throws VerificationFailException {

        // Build the error message.
        StringBuilder combinedMessage = new StringBuilder(descriptionMessage);
        if (descriptionMessage != null && !descriptionMessage.isEmpty()) {
            combinedMessage.append("\n");
        }
        combinedMessage.append(" VERIFICATION FAILS BETWEEN OBJECTS: ");
        combinedMessage.append(errorMessage.getMessage());

        //Write message to the log file with error message
        FrameworkLogger.logFail(combinedMessage.toString());

        //Throw new Verification fail exception if it is not suppressed
        if (!suppressException) {
            throw new VerificationFailException(combinedMessage.toString());
        }
    }

}
