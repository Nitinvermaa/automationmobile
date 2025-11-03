package com.screens.web;


import com.gl.testngfw.api.ScreenPattern;
import com.gl.testngfw.api.WebUiControl;
import com.gl.testngfw.enums.ElementType;
import com.gl.testngfw.logging.FrameworkLogger;

public class ManagementPortalScreen extends ScreenPattern {
    private static final String USERNAME = "loginForm:username";
    private static final String PASSWORD_FIELD = "loginForm:pwd";
    private static final String LOGIN = "//*[@title='Click to login']";
    private static final String HomePage = "login-container-heading";

    private static final String FIND_DEVICE = "//*[text() = 'Device Class']";


    private static final String SEARCH_FIELD = "dataTableId:globalFilter";

    private static final String CARRIER_BAND = "j_idt77_label";
    private static final String SPRINT_7 = "//*[@data-label='[7] Sprint -  Sprint']";

    private static final String SEARCH_BOX = "dataTableId:globalFilter";
    private static final String SEARCH_RESULT = "//*[@id='dataTableId_data']/tr";

    private static final String DEVICE_INFO = "//*[@id='j_idt131']/tbody/tr/td";

    /*T-Mobile website Locators*/

    private static final String REJECT_BTN = "//*[@id='onetrust-reject-all-handler']";

    private static final String MY_ACCOUNT_BTN = "//*[@aria-label='My account']";

    private static final String LOGIN_BTN = "//*[text()='Login']";

    private static final String USER_NAME_TEXTBOX = "//*[@aria-label='Email or phone number']";

    private static final String NEXT_BTN = "//*[@id='lp1-next-btn']";

    private static final String PASSWORD_TEXTBOX = "//*[@aria-label='Password']";

    private static final String LOGIN_BTN2 = "//*[@id='lp2-login-btn']";

    private static final String SEND_CODE_BTN = "//*[text()='Text a code to ']";






    public WebUiControl getRejectBtn() {
        return getUIControl(REJECT_BTN, "Web: Reject BTN", ElementType.BY_XPATH);
    }

    public WebUiControl getSendCodeBtn() {
        return getUIControl(SEND_CODE_BTN, "Web: Send code BTN", ElementType.BY_XPATH);
    }

    public WebUiControl getMyAccountBtn() {
        return getUIControl(MY_ACCOUNT_BTN, "Web: Account BTN", ElementType.BY_XPATH);
    }

    public WebUiControl getLoginBtn() {
        return getUIControl(LOGIN_BTN, "Web: Login BTN", ElementType.BY_XPATH);
    }


    public WebUiControl getUserNameTextBox() {
        return getUIControl(USER_NAME_TEXTBOX, "Web: UserName BTN", ElementType.BY_XPATH);
    }

    public WebUiControl getNextBtn() {
        return getUIControl(NEXT_BTN, "Web: NEXT_BTN BTN", ElementType.BY_XPATH);
    }


    public WebUiControl getPasswordTextBox() {
        return getUIControl(PASSWORD_TEXTBOX, "Web: PASSWORD_TEXTBOX BTN", ElementType.BY_XPATH);
    }

    public WebUiControl getLoginBtn2() {
        return getUIControl(LOGIN_BTN2, "Web: LOGIN_BTN2 BTN", ElementType.BY_XPATH);
    }










    public WebUiControl getUserName() {
        return getUIControl(USERNAME, "Web: User Name Field", ElementType.BY_ID);
    }

    public WebUiControl getPassword() {
        return getUIControl(PASSWORD_FIELD, "Web: Password Field", ElementType.BY_ID);
    }

    public WebUiControl getLoginButton() {
        return getUIControl(LOGIN, "Web: Common Button", ElementType.BY_XPATH);
    }

    public WebUiControl getHomePageHeading() {
        return getUIControl(HomePage, "Web: Home Page Heading", ElementType.BY_ID);
    }


    public WebUiControl getDeviceClass() {
        return getUIControl(FIND_DEVICE, "Web: Device Class", ElementType.BY_XPATH);
    }

    public WebUiControl getSearchField() {
        return getUIControl(SEARCH_FIELD, "Web: Search Field", ElementType.BY_XPATH);
    }

    public WebUiControl getCARRIER() {
        return getUIControl(CARRIER_BAND, "Web: CARRIER Field", ElementType.BY_ID);

    }


    public WebUiControl getSprint7() {
        return getUIControl(SPRINT_7, "Web: SPRINT_7", ElementType.BY_XPATH);
    }

    public WebUiControl getCarrierOption(String option) {
        return getUIControl("//*[@data-label='"+option+"']", "Web: Carrier Option", ElementType.BY_XPATH);
    }


    public WebUiControl getSearchBox() {
        return getUIControl(SEARCH_BOX, "Web: Carrier Option", ElementType.BY_ID);
    }

    public WebUiControl getSearchResult() {
        return getUIControl(SEARCH_RESULT, "Web: Carrier Option", ElementType.BY_XPATH);
    }

    public WebUiControl getDeviceInfo() {
        return getUIControl(DEVICE_INFO, "Web: Carrier Option", ElementType.BY_XPATH);
    }
}
