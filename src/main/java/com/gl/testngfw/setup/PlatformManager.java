package com.gl.testngfw.setup;

import com.gl.testngfw.enums.ExecutorType;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.setup.api.APIDriver;
import com.gl.testngfw.setup.api.APILogger;
import com.gl.testngfw.setup.api.ApiVideo;
import com.gl.testngfw.setup.contract.ContractDriver;
import com.gl.testngfw.setup.contract.ContractLogger;
import com.gl.testngfw.setup.contract.ContractVideo;
import com.gl.testngfw.setup.interfaces.DriverManager;
import com.gl.testngfw.setup.interfaces.LogManager;
import com.gl.testngfw.setup.interfaces.PlatformInitializer;
import com.gl.testngfw.setup.interfaces.VideoManager;
import com.gl.testngfw.setup.mobile.MobileDriver;
import com.gl.testngfw.setup.mobile.MobileLogger;
import com.gl.testngfw.setup.mobile.MobileVideo;
import com.gl.testngfw.setup.stromtest.StormTestDriver;
import com.gl.testngfw.setup.stromtest.StormTestLogger;
import com.gl.testngfw.setup.stromtest.StormTestVideo;
import com.gl.testngfw.setup.web.WebDriver;
import com.gl.testngfw.setup.web.WebLogger;
import com.gl.testngfw.setup.web.WebVideo;
import com.gl.testngfw.utility.Util;

public class PlatformManager implements PlatformInitializer {
    private DriverManager driverManager;
    private LogManager logManager;
    private VideoManager videoManager;

    PlatformManager(String executionType) {
        Platforms platform = Util.getPlatform(executionType);
        switch (platform) {
            case MOBILE:
                driverManager = new MobileDriver();
                logManager = new MobileLogger();
                videoManager = new MobileVideo();
                break;
            case WEB:
                driverManager = new WebDriver();
                logManager = new WebLogger();
                videoManager = new WebVideo();
                break;
            case API:
                driverManager = new APIDriver();
                logManager = new APILogger();
                videoManager = new ApiVideo();
                break;
            case CONTRACT:
                driverManager = new ContractDriver();
                logManager = new ContractLogger();
                videoManager = new ContractVideo();
                break;
            case LOAD_TESTING:
                break;
            case PERFORMANCE:
                break;
            case STB:
                if(InitializerScript.getTestCase().getExecutorType().equals(ExecutorType.STORMTEST)) {
                    driverManager = new StormTestDriver();
                    logManager = new StormTestLogger();
                    videoManager = new StormTestVideo();
                }
                break;
            case ROKU:
                break;
            case DESKTOP:
                break;
            default:
                break;
        }
    }

    @Override
    public synchronized void init() {
        driverManager.initDriver();
        logManager.initLogger();
        videoManager.initVideo();
    }

    @Override
    public synchronized void tearDown(int status) {
        logManager.teardown();
        videoManager.teardown();
        driverManager.teardown(status);
    }
}
