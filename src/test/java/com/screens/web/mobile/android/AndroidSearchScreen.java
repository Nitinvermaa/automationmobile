package com.screens.web.mobile.android;

import com.gl.testngfw.api.MobileUiControl;
import com.gl.testngfw.api.ScreenPattern;
import com.gl.testngfw.enums.ElementType;
import com.gl.testngfw.logging.FrameworkLogger;

/**
 * Created by rashmi.z on 3/24/2017.
 */
public class AndroidSearchScreen extends ScreenPattern {
    private static final String SEARCH_BAR = "app.globallogic.com.glo:id/action_search";
    private static final String SEARCH_TEXTBOX = "app.globallogic.com.glo:id/searchKeyword";
    private static final String SEARCH_BUTTON = "app.globallogic.com.glo:id/search_icon_IV";
    private static final String SEARCH_RESULTS = "app.globallogic.com.glo:id/noresult_found_bg";

    public MobileUiControl getSearchBar() {
        return getUIControl(SEARCH_BAR, "Mob: Search Field", ElementType.BY_ID);
    }


    public MobileUiControl getSearchTextBox() {
        return getUIControl(SEARCH_TEXTBOX, "Mob: Search Text Box", ElementType.BY_ID);
    }

    public MobileUiControl getSearchButton() {
        return getUIControl(SEARCH_BUTTON, "Mob: Search Button", ElementType.BY_ID);
    }

    public MobileUiControl getSearchNoResults() {
        return getUIControl(SEARCH_RESULTS, "Mob: Search Result", ElementType.BY_ID);
    }


    public boolean verifySearch(String search) {

        try {
            if (!getSearchBar().isFound(20))
                return false;
            getSearchBar().click();
            getSearchTextBox().sendText(search);
            getSearchButton().click();
            return getSearchNoResults().isFound(10);
        } catch (Exception e) {
            FrameworkLogger.logWarning("Unable to enter search text");
            return false;
        }
    }
}

