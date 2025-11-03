package com.screens.web.mobile.android;

import com.gl.testngfw.api.MobileUiControl;
import com.gl.testngfw.api.ScreenPattern;
import com.gl.testngfw.enums.ElementType;
import com.gl.testngfw.logging.FrameworkLogger;

public class AndroidMCMAdminScreen extends ScreenPattern {

    private static final String MCM_BTN = "//android.widget.TextView[@text='MCM']";
    private static final String SETTING_BTN = "com.tmobile.dm.cmas:id/btn_settings";

    private static final String BRANDING_BTN = "//android.widget.TextView[@text='Branding']";
    private static final String NOTIFICATION_LABEL = "//android.widget.TextView[@text='Content Manager']";
    private static final String APPLICATION_INSTALLED_LABEL = "//android.widget.TextView[@text='Applications Installed']";

    private static final String INSTALLED_APPLICATION_TITLE = "//android.widget.TextView[@text='Installed Applications']";


    private static final String AMAZON_SHOPPING_LABEL = "//android.widget.TextView[@text='Amazon Shopping']";


    private static final String T_MOBILE_PLAY_LABEL = "//android.widget.TextView[@text='T-Mobile Play']";
    private static final String APPHUB_LABEL = "//android.widget.TextView[@text='AppHub']";
    private static final String T_LIFE_LABEL = "//android.widget.TextView[@text='T-Life']";

    private static final String TRIGGER_BTN = "com.tmobile.dm.cmas:id/btn_dwnld";
    private static final String MCMCCONFIG_BTN = "com.sprint.ce.test:id/btn_mcm_config";
    private static final String MCM_ADDRESS_BTN = "//*[@text='Address']";
    private static final String MCM_ENV_RTB1_BTN = "//*[@text='RTB1']";


    private static final String MORE_OPTION_BTN = "//android.widget.ImageView[@content-desc='More options']";

    private static final String UNINSTALL_UPDATES_BTN = "com.android.settings:id/title";

    private static final String OK_BTN = "//android.widget.Button[@text='OK']";








    public MobileUiControl getSettingBtn() {
        return getUIControl(SETTING_BTN, "Mob: Setting BTN ", ElementType.BY_ID);
    }

    public MobileUiControl getUninstallUpdatesBtn() {
        return getUIControl(UNINSTALL_UPDATES_BTN, "Mob: Setting BTN ", ElementType.BY_ID);
    }

    public MobileUiControl getMoreOptionBtn() {
        return getUIControl(MORE_OPTION_BTN, "Mob: More Option BTN", ElementType.BY_XPATH);
    }

    public MobileUiControl getOKBtn() {
        return getUIControl(OK_BTN, "Mob: OK BTN", ElementType.BY_XPATH);
    }


    public MobileUiControl getMCMBtn() {
        return getUIControl(MCM_BTN, "Mob: MCM BTN Field", ElementType.BY_XPATH);
    }

    public MobileUiControl getBrandingBtn() {
        return getUIControl(BRANDING_BTN, "Mob: Branding BTN Field", ElementType.BY_XPATH);
    }

    public MobileUiControl getNotificationCMLabel() {
        return getUIControl(NOTIFICATION_LABEL, "Mob: Notification CM Label", ElementType.BY_XPATH);
    }

    public MobileUiControl getApplicationInstalledLabel() {
        return getUIControl(APPLICATION_INSTALLED_LABEL, "Mob: Notification CM Label", ElementType.BY_XPATH);
    }

    public MobileUiControl getInstalledLabelApplicationTitle() {
        return getUIControl(INSTALLED_APPLICATION_TITLE, "Mob: Notification CM Label", ElementType.BY_XPATH);
    }

    public MobileUiControl getTMobilePlayLabel() {
        return getUIControl(T_MOBILE_PLAY_LABEL, "Mob: Notification CM Label", ElementType.BY_XPATH);
    }
    public MobileUiControl getTLifeLabel() {
        return getUIControl(T_LIFE_LABEL, "Mob: Notification CM Label", ElementType.BY_XPATH);
    }


    public MobileUiControl getAmazonShoppingLabel() {
        return getUIControl(AMAZON_SHOPPING_LABEL, "Mob: Notification CM Label", ElementType.BY_XPATH);
    }


    public MobileUiControl getAppHubLabel() {
        return getUIControl(APPHUB_LABEL, "Mob: Notification CM Label", ElementType.BY_XPATH);
    }




    public MobileUiControl getTriggerBtn() {
        return getUIControl(TRIGGER_BTN, "Mob: Trigger BTN Field", ElementType.BY_ID);
    }

    public MobileUiControl getMCMConfigBTN() {
        return getUIControl(MCMCCONFIG_BTN, "Mob: Trigger BTN Field", ElementType.BY_ID);
    }

    public MobileUiControl getMCMAddressBTN() {
        return getUIControl(MCM_ADDRESS_BTN, "Mob: Trigger BTN Field", ElementType.BY_XPATH);
    }

    public MobileUiControl getMCM_RTB1BTN() {
        return getUIControl(MCM_ENV_RTB1_BTN, "Mob: Trigger BTN Field", ElementType.BY_XPATH);
    }


}
