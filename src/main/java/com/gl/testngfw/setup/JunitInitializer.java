package com.gl.testngfw.setup;

import com.gl.testngfw.common.DBUpdater;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.logging.DriverNotCreated;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.report.model.ConfigData;
import com.gl.testngfw.report.model.Execution;
import com.gl.testngfw.report.model.Result;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JunitInitializer extends BaseInitializer {
    private static final Logger LOGGER = Logger.getLogger(JunitInitializer.class.getName());
    private static Execution execution;
    public static PlatformManager platformManager;
    private static int passCount = 0;
    private static int failCount = 0;
    private static int skipCount = 0;

    static {
        try {
            if (JunitConfig.getLocalTotalTestCase() == null) {
                setExecutionContainer(new ExecutionContainer());
                getExecutionContainer().setLocalConfigData(new ConfigData());
                new JunitConfig(getConfigData().getExecuteFor().get(0).name(), getConfigData().getExecutionPlatform(), String.valueOf(1));
                String currentPlatform = getExecutionPlatformName(JunitConfig.getLocalPlatform());
                execution = initExecution(JunitConfig.getLocalExecutionType(), currentPlatform);
            } else if(!JunitConfig.getLocalTotalTestCase().isEmpty()){
                deviceInfoMap = TestExecutor.getDeviceInfoMap();
                String currentPlatform = getExecutionPlatformName(JunitConfig.getLocalPlatform());
                execution = initExecution(JunitConfig.getLocalExecutionType(), currentPlatform);
                dbUpdater = getConfigData().isUseDB() ? new DBUpdater() : null;
                if (!TestExecutor.isIsGridStarted() && !getConfigData().isGafCloud() &&
                        (getConfigData().getExecuteFor().contains(Platforms.WEB) ||
                                getConfigData().getExecuteFor().contains(Platforms.MOBILE))) {
                    GridInitializer.startServer();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Rule
    public TestName testName = new TestName();
    @Rule
    public final TestRule watchman = new TestWatcher() {
        protected void succeeded(Description description) {
            passCount++;
            FrameworkLogger.logPass(description.getDisplayName() + " Test Passed");
            platformManager.tearDown(1);

        }

        protected void failed(Throwable e, Description description) {
            platformManager.tearDown(2);
            failCount++;
            String message = e.getMessage();
            getLocalResult().setErrorMessage(message);
            getLocalResult().setStatus("Fail");
            String currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new java.util.Date(System.currentTimeMillis()));
            getLocalResult().setEndTime(currentDate);
            FrameworkLogger.logFail(description.getDisplayName() + " Test Failed " + message);
            LOGGER.log(Level.INFO, "Execution of test case failed : " + description.getMethodName() + e.getMessage());
        }

        protected void skipped(org.junit.AssumptionViolatedException e, Description description) {
            this.skipped((org.junit.internal.AssumptionViolatedException) e, description);
            platformManager.tearDown(3);
            skipCount++;
            String currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new java.util.Date(System.currentTimeMillis()));
            getLocalResult().setEndTime(currentDate);
            getLocalResult().setStatus("Skip");
            LOGGER.log(Level.INFO, "Execution of test case ignored : " + description.getMethodName());
        }

        /** @deprecated */
        @Deprecated
        protected void skipped(org.junit.internal.AssumptionViolatedException e, Description description) {
        }

        protected void starting(Description description) {
            LOGGER.log(Level.INFO, "Test Run Started");
        }

        protected void finished(Description description) {
            if (JunitInitializer.getConfigData().isUseDB()) {
                dbUpdater.postExecutionStatus(getResultContainer().getExecutionPercentage(passCount, failCount, skipCount));
            }
        }
    };

    @BeforeClass
    public static void setup() {
        String type = JunitConfig.getLocalExecutionType().isEmpty() ? getConfigData().getExecuteFor().get(0).name() : JunitConfig.getLocalExecutionType();
        setExecutionData(JunitConfig.getLocalPlatform(), type, execution);
        getResultContainer().setTotalTestCount(Integer.valueOf(JunitConfig.getLocalTotalTestCase()));
        getResultContainer().setReportMap(JunitConfig.getLocalPlatform());
    }

    @AfterClass
    public static void tear() {
        LOGGER.log(Level.INFO, "******In After Suite *********************");
        if (JunitConfig.getLocalTotalTestCase().isEmpty() || getExecutionContainer().getLocalExecution().getResultsList().size() == Integer.parseInt(JunitConfig.getLocalTotalTestCase())) {
            teardown();
        }
    }

    public static void teardown() {
        try {
            stopAppium();
        } catch (Exception ignored) {
        } finally {
            List<Execution> suiteList = getResultContainer().writeExecutionResult(false);
            if (getConfigData().isUseDB()) {
                dbUpdater.postExecutionData(suiteList);
                getResultContainer().postExecutionStatus();
            }
            exitGrid();
        }

    }

    @Before
    public void setupThis() {
        LOGGER.log(Level.INFO, "******In Before Method *********************");
        try {
            updateConfiguration();
            getExecutionContainer().setThreadLocalResult(new Result());
            getExecutionContainer().setLocalExecution(execution);
            initTestCase(getMethod(testName.getMethodName()), new Object[0]);
            initTestData(getTestCase().getDataType(), getTestCase().getTestDataPath());
            platformManager = new PlatformManager(getCurrentPlatform());
            platformManager.init();
            if (isDriverCreated()) {
                FrameworkLogger.logDebug("Initializing " + getTestCase().getTestMethodName() + "test");
                initScreens();
            } else {
                FrameworkLogger.logWarning("Failed to create driver");
                throw new DriverNotCreated("Failed to create driver");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Before Method failed", e);
        }
    }

    private Method getMethod(String methodName) {
        Method[] declaredMethods = this.getClass().getDeclaredMethods();
        Method method = null;
        for (Method meth : declaredMethods) {
            if (meth.getName().equals(methodName)) {
                method = meth;
            }
        }
        return method;
    }

    @After
    public void tearThis() throws IOException {
        try {
            LOGGER.log(Level.INFO, "******In After Method *********************");
            //platformManager.tearDown();
          /*  FrameworkLogger.setEnableAPIFileLogging(false);
            stopWebVideoRecording();
            captureDeviceLogs();
            getRecodedMobileVideo();
            exceptionHandling();
            quitDriver();
            reInitialiseAppiumServer();
            Executor.closeIdleConnections();*/
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "After Method Failed", e);
        }
    }
}
