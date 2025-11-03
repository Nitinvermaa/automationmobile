package com.screens.web;

import com.gl.testngfw.api.ScreenPattern;
import com.gl.testngfw.api.WebUiControl;
import com.gl.testngfw.enums.ElementType;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.logging.Verify;

public class GLO_SearchScreen extends ScreenPattern {
    private static final String SEARCH_BAR = "//input[@id='search']";
    private static final String SEARCH_BUTTON = "//a[@class='btn btn-default search-btn']";
    private static final String SEARCH_NO_RESULTS = "//*[@class='list-unstyled']//p[contains(text(),'No matches found for ')]";
    private static final String SEARCH_RESULTS = " //*[@id=\"results\"]//p[contains (text(), 'Nitin Krishnan Unni')]";
    private static final String SEARCH_TILE = "//*[@id=\"profile-layer\"]//*[@title='Nitin Unni']";

    public WebUiControl getSearchBar() {
        return getUIControl(SEARCH_BAR, "Web: Search Bar", ElementType.BY_XPATH);
    }

    public WebUiControl getSearchButton() {
        return getUIControl(SEARCH_BUTTON, "Web: Search Button", ElementType.BY_XPATH);
    }

    public WebUiControl getSearchNoResults() {
        return getUIControl(SEARCH_NO_RESULTS, "Web: No Search Result Found", ElementType.BY_XPATH);
    }
    public WebUiControl getSearchResults() {
        return getUIControl(SEARCH_RESULTS, "Web: Search Result Found", ElementType.BY_XPATH);
    }
    public WebUiControl getSearchTitle() {
        return getUIControl(SEARCH_TILE, "Web: Search Result Found", ElementType.BY_XPATH);
    }
    public void verifySearch(String search) {
        try {
            if (!getSearchBar().isFound(20))
               // return false;
            getSearchBar().click();
            Thread.sleep(2000);
            getSearchBar().sendText(search);
            Thread.sleep(3000);
            //getSearchButton().click();
           // return getSearchNoResults().isFound(20);
            getSearchResults().isFound(20);
            getSearchResults().click();
            Thread.sleep(3000);
            String people = getSearchBar().getText();
            String searchResultsTitle = getSearchTitle().getText();
            Verify.verifyEquals(people, searchResultsTitle);
        } catch (Exception e) {
            FrameworkLogger.logWarning("Unable to enter search text");
            //return false;
        }
    }
}
