package com.gl.testngfw.setup.stromtest;

import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.setup.interfaces.VideoManager;

public class StormTestVideo implements VideoManager {

    @Override
    public void initVideo() {
        InitializerScript.getStormTestDriver().startVideoLog(InitializerScript.getTestCase().getTestMethodName());
    }

    @Override
    public void teardown() {
        InitializerScript.getStormTestDriver().stopVideoLog();
    }
}
