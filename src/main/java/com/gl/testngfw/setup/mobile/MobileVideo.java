package com.gl.testngfw.setup.mobile;

import com.gl.testngfw.setup.BaseInitializer;
import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.setup.ResultContainer;
import com.gl.testngfw.setup.interfaces.VideoManager;
import com.gl.testngfw.utility.Util;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.screenrecording.ScreenRecordingUploadOptions;

import java.io.File;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MobileVideo implements VideoManager {
    private static final Logger LOGGER = Logger.getLogger(MobileVideo.class.getName());

    @Override
    public void initVideo() {
        if (BaseInitializer.getConfigData().isRecordVideo()) {
            if (InitializerScript.isAndroid()) {
                InitializerScript.getAndroidDriver().startRecordingScreen(
                        new AndroidStartScreenRecordingOptions().
                                withUploadOptions(ScreenRecordingUploadOptions.uploadOptions()).
                                withBitRate(5000000).
                                withVideoSize("720x1280").
                                withTimeLimit(Duration.ofSeconds(180)));
            } else if (InitializerScript.isIOS()) {
                InitializerScript.getIOSDriverDriver().startRecordingScreen();
            }
        }
    }

    @Override
    public void teardown() {
        getRecodedMobileVideo();
    }

    private void getRecodedMobileVideo() {
        if (BaseInitializer.getConfigData().isRecordVideo()) {
        ResultContainer resultContainer = InitializerScript.getExecutionContainer().getLocalResultContainer();
        if (InitializerScript.isDriverCreated()) {
            try {
                if (InitializerScript.getConfigData().isRecordVideo()) {
                    String path = resultContainer.getReportDirPath() + "/Videos/";
                    Util.createDirectory(path);
                    resultContainer.setVideoPath(path + InitializerScript.getTestCase().getTestMethodName() + Util.randomString(5) + ".mp4");
                    if (InitializerScript.isAndroid()) {
                        Util.getDecoded(InitializerScript.getAndroidDriver().stopRecordingScreen(), new File(resultContainer.getVideoPath()));
                    } else if (InitializerScript.isIOS()) {
                        Util.getDecoded(InitializerScript.getIOSDriverDriver().stopRecordingScreen(), new File(resultContainer.getVideoPath()));
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "", e);
            }
        }
        }
    }
}
