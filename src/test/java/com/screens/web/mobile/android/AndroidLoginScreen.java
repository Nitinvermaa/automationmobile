package com.screens.web.mobile.android;

import com.gl.testngfw.api.MobileUiControl;
import com.gl.testngfw.api.ScreenPattern;
import com.gl.testngfw.enums.ElementType;
import com.gl.testngfw.logging.FrameworkLogger;

public class AndroidLoginScreen extends ScreenPattern {
    private static final String USERNAME = "app.globallogic.com.glo:id/et_username";
    private static final String PASSWORD_FIELD = "app.globallogic.com.glo:id/et_password";
    private static final String LOGIN = "app.globallogic.com.glo:id/login_button";
    private static final String ERROR_MESSAGE = "app.globallogic.com.glo:id/error_msg";
    private static final String LOADER_IMAGE = "app.globallogic.com.glo:id/loaderImage";
    private static final String HOME_MENU = "//*[@text='Home']";
    private static final String MENUDRAWER = "//android.widget.ImageButton[@content-desc='Open navigation drawer']";
    private static final String LOGOUT = "app.globallogic.com.glo:id/logout_text";
    private static final String LOGOUT_YES = "android:id/button1";

    public MobileUiControl getUserName() {
        return getUIControl(USERNAME, "Mob: User Name Field", ElementType.BY_ID);
    }

    public MobileUiControl getPassword() {
        return getUIControl(PASSWORD_FIELD, "Mob: Password Field", ElementType.BY_ID);
    }

    public MobileUiControl getLoginButton() {
        return getUIControl(LOGIN, "Mob: Login Button", ElementType.BY_ID);
    }

    public MobileUiControl getErrorMessage() {
        return getUIControl(ERROR_MESSAGE, "Web: Error Message text", ElementType.BY_ID);
    }

    public MobileUiControl getLoaderImage() {
        return getUIControl(LOADER_IMAGE, "Web: Error Message text", ElementType.BY_ID);
    }

    public MobileUiControl getHomeMenu() {
        return getUIControl(HOME_MENU, "Web: Home Menu", ElementType.BY_XPATH);
    }

    public MobileUiControl getMenu() {
        return getUIControl(MENUDRAWER,
                "Web: Menu Drawer of mobile", ElementType.BY_XPATH);
    }

    public MobileUiControl getLogout() {
        return getUIControl(LOGOUT, "Mob: Logout", ElementType.BY_ID);
    }

    public MobileUiControl getLogoutYes() {
        return getUIControl(LOGOUT_YES, "Mob: Logout Yes option", ElementType.BY_ID);
    }

    public boolean login(String username, String password) {
        try {
            if (!getUserName().isFound(10)) {
                if (!getMenu().isFound(10))
                    return false;
                getMenu().click();
                if (!getLogout().isFound(10))
                    return false;
                getLogout().click();
                if (!getLogoutYes().isFound(10))
                    return false;
                getLogoutYes().click();
            }
            getUserName().sendText(username);
            Thread.sleep(2000);
            getPassword().sendText(password);
            Thread.sleep(2000);
            getLoginButton().click();
            Thread.sleep(2000);
            getMenu().click();
            Thread.sleep(2000);
            getLogout().click();
            return true;
        } catch (Exception e) {
            FrameworkLogger.logWarning("Home menu is not found");
            return false;
        }
    }

    public boolean invalid_login(String username, String password) {
        try {
            if (!getUserName().isFound(10)) {
                getMenu().isFound(10);
                Thread.sleep(2000);
                getMenu().click();
                if (!getLogout().isFound(10))
                    return false;
                getLogout().click();
                if (!getLogoutYes().isFound(10))
                    return false;
                getLogoutYes().click();
            }
            getUserName().isFound(15);
            FrameworkLogger.logWarning("User not logged in");
            Thread.sleep(2000);
            getUserName().sendText(username);
            Thread.sleep(2000);
            getPassword().sendText(password);
            Thread.sleep(2000);
            getLoginButton().click();
            Thread.sleep(2000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean VerifyErrorMessage() {
        return getErrorMessage().isFound(10);
    }

    public boolean VerifyHomeMenu() {
        return getHomeMenu().isFound(10);

    }
}
