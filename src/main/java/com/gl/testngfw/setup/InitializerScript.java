package com.gl.testngfw.setup;

import com.gl.testngfw.common.DBUpdater;
import com.gl.testngfw.datadriven.model.TestDataRecord;
import com.gl.testngfw.datadriven.reader.TestDataReader;
import com.gl.testngfw.enums.ExecutorType;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.logging.DriverNotCreated;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.report.model.Device;
import com.gl.testngfw.report.model.Execution;
import com.gl.testngfw.report.model.Result;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Initializer class
 */
public class InitializerScript extends JunitInitializer {
    private static final Logger LOGGER = Logger.getLogger(InitializerScript.class.getName());
    private Execution execution;
    private PlatformManager driverManager;

    @BeforeSuite()
    @Parameters({"platform", "executionType"})
    public synchronized void startServer(@Optional String platform, @Optional String executionType) throws Exception {
        LOGGER.log(Level.INFO, "******In BeforeSuite *********************");
        deviceInfoMap = TestExecutor.getDeviceInfoMap();
        String currentPlatform = getExecutionPlatformName(platform);
        execution = initExecution(executionType, currentPlatform);
        dbUpdater = getConfigData().isUseDB() ? new DBUpdater() : null;
        if (!TestExecutor.isIsGridStarted() && !getConfigData().isGafCloud() &&
                (getConfigData().getExecuteFor().contains(Platforms.WEB) ||
                        getConfigData().getExecuteFor().contains(Platforms.MOBILE))) {
            GridInitializer.startServer();
        } else if (getConfigData().getExecuteFor().contains(Platforms.STB) &&
                executionType.equalsIgnoreCase(ExecutorType.STORMTEST.name())) {
            Device executionDevice = TestExecutor.getExecutionRecord().getTestCases().get(platform).getDevice();
            StormTestUtils.connectStormTestServerAndReserveSlot(executionDevice.getDeviceIp(), Integer.valueOf(executionDevice.getSlotNumber()));
        }
    }

    @BeforeClass(alwaysRun = true)
    @Parameters({"platform", "executionType"})
    public void setup(@Optional String platform, @Optional String executionType) {
        LOGGER.log(Level.INFO, "******In BeforeClass *********************");
        try {
            setExecutionData(platform, executionType, execution);
            dbUpdater = getConfigData().isUseDB() ? new DBUpdater() : null;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "BeforeClass failed", e);
        }
    }


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(ITestContext context, Method method, Object[] params) throws Exception {
        LOGGER.log(Level.INFO, "******In Before Method *********************");
        try {
            updateConfiguration();
            getExecutionContainer().setThreadLocalResult(new Result());
            getExecutionContainer().setLocalExecution(execution);
            initTestCase(method, params);
            initTestData(getTestCase().getDataType(), getTestCase().getTestDataPath());
            driverManager = new PlatformManager(getCurrentPlatform());
            driverManager.init();
            if (isDriverCreated()) {
                FrameworkLogger.logDebug("Initializing " + getTestCase().getTestMethodName() + "test");
                initScreens();
            } else {
                FrameworkLogger.logWarning("Failed to create driver");
                throw new DriverNotCreated("Failed to create driver");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "BeforeClass Method", e);
        }
    }


    @AfterMethod()
    public synchronized void tearDown(ITestResult testResult) {
        LOGGER.log(Level.INFO, "******In After Method *********************");
        try {
            if (isDriverCreated()&&!testResult.isSuccess()) {
                FrameworkLogger.logFail(testResult.getThrowable().getMessage());
            }
            driverManager.tearDown(testResult.getStatus());
            if (getConfigData().isUseDB()) {
                dbUpdater.postExecutionStatus(getResultContainer().getExecutionPercentage(testResult));
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "After Method failed", e);
        }
    }


    @AfterClass()
    public void afterClass() {
        LOGGER.log(Level.INFO, "******In After Test *********************");
    }

    @AfterSuite()
    public void afterSuite() {
        try {
            LOGGER.log(Level.INFO, "******In After Suite *********************");

            if (!isDriverCreated()) {
                LOGGER.log(Level.INFO, "Driver is not created " + getExecutionContainer().getExecutionPlatform());
            }

            stopAppium();

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "After Suite failed", e);
        } finally {
            List<Execution> suiteList = getResultContainer().writeExecutionResult(false);
            if (getConfigData().isUseDB()) {
                dbUpdater.postExecutionData(suiteList);
                getResultContainer().postExecutionStatus();
            }
            exitGrid();
            if (getExecutionContainer().isStormTest()) {
                getStormTestDriver().releaseSlot();
            }
        }
    }


    /**
     * Method to get test data for data driven test
     *
     * @return Test data record list in Array
     */
    public Object[][] getTestData() throws ParseException {
        String path = getUserDir() + "/resources/" + getTestCase().getTestDataPath();
        TestDataReader dataReader = getTestDataReaderByDataType(getTestCase().getDataType());
        dataContainer = dataReader != null ? dataReader.readTestData(path) : null;
        List<TestDataRecord> testDataList = dataContainer != null ? dataContainer.getTestDataList() : null;
        Object[][] data = new Object[0][];
        if (testDataList != null) {
            data = new Object[testDataList.size()][1];
            for (int i = 0; i < testDataList.size(); i++) {
                TestDataRecord testDataRecord = testDataList.get(i);
                data[i] = new Object[1];
                data[i][0] = testDataRecord.getValueByIndex(0).getValue();
            }
        }
        return data;
    }
}