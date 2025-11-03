package com.gl.testngfw.api;

import com.gl.testngfw.setup.InitializerScript;

import java.util.HashMap;
import java.util.Map;

/**
 * Class represents abstract container on the android UI.
 * All other controls extend this class since any control might contain other(s) inside
 */
abstract class Container {
    private String className = "";
    private Map<String, Object> controlMap = new HashMap<>();

    /**
     * Gets the cached control from the screen.
     *
     * @param uiControl UIControl Class
     * @return the cached control.
     */
    UiControl getCachedControl(UiControl uiControl) {
        String id = uiControl.getElementIdentifier();
        clearData(uiControl);
        className = this.getClass().getName();
        if (!controlMap.containsKey(id)) {
            controlMap.put(id, uiControl);
        }
        return (UiControl) controlMap.get(id);
    }


    /**
     * Clears all the cached UI controls from screen.
     * It is recommended to call that method after navigating away from the screen.
     */
    private void clearCache() {
        if (controlMap != null) {
            controlMap.clear();
        }
    }

    /**
     * Clears all the cached UI controls from screen.
     * It is recommended to call that method after navigating away from the screen.
     */
    private void clearData(UiControl uiControl) {
        if (!controlMap.isEmpty() && !className.equals(this.getClass().getName())) {
            clearCache();
        } else if (InitializerScript.isWeb() && uiControl.webDriver.getSessionId() != InitializerScript.getWebDriver().getSessionId()) {
            clearCache();
        } else if (InitializerScript.isMobile() && uiControl.driver.getSessionId() != InitializerScript.getDriver().getSessionId()) {
            clearCache();
        }
    }
}
