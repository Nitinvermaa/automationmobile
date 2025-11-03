package com.screens.web.mobile.android;

import com.gl.testngfw.api.MobileUiControl;
import com.gl.testngfw.api.ScreenPattern;
import com.gl.testngfw.enums.ElementType;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class NetflixAndroidTestScreen extends ScreenPattern {

    @AndroidFindBy(id = "com.netflix.mediaclient:id/menu_sign_in")
    AndroidElement signIn;

    @AndroidFindBy(id = "com.netflix.mediaclient:id/login_email")
    AndroidElement username;

    @AndroidFindBy(id = "com.netflix.mediaclient:id/login_password")
    AndroidElement password;

    @AndroidFindBy(id = "com.netflix.mediaclient:id/login_action_btn")
    AndroidElement signInBtn;

    @AndroidFindBy(id = "com.netflix.mediaclient:id/profile_avatar_title")
    AndroidElement mediaClient;

    @AndroidFindBy(id = "com.netflix.mediaclient:id/ab_menu_search_item")
    AndroidElement searchIcon;

    @AndroidFindBy(id = "android:id/search_src_text")
    AndroidElement searchByText;

    @AndroidFindBy(id = "com.netflix.mediaclient:id/movie_boxart")
    AndroidElement searchResult;

    @AndroidFindBy(id = "com.netflix.mediaclient:id/menu_navigation_button_view")
    AndroidElement navigateUp;

    @AndroidFindBy(id = "com.netflix.mediaclient:id/play_button")
    AndroidElement assetDetailsPlay;

    @AndroidFindBy(id = "com.netflix.mediaclient:id/close_button")
    AndroidElement assetDetailsCloseBtn;

    @AndroidFindBy(id = "com.netflix.mediaclient:id/player_pause_btn")
    AndroidElement pausePlayer;

    @AndroidFindBy(id = "com.netflix.mediaclient:id/player_back_button")
    AndroidElement closePlayback;


    public MobileUiControl signIn() {
        return getUIControl("com.netflix.mediaclient:id/menu_sign_in", "Web: No Search Result Found", ElementType.BY_ID);
    }

    public MobileUiControl username() {
        return getUIControl("com.netflix.mediaclient:id/login_email", "Web: No Search Result Found", ElementType.BY_ID);
    }

    public MobileUiControl password() {
        return getUIControl("com.netflix.mediaclient:id/login_password", "Web: No Search Result Found", ElementType.BY_ID);
    }

    public MobileUiControl signInBtn() {
        return getUIControl("com.netflix.mediaclient:id/login_action_btn", "Web: No Search Result Found", ElementType.BY_ID);
    }
    public MobileUiControl mediaClient() {
        return getUIControl("com.netflix.mediaclient:id/profile_avatar_title", "Web: No Search Result Found", ElementType.BY_ID);
    }
    public MobileUiControl searchIcon() {
        return getUIControl("com.netflix.mediaclient:id/ab_menu_search_item", "Web: No Search Result Found", ElementType.BY_ID);
    }
    public MobileUiControl searchByText() {
        return getUIControl("android:id/search_src_text", "Web: No Search Result Found", ElementType.BY_ID);
    }

    public MobileUiControl searchresult() {
        return getUIControl("com.netflix.mediaclient:id/movie_boxart", "Web: No Search Result Found", ElementType.BY_ID);
    }

    public MobileUiControl nevigateUp() {
        return getUIControl("com.netflix.mediaclient:id/menu_navigation_button_view", "Web: No Search Result Found", ElementType.BY_ID);
    }


    public MobileUiControl assertDetailPlayBtn() {
        return getUIControl("com.netflix.mediaclient:id/play_button", "Mobile: Assert detail play button", ElementType.BY_ID);
    }

    public MobileUiControl playBtn() {
        return getUIControl("com.netflix.mediaclient:id/player_pause_btn", "Mobile: Play Button", ElementType.BY_ID);
    }

    public MobileUiControl pauseBtn() {
        return getUIControl("com.netflix.mediaclient:id/player_pause_btn", "Mobile: Pause Button", ElementType.BY_ID);
    }

    public MobileUiControl backSeekBtn() {
        return getUIControl("com.netflix.mediaclient:id/skip_back_seek_button", "Mobile: back Button", ElementType.BY_ID);
    }

    public MobileUiControl forwardSeekBtn() {
        return getUIControl("com.netflix.mediaclient:id/skip_forward_seek_button", "Mobile: Forward Button", ElementType.BY_ID);
    }

    public MobileUiControl playerScreen() {
        return getUIControl("//*[@class='android.view.View']", "Mobile: Player Screen", ElementType.BY_XPATH);
    }
}
