package com.gl.testngfw.setup.web;

import com.gl.testngfw.setup.ExecutionContainer;
import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.setup.ResultContainer;
import com.gl.testngfw.setup.interfaces.VideoManager;
import com.gl.testngfw.utility.Util;
import com.gl.testngfw.utility.VideoRecord;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebVideo implements VideoManager {
    private static final Logger LOGGER = Logger.getLogger(WebVideo.class.getName());

    private ExecutionContainer executionContainer = InitializerScript.getExecutionContainer();
    private ResultContainer resultContainer = InitializerScript.getExecutionContainer().getLocalResultContainer();
    private VideoRecord videoRecord = new VideoRecord();


    @Override
    public void initVideo() {
        if (InitializerScript.getConfigData().isRecordVideo()&& InitializerScript.isDriverCreated()) {
            String videoName = executionContainer.getLocalTestContext().getTestMethodName() + Util.randomString(5);
            resultContainer.setVideoPath(resultContainer.getReportDirPath() + "/Videos/" + videoName + ".avi");
            LOGGER.log(Level.INFO, "Video Path :: " + resultContainer.getReportDirPath() + "/Videos ----Test Case Name ::" + videoName);
            videoRecord.startRecording(new File(resultContainer.getReportDirPath() + "/Videos/"), videoName);
        }
    }

    @Override
    public void teardown() {
        if (InitializerScript.getConfigData().isRecordVideo()&& InitializerScript.isDriverCreated()) {
            try {
                videoRecord.stopRecording();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
