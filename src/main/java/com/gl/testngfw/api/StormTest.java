package com.gl.testngfw.api;

import com.gl.testngfw.common.Constants;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.utility.Util;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.javatuples.Pair;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StormTest {
    private static final Logger LOGGER = Logger.getLogger(StormTest.class.getName());
    private XmlRpcClient client = null;
    private XmlRpcClientConfigImpl config = null;

    private static final String logDirectory = TestExecutor.getBaseReportDirPath() + "/STB/";
    private static final String VIDEO_PREFIX = "STORM_TEST_";
    private int slotNo;
    private String serverIp;
    private String testCaseFileName = null;

    public StormTest() {
    }

    /**
     * Initializes connection with StormTest server application and reserves the specified slot on the StormTestRack
     *
     * @param serverIp IP address of Storm Test Rack
     * @param slotNo   Slot in Storm Test rack on which we should execute tests
     * @param nodeURL  URL to make connection to Storm Test Application Server
     * @return true if successful false if the method call fails or if there is an exception
     */
    ////@Override
    public boolean init(String serverIp, Integer port, Integer slotNo, String nodeURL) {

        this.slotNo = slotNo;
        this.serverIp = serverIp;
        LOGGER.log(Level.INFO, "Storm Test Details: Rack IP: " + serverIp + ". Rack Slot: " + slotNo);
        LOGGER.log(Level.INFO, "Storm Test Application Server: " + nodeURL);
        /*FrameworkLogger.logDebug("Storm Test Details: Rack IP: " + serverIp + ". Rack Slot: " + slotNo);
        FrameworkLogger.logDebug("Storm Test Application Server: " + nodeURL);*/
        if (config == null) {
            config = new XmlRpcClientConfigImpl();
        }
        try {
            LOGGER.log(Level.INFO, "Trying to connect to Storm Test Application Server at URL: " + nodeURL);
            //FrameworkLogger.logDebug("Trying to connect to Storm Test Application Server at URL: " + nodeURL);
            config.setServerURL(new URL(nodeURL));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "", e);
            LOGGER.log(Level.WARNING, "Failed to create URL for Strom Test server application");
            // FrameworkLogger.logDebug("Failed to create URL for Strom Test server application");
            return false;
        }

        client = new XmlRpcClient();
        client.setConfig(config);
        return true;
    }

    ////@Override
    public boolean connectToServer() {
        Util.createDirectory(logDirectory + "Slot_" + slotNo);
        Object[] args = {serverIp, logDirectory + "Slot_" + slotNo};
        try {
            Object[] result = (Object[]) client.execute("connectStormtestServer.connectServer", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "connect stormtest server failed with" + result[1]);
                //FrameworkLogger.logDebug("connect stormtest server failed with" + result[1]);
            }
            //logDirectory = String.valueOf(result[2]);
            LOGGER.log(Level.WARNING, "Stormtest execution data directory for slot[" + slotNo + "] = " + result[2]);
            //FrameworkLogger.logDebug("Stormtest execution data directory for slot[" + slotNo + "] = " + result[2]);
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    // //@Override
    public boolean reserveSlot() {
        Object[] args = {slotNo};
        try {
            Object[] result = (Object[]) client.execute("connectStormtestServer.reserveSlot", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "Failed to reserve storm test slot: " + slotNo + ". Reason: " + result[1]);
                //FrameworkLogger.logDebug("Failed to reserve storm test slot: " + slotNo + ". Reason: " + result[1]);

            } else {
                showVideo(args);
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "Failed to reserve storm test slot: " + slotNo);
            return false;
        }
    }

    // //@Override
    public boolean releaseSlot() {
        Object[] args = {slotNo};
        try {
            Object[] result = (Object[]) client.execute("releaseStormtestServer.freeSlot", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "Release stormtest server failed with" + result[1]);
                //FrameworkLogger.logDebug("Release stormtest server failed with" + result[1]);

            } else {
                closeVideo(args);
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "Release stormtest server failed");
            return false;
        }
    }


    /**
     * Starts Video Logging
     *
     * @param testCaseName - name of testcase
     *                     //* @param streamName   streamName(i.e. executionContext) for which the video log is getting started. specially used for kpi startRawRecording
     * @return true if successful false if the method call fails or if there is an exception
     */
    //@Override
    public boolean startVideoLog(String testCaseName) {
        Object[] args = {VIDEO_PREFIX + testCaseName + "_", slotNo};
        try {
            FrameworkLogger.logStep("VideoLog.startVideoLog");
            Object[] result = (Object[]) client.execute("VideoLog.startVideoLog", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "Failed to start Video Log with result = {} ", result[1]);
                FrameworkLogger.logWarning("Failed to start Video Log with result = {} " + result[1]);
            }
            testCaseFileName = (String) result[2];
            //ChangeManagementHandler.getHandler().setTestCaseFileName(testCaseFileName);
            LOGGER.log(Level.INFO, "testCaseFileName STB = {}", testCaseFileName);
            return (boolean) result[0];
        } catch (XmlRpcException | ArrayIndexOutOfBoundsException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }


    /**
     * Stops Video Logging
     *
     * @return true if successful false if the method call fails or if there is an exception
     */

    public Pair stopVideoLog() {
        Pair responsePair = null;
        Object[] args = {slotNo};
        try {
            FrameworkLogger.logStep("VideoLog.stopVideoLog");
            Object[] result = (Object[]) client.execute("VideoLog.stopVideoLog", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "Failed to stop Video Log with" + result[1]);
                FrameworkLogger.logWarning("Failed to stop Video Log with" + result[1]);
            }

            responsePair = new Pair((boolean) result[0], "");
            return responsePair;
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            responsePair = new Pair(false, "");
            return responsePair;

        }
    }

    /**
     * Show Video
     *
     * @return true if successful false if the method call fails or if there is an exception
     */
    public boolean showVideo(Object[] args) {
        try {
            FrameworkLogger.logStep("VideoLog.showVideo");
            Object[] result = (Object[]) client.execute("VideoLog.showVideo", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "Failed to show Video " + result[1]);
                FrameworkLogger.logWarning("Failed to show Video");
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }


    /**
     * Close Video
     *
     * @return true if successful false if the method call fails or if there is an exception
     */
    public boolean closeVideo(Object[] args) {
        try {
            FrameworkLogger.logStep("VideoLog.closeVideo");
            Object[] result = (Object[]) client.execute("VideoLog.closeVideo", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "Failed to close Video " + result[1]);
                FrameworkLogger.logWarning("Failed to close Video " + result[1]);
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    /**
     * Marks the begining of the regin in Log
     *
     * @param testCaseName - name of testcase
     */
    public void beginLogRegion(String testCaseName) {
        Object[] args = {testCaseName, slotNo};
        try {
            FrameworkLogger.logStep("beginLogRegion");
            Object[] result = (Object[]) client.execute("Log.beginLogRegion", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "beginLogRegion failed");
                FrameworkLogger.logWarning("beginLogRegion failed");
            }
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    /**
     * Marks the Ending of the region in Log
     *
     * @param testCaseName - name of testcase
     */
    public void endLogRegion(String testCaseName) {
        Object[] args = {testCaseName, slotNo};
        try {
            FrameworkLogger.logStep("endLogRegion");
            Object[] result = (Object[]) client.execute("Log.endLogRegion", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "endLogRegion failed");
                FrameworkLogger.logWarning("endLogRegion failed");
            }
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    /**
     * Press key on the configured Storm Test Remote
     *
     * @param key Key Name for the key that should be pressed.
     * @return true if successful false if the method call fails or if there is an exception
     */
    public boolean pressKey(Enum key) {
        Object[] args = {key.toString(), slotNo};
        try {
            FrameworkLogger.logStep("PressButton" + key);
            Object[] result = (Object[]) client.execute("PressButton.PressButton", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "Failed to press key" + key.toString() + "error:" + result[1]);
                FrameworkLogger.logWarning("Failed to press key" + key.toString() + "error:" + result[1]);
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    /**
     * Finds String from given region
     *
     * @param namedRegion - name of region
     * @return string value in given region
     */
    public String findString(String namedRegion) {
        Object[] args = new Object[]{slotNo, namedRegion};
        try {
            long startTime = System.currentTimeMillis();
            FrameworkLogger.logStep("Finding string in region " + namedRegion);
            while ((System.currentTimeMillis() - startTime) < 3000) {

                Object[] result = (Object[]) client.execute("OCR.findString", args);
                LOGGER.log(Level.WARNING, "Result : " + result[0]);
                if (Constants.FALSE.equals(result[0])) {
                    LOGGER.log(Level.WARNING, "Failed to find string. error:" + result[1]);
                    FrameworkLogger.logWarning("Failed to find string. error:" + result[1]);
                } else {
                    if (!result[0].toString().isEmpty() && result[0] != null) {
                        return String.valueOf(result[0]);
                    }
                }

            }
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return "Error Received";
        }
        return "";
    }

    /**
     * Returns true if expected string is present in given region
     *
     * @param namedRegion    - region for finding string value
     * @param expectedString - expected string
     * @return true if String is present in given region
     */
    public boolean ifStringPresent(String namedRegion, String expectedString) {
        Object[] args = new Object[]{slotNo, namedRegion, expectedString};
        FrameworkLogger.logStep("Checking if " + expectedString + " is present in region " + namedRegion);
        try {
            Object[] result = (Object[]) client.execute("OCR.ifStringPresent", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "Failed to Verify the String with" + result[1]);
                FrameworkLogger.logFail("Failed to Verify the String with" + result[1]);
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    /**
     * Compares the image in given region.
     *
     * @param testRegion - region for comparing image
     * @param refImage   - image to be compared in given region
     * @return true if image is found in given region
     */
    public boolean compareImage(String testRegion, String refImage) {
        Object[] args = new Object[]{slotNo, testRegion, refImage};
        FrameworkLogger.logStep("Comparing image " + refImage + " in region " + testRegion);
        try {
            Object[] result = (Object[]) client.execute("OCR.compareImage", args);
            if (Constants.FALSE.equals(result[0])) {
                if (Constants.NULL.equals(result[1])) {
                    LOGGER.log(Level.WARNING, "TestImage is not matching the refrence image");
                    FrameworkLogger.logFail("Image is not matching with the refrence image");
                } else {
                    LOGGER.log(Level.WARNING, "Failed to compare Image. error:" + result[1]);
                    FrameworkLogger.logWarning("Failed to compare Image. error:" + result[1]);
                }
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    /**
     * Capture image in given region.
     *
     * @param testRegion - region for capturing image
     * @param filePath   - path in which image captured will be stored
     * @return true if image captured successfully
     */
    public boolean captureImage(String testRegion, String filePath) {
        Object[] args = new Object[]{slotNo, testRegion, filePath};
        FrameworkLogger.logStep("Capture image in region " + testRegion);
        try {
            Object[] result = (Object[]) client.execute("OCR.captureImage", args);
            if (Constants.FALSE.equals(result[0])) {
                if (Constants.NULL.equals(result[1])) {
                    LOGGER.log(Level.WARNING, "Failed to capture image");
                    FrameworkLogger.logFail("Failed to capture image");
                } else {
                    LOGGER.log(Level.WARNING, "Failed to compare Image. error:" + result[1]);
                    FrameworkLogger.logWarning("Failed to capture image");
                }
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }

    }

    /**
     * Detects motion
     *
     * @param timeout    - timeout for detecting motion
     * @param regionName - region for detecting motion
     * @return true if motion is detected in given region.
     */
    public boolean detectMotion(int timeout, String regionName) {
        Object[] args = new Object[]{slotNo, timeout, regionName};
        FrameworkLogger.logStep("Detecting motion in region " + regionName);
        try {
            Object[] result = (Object[]) client.execute("OCR.detectMotion", args);
            if (Constants.FALSE.equals(result[0])
                    && Constants.NULL.equals(result[1])) {
                LOGGER.log(Level.WARNING, "No Motion Detected");
                FrameworkLogger.logFail("No Motion Detected in region " + regionName);
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    /**
     * Compares color using colored image in the given region.
     *
     * @param testRegion    - given region
     * @param refColorImage - reference color image for comparing color in the given region
     * @return true if color matches with the referenced color image.
     */
    public boolean compareColor(String testRegion, String refColorImage) {
        Object[] args = new Object[]{slotNo, testRegion, refColorImage};
        FrameworkLogger.logStep("Compare color in region " + testRegion);
        try {
            Object[] result = (Object[]) client.execute("OCR.compareColor", args);
            if (Constants.FALSE.equals(result[0])) {
                if (Constants.NULL.equals(result[1])) {
                    LOGGER.log(Level.WARNING, "Test Region is not matching the refrence Color Image");
                    FrameworkLogger.logFail("Test Region is not matching the refrence Color Image");
                } else {
                    LOGGER.log(Level.WARNING, "Failed to compare Color. error:" + result[1]);
                    FrameworkLogger.logWarning("Failed to compare Color. error:" + result[1]);
                }
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    /**
     * Reboots the STB.
     *
     * @param powerOnDelay  - delay for power up
     * @param powerOffDelay - delay for power down
     * @return true if rebooted successfully
     */
    public boolean rebootStb(int powerOnDelay, int powerOffDelay) {
        Object[] args = new Object[]{slotNo, powerOnDelay, powerOffDelay};
        FrameworkLogger.logStep("Reboot STB");
        try {
            Object[] result = (Object[]) client.execute("powerControl.reboot", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "Failed to reboot STB. error:" + result[1]);
                FrameworkLogger.logFail("Failed to reboot STB. error:" + result[1]);
            }
            Thread.sleep(60000);

            return (boolean) result[0];
        } catch (XmlRpcException | InterruptedException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    /**
     * Power up the STB.
     *
     * @param powerUpDelay - delay for power up
     * @return true if power up successful.
     */
    public boolean powerUpStb(int powerUpDelay) {
        Object[] args = new Object[]{slotNo, powerUpDelay};
        FrameworkLogger.logStep("Power up the STB");
        try {
            Object[] result = (Object[]) client.execute("powerControl.powerUp", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "Failed to power up STB. error:" + result[1]);
                FrameworkLogger.logFail("Failed to power up STB. error:" + result[1]);
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    /**
     * Power down the STB.
     *
     * @param powerDownDelay - delay for power down
     * @return true if power down successful.
     */
    public boolean powerDownStb(int powerDownDelay) {
        Object[] args = new Object[]{slotNo, powerDownDelay};
        FrameworkLogger.logStep("Power down the STB");
        try {
            Object[] result = (Object[]) client.execute("powerControl.powerDown", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "Failed to power down STB. error:" + result[1]);
                FrameworkLogger.logFail("Failed to power down STB. error:" + result[1]);
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }


    /**
     * Gets XmlRpcClient instance used for calling python api
     *
     * @return XmlRpcClient instance
     */
    public XmlRpcClient getClient() {
        return client;
    }

    /**
     * Gets StormTest server ip.
     *
     * @return StormTest server ip
     */
    public String getServerIp() {
        return serverIp;
    }

    /**
     * Gets StormTest server slot.
     *
     * @return StormTest server slot.
     */
    public int getSlotNo() {
        return slotNo;
    }

    /**
     * Checks whether video is playing or not.
     *
     * @param timeout check for this duration value (seconds)
     * @return true if video is playing or not
     */
    public boolean isVideoPlaying(int timeout) {

        Object[] args = new Object[]{timeout, slotNo};
        FrameworkLogger.logStep("Checks whether video is playing");
        try {
            Object[] result = (Object[]) client.execute("Content.isVideoPlaying", args);
            if (Constants.FALSE.equals(result[0])
                    && Constants.NULL.equals(result[1])) {
                LOGGER.log(Level.WARNING, "No Motion Detected");
                FrameworkLogger.logFail("No Motion Detected");
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    /**
     * Checks whether audio is present in video or not.
     *
     * @return true if audio is present else false
     */
    public boolean isAudioPresent(Integer threshold, Integer timeout) {

        Object[] args = new Object[]{threshold, timeout, slotNo};
        FrameworkLogger.logStep("Checks whether audio is present in video");
        try {
            Object[] result = (Object[]) client.execute("Content.isAudioPresent", args);
            if (Constants.FALSE.equals(result[0])
                    && Constants.NULL.equals(result[1])) {
                LOGGER.log(Level.WARNING, "Audio is not present");
                FrameworkLogger.logFail("Audio is not present");
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    /**
     * Gets Array of Closed Captioning texts found on upper region of screen and lower region of text
     *
     * @param logger logger instance
     * @return true if audio is present else false
     */
    public String[] getClosedCaptioningTexts(String upperScreenRegion, String lowerScreenRegion, Logger logger) {

        Object[] args = new Object[]{upperScreenRegion, lowerScreenRegion, slotNo};
        FrameworkLogger.logStep("Getting Closed Captioning texts found on upper region of screen and lower region");
        try {
            Object[] result = (Object[]) client.execute("Content.getClosedCaptioningTexts", args);

            return new String[]{(String) result[0], (String) result[1]};
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
        return new String[]{};
    }

    /**
     * Reconnect storm test server and reserve slot
     *
     * @return
     */
    public boolean reconnectServerAndSlot() {

        boolean result = connectToServer();
        if (result) {
            result = reserveSlot();
        }
        return result;
    }

    /**
     * Set Language for OCR read operations.
     *
     * @param language
     * @return
     */
    public boolean setOCRReadLanguage(String language) {
        FrameworkLogger.logStep("set OCR Read Language" + language);
        Object[] args = new Object[]{language, slotNo};
        try {
            Object[] result = (Object[]) client.execute("OCR.setOCRReadLanguage", args);
            if ((boolean) result[0]) {
                LOGGER.log(Level.WARNING, "setOCRReadLanguage() failed, language = " + language + " not set for OCR operations");
                FrameworkLogger.logFail("setOCRReadLanguage() failed, language = " + language + " not set for OCR operations");
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "Error occurred in setOCRReadLanguage() failed, language = " + language + " not set for OCR operations");
            FrameworkLogger.logWarning("setOCRReadLanguage() failed, language = " + language + " not set for OCR operations");
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    /**
     * Press key on the configured Storm Test Remote
     *
     * @param key Key Name for the key that should be pressed.
     * @return true if successful false if the method call fails or if there is an exception
     */
    public boolean pressKeyNoWait(Enum key) {
        Object[] args = {key.toString(), slotNo};
        FrameworkLogger.logStep("Press " + key.toString());
        try {
            Object[] result = (Object[]) client.execute("PressButton.PressButtonNoWait", args);
            if (result[0] == "False") {
                LOGGER.log(Level.WARNING, "pressKeyNoWait() :: Failed to press key " + key.toString() + "error:" + result[1]);
                FrameworkLogger.logStep("Failed to press key " + key.toString() + "error:" + result[1]);
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    public String getLogDirectory() {
        return logDirectory;
    }

    public String getTestCaseFileName() {
        return testCaseFileName;
    }

    /**
     * Compare String value in given region with expected string value.
     *
     * @param namedRegion    - name of region
     * @param expectedString = expected String to be compared
     * @return true if string value at given region matches with expected string value.
     */
    public boolean compareString(String namedRegion, String expectedString) {
        return compareString(namedRegion, expectedString, 2);
    }

    /**
     * Compare String value in given region with expected string value.
     *
     * @param namedRegion    - name of region
     * @param expectedString = expected String to be compared
     * @param distance       - for logging
     * @return true if string value at given region matches with expected string value.
     */
    public boolean compareString(String namedRegion, String expectedString, int distance) {
        long startTime = System.currentTimeMillis(); //fetch starting time
        FrameworkLogger.logStep("Compare " + expectedString + " value in region " + namedRegion);
        Object[] args = new Object[]{slotNo, namedRegion, expectedString};
        while ((System.currentTimeMillis() - startTime) < 1000) {
            try {
                Object[] result = (Object[]) client.execute("OCR.compareString", args);
                LOGGER.log(Level.INFO, result[0].toString());
                if (!(boolean) result[0]) {
                    LOGGER.log(Level.INFO, "Compare String failed to match");
                    LOGGER.log(Level.WARNING, "Failed to compare string. error:" + result[1]);
                    FrameworkLogger.logFail("Failed to Compare " + expectedString + " value in region " + namedRegion + result[1]);
                } else {
                    LOGGER.log(Level.INFO, "Compare string returning true");
                    FrameworkLogger.logFail("Failed to Compare " + expectedString + " value in region " + namedRegion + result[1]);
                    return (boolean) result[0];
                }

            } catch (XmlRpcException e) {
                LOGGER.log(Level.WARNING, "", e);
                return false;
            }
        }
        return false;
    }


    /**
     * /**
     * Compares the image in given region.
     *
     * @param testRegion - region for comparing image
     * @param refImage   - image to be compared in given region
     * @return true if image is found in given region
     */
    //@Override
    public boolean compareImage(String testRegion, String refImage, Integer percentageToMatch) {
        Object[] args = new Object[]{slotNo, testRegion, refImage, percentageToMatch};
        FrameworkLogger.logStep("Compare refImage in region " + testRegion);
        try {
            Object[] result = (Object[]) client.execute("OCR.compareImage", args);
            if (result[0] == "False") {
                if (result[1] == "Null") {
                    LOGGER.log(Level.WARNING, "TestImage is not matching the refrence image");
                    FrameworkLogger.logFail("Image in " + testRegion + " is not matching the refrence image");
                } else {
                    LOGGER.log(Level.WARNING, "Failed to compare Image. error:" + result[1]);
                    FrameworkLogger.logFail("Failed to compare Image. error:" + result[1]);
                }
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }


    /**
     * Waits for element to disappear.
     *
     * @param refImage     the reference image which needs to be checked for invisibility
     * @param refImgRegion region of reference image
     * @param percent      percentage to be checked for invisibility of image
     * @param timeToWait   time to wait for invisibility check
     * @param waitGap      wait gap
     * @return true if image disappeared else false
     */
    public boolean waitImageNoMatch(String refImage, String refImgRegion, int percent, int timeToWait, int waitGap) {
        FrameworkLogger.logStep("Waits for " + refImage + " to disappear");
        if (timeToWait == 0)
            timeToWait = 5;
        if (waitGap == 0)
            waitGap = 1;
        Object[] args = new Object[]{refImage, refImgRegion, percent, timeToWait, waitGap, slotNo};
        try {
            Object[] result = (Object[]) client.execute("OCR.waitImageNoMatch", args);
            LOGGER.log(Level.INFO, "waitImageNoMatch :: " + refImage + ", result = " + result[0]);
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    /**
     * Finds String from given region
     *
     * @param namedRegion - name of region
     * @return string value in given region
     */
    public String getString(String namedRegion) {
        Object[] args = new Object[]{slotNo, namedRegion};
        try {
            FrameworkLogger.logStep("Finds String in region " + namedRegion);
            Object[] result = (Object[]) client.execute("OCR.findString", args);
            LOGGER.log(Level.WARNING, "Result : " + result[0]);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "Failed to find string. error:" + result[1]);
                FrameworkLogger.logFail("Failed to find string. error:" + result[1]);
            } else {
                if (!result[0].toString().isEmpty() && result[0] != null) {
                    FrameworkLogger.logDebug("Result : " + result[0]);
                    return String.valueOf(result[0]);
                }
            }


        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return "Error Received";
        }
        return "";
    }

    //@Override
    public boolean SendVoiceCommand(String voiceCommand) {
        return false;
    }

    /**
     * Get Directory Name
     *
     * @return directory name
     */
    public String getDirectoryName() {
        return "";
    }

    /**
     * Checks whether video is playing or not.
     *
     * @param timeout check for this duration value (seconds)
     * @return true if video is playing or not
     */
    public boolean isVideoPlaying(String regionName, int timeout) {
        FrameworkLogger.logStep("Checks whether video is playing");
        Object[] args = new Object[]{timeout, slotNo};
        try {
            Object[] result = (Object[]) client.execute("Content.isVideoPlaying", args);
            if (Constants.FALSE.equals(result[0])
                    && Constants.NULL.equals(result[1])) {
                LOGGER.log(Level.WARNING, "No Motion Detected");
                FrameworkLogger.logFail("No Motion Detected");
            }
            FrameworkLogger.logPass("Video is playing Motion Detected");
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    //@Override
    public String detectLanguage(String ocrText) {
        return "";
    }

    /**
     * This method is used to make a call to method - calculateAvmos in python using rpcXmlServer.
     *
     * @return
     */
    public boolean generateAVMOSData(String outputFile, String mosType) {
        boolean result = false;
        Object[] args = new Object[]{outputFile, mosType, slotNo};
        try {
            LOGGER.log(Level.INFO, "Calling generateAVMOSData() python using rpcXmlServer");
            LOGGER.log(Level.INFO, "Params" + outputFile + "  MOS type" + mosType);
            FrameworkLogger.logStep("Generating AVMOS Data");
            client.execute("KPI.calculateAvmos", args);
            result = true;
        } catch (XmlRpcException e) {
            result = false;
            LOGGER.log(Level.WARNING, "Error while calling generateAVMOSData() python using rpcXmlServer");
            FrameworkLogger.logStep("Error while generating AVMOS Data");
            LOGGER.log(Level.WARNING, "", e);
        }
        return result;
    }

    /**
     * Read file generated in VQA analysis to get mos value.
     *
     * @param vqaResultFile
     * @return list of records read from vqa result file
     * @throws IOException
     */
    private List<CSVRecord> readVqaResults(String vqaResultFile) throws IOException {
        CSVParser parser = new CSVParser(new FileReader(vqaResultFile), CSVFormat.DEFAULT.withHeader());
        ArrayList<CSVRecord> csvRecordList = new ArrayList<>();
        for (CSVRecord record : parser) {
            csvRecordList.add(record);
        }
        parser.close();
        return csvRecordList;
    }

    public boolean compareImageHighThreshold(String testRegion, String refImage) {
        Object[] args = new Object[]{slotNo, testRegion, refImage};
        try {
            FrameworkLogger.logStep("Comparing image with reference image");
            Object[] result = (Object[]) client.execute("OCR.compareImage", args);
            if (Constants.FALSE.equals(result[0])) {
                if (Constants.NULL.equals(result[1])) {
                    LOGGER.log(Level.WARNING, "TestImage is not matching the refrence image");
                    FrameworkLogger.logFail("Image is not matching with the reference image");
                } else {
                    LOGGER.log(Level.WARNING, "Failed to compare Image. error:" + result[1]);
                    FrameworkLogger.logWarning("Failed to compare Image. error:" + result[1]);
                }
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }

    /**
     * Checks whether audio is present in video or not.
     *
     * @return true if audio is present else false
     */
    public Object[] isAudioPresentObj(Integer threshold, Integer timeout) {
        FrameworkLogger.logStep("Checks whether audio is present in video");
        Object[] args = new Object[]{threshold, timeout, slotNo};
        try {
            Object[] result = (Object[]) client.execute("Content.isAudioPresent", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "Audio is not present");
                FrameworkLogger.logFail("Audio is not present");
            }
            FrameworkLogger.logDebug("Audiolevel is " + result[1].toString());
            FrameworkLogger.logDebug("Audiolevel is " + result[1].toString());
            return result;
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            Object[] result = new Object[]{false, ""};
            return result;
        }
    }

    //@Override
    public boolean waitForOCRMatch(String namedRegion, String expectedString, Integer distance, Integer timeOut, Logger logger) {
        return false;
    }

    /**
     * Press key on the configured Storm Test Remote
     *
     * @param key Key Name for the key that should be pressed.
     * @return true if successful false if the method call fails or if there is an exception
     */
    public boolean pressButton(Enum key) {
        Object[] args = {key.toString(), slotNo};
        FrameworkLogger.logStep("Press key on the configured Storm Test Remote");
        try {
            Object[] result = (Object[]) client.execute("PressButton.PressKey", args);
            if (Constants.FALSE.equals(result[0])) {
                LOGGER.log(Level.WARNING, "Failed to press key" + key.toString() + "error:" + result[1]);
                FrameworkLogger.logFail("Failed to press key" + key.toString() + "error:" + result[1]);
            }
            return (boolean) result[0];
        } catch (XmlRpcException e) {
            LOGGER.log(Level.WARNING, "", e);
            return false;
        }
    }
}