package com.gl.testngfw.api;

import com.gl.testngfw.enums.ElementType;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.utility.Util;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Objects;

/**
 * Base class for screen object classes
 */
public class ScreenPattern extends Container {

    public AppiumDriver getDriver() {
        return InitializerScript.getDriver();
    }

    public RemoteWebDriver getWebDriver() {
        return InitializerScript.getWebDriver();
    }

    protected <T>T getUIControl(String id, String elementLabel, ElementType type) {
        Platforms platform = Util.getPlatform(InitializerScript.getCurrentPlatform());
        UiControl control;
        switch (platform) {
            case MOBILE:
                control = getCachedControl(new MobileUiControl(id, elementLabel, type));
                return (T)((MobileUiControl)control);
            case WEB:
                control= getCachedControl(new WebUiControl(id, elementLabel, type));
                return (T) ((WebUiControl) control);
            case STB:
                control= getCachedControl(new STBUiControl(id, elementLabel, type));
                return (T) ((STBUiControl) control);
            case ROKU:
                control= getCachedControl(new RokuUiControl(id, elementLabel, type));
                return (T) ((RokuUiControl) control);
            default:
                return null;
        }
    }
}
