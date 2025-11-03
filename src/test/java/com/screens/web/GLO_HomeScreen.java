package com.screens.web;

import com.gl.testngfw.api.ScreenPattern;
import com.gl.testngfw.api.UiControl;
import com.gl.testngfw.api.WebUiControl;
import com.gl.testngfw.enums.ElementType;
import com.gl.testngfw.logging.FrameworkLogger;

public class GLO_HomeScreen extends ScreenPattern {
    private static final String MYCOMMUNITIES = "//div[text()='My Communities']";
    private static final String SHARE = "//div[@id='my-communities']/h2i";

    public WebUiControl getMyCommunitiesText() {
        return getUIControl(MYCOMMUNITIES, "Web: Common Button", ElementType.BY_XPATH);
    }
    public WebUiControl getMyCommunitiesOption() {
        return null;
    }

    public WebUiControl getShareText() {
        return getUIControl(SHARE, "Web: Common Button", ElementType.BY_XPATH);
    }

    public boolean VerifyMyCommunities() {
        try {
            return getMyCommunitiesText().isFound(10);
        } catch (Exception e) {
            FrameworkLogger.logWarning("Unable to find My Communities text");
            return false;
        }
    }

    public boolean VerifyShare() {
        try {
            return getShareText().isFound(5);
        } catch (Exception e) {
            FrameworkLogger.logWarning("Unable to find My Communities text");
            return false;
        }
    }
    public UiControl getCommunitiesOption() {
        return null;
    }
}
