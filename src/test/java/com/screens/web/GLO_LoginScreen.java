package com.screens.web;


import com.gl.testngfw.api.ScreenPattern;
import com.gl.testngfw.api.WebUiControl;
import com.gl.testngfw.enums.ElementType;
import com.gl.testngfw.logging.FrameworkLogger;

public class GLO_LoginScreen extends ScreenPattern {
    private static final String USERNAME = "//input[@id='login']";
    private static final String PASSWORD_FIELD = "//input[@name='password']";
    private static final String LOGIN = "/*//*[@type='submit']";
    private static final String ERROR_MESSAGE = "/*//*[@class=' alert alert-danger']";
    private static final String HOME_MENU = "//a[@title='My Profile']";
    private static final String GLO_TOUR = "//div[@class='slide-intro']";
    private static final String SKIP_BUTTON = "//a[@id='slideSkip']";

    public WebUiControl getUserName() {
        return getUIControl(USERNAME, "Web: User Name Field", ElementType.BY_XPATH);
    }

    public WebUiControl getPassword() {
        return getUIControl(PASSWORD_FIELD, "Web: Password Field", ElementType.BY_XPATH);
    }

    public WebUiControl getLoginButton() {
        return getUIControl(LOGIN, "Web: Common Button", ElementType.BY_XPATH);
    }

    public WebUiControl getErrorMessage() {
        return getUIControl(ERROR_MESSAGE, "Web: Common Button", ElementType.BY_XPATH);
    }

    public WebUiControl getHomeMenu() {
        return getUIControl(HOME_MENU, "Web: Common Button", ElementType.BY_XPATH);
    }

    public WebUiControl getGLOTour() {
        return getUIControl(GLO_TOUR, "Web: Common Button", ElementType.BY_XPATH);
    }

    public boolean invalid_login(String username, String password) {
        try {
            getUserName().clearTextField();
            if (!getUserName().isFound(20))
                return false;
            Thread.sleep(2000);
            getUserName().sendText(username);
            Thread.sleep(2000);
            getPassword().sendText(password);
            Thread.sleep(2000);
            getLoginButton().click();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean VerifyErrorMessage() {
        return getErrorMessage().isFound(5);
    }

    public boolean VerifyHomeMenu() throws InterruptedException {
        Thread.sleep(2000);
        return getHomeMenu().isFound(20);
    }
    public WebUiControl getSkipButton() {
        return getUIControl(SKIP_BUTTON, "Web: Common Button", ElementType.BY_XPATH);
    }

    public boolean login(String username, String password) {

        try {
            if (!getUserName().isFound(20))
                return false;
            Thread.sleep(2000);
            getUserName().sendText(username);
            Thread.sleep(2000);
            getPassword().sendText(password);
            Thread.sleep(2000);
            getLoginButton().click();
            try {
                if (getGLOTour().isFound(10)) {
                    Thread.sleep(2000);
                    getSkipButton().click();
                }
            } catch (Exception e) {
                FrameworkLogger.logWarning("Tour Not Found");
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
