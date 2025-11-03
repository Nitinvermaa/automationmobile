package com.screens.web;

import com.gl.testngfw.api.ScreenPattern;
import com.gl.testngfw.api.WebUiControl;
import com.gl.testngfw.enums.ElementType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;


public class NetflixTestScreen extends ScreenPattern {

    @FindBy(xpath = "//button[@class='searchTab']")
    WebElement searchTab;

    @FindBy(id = "searchInput")
    WebElement searchBox;

    @FindBy(xpath="//a[@class='authLinks redButton']")
    WebElement gotoSignForm;

    @FindBy(id="id_userLoginId")
    WebElement userId;

    @FindBy(id="id_password")
    WebElement password;

    @FindBy(xpath="//button[@data-uia='login-submit-button']")
    WebElement SignInBtn;

    @FindBy(xpath = "//a[@aria-label = 'Kabir Singh']")
    WebElement assetKabirSingh;

    @FindBy(xpath = "//button[@aria-label = 'Play']")
    WebElement play;

    @FindBy(xpath = "//button[@aria-label = 'Pause']")
    WebElement pause;

    @FindBy(xpath = "//a[@aria-label = 'Squid Game']")
    WebElement assetSquidGame;

    @FindBy(xpath = "//a[@aria-label = 'Transformers']")
    WebElement assetTransformers;

    @FindBy(xpath = "//a[@aria-label = 'Venom']")
    WebElement assetVenom;

    @FindBy(xpath = "//button[@aria-label = 'expand to detail modal']")
    WebElement moreInfo;

    @FindBy(xpath = "//a[@aria-label = 'Play']")
    WebElement detailsViewPlaybutton;

    @FindBy(xpath = "//div[@class='rowContainer rowContainer_title_card' and @id='row-1']")
    List<WebElement> LineUp1;

    @FindBy(xpath = "//a[@aria-label='Netflix']")
    WebElement gotoHome;

    @FindBy(xpath = "//div[@class='previewModal=close']")
    //@FindBy(xpath = "//div[@data-uia='previewModal--backDrop']")
            WebElement closeModalView;



    public WebUiControl gotoSignForm() {
        return getUIControl("//a[@class='authLinks redButton']", "Web: No Search Result Found", ElementType.BY_XPATH);
    }

    public WebUiControl userId() {
        return getUIControl("id_userLoginId", "Web: No Search Result Found", ElementType.BY_ID);
    }

    public WebUiControl password() {
        return getUIControl("id_password", "Web: No Search Result Found", ElementType.BY_ID);
    }

    public WebUiControl SignInBtn() {
        return getUIControl("//button[@data-uia='login-submit-button']", "Web: No Search Result Found", ElementType.BY_XPATH);
    }

    public WebUiControl gotoHome() {
        return getUIControl("//a[@aria-label='Netflix']", "Web: No Search Result Found", ElementType.BY_XPATH);
    }
    public WebUiControl searchTab() {
        return getUIControl("//button[@class='searchTab']", "Web: No Search Result Found", ElementType.BY_XPATH);
    }
    public WebUiControl searchBox() {
        return getUIControl("searchInput", "Web: No Search Result Found", ElementType.BY_ID);
    }

    public WebUiControl selectProfile() {
        return getUIControl("//li[@class='profile'][1]", "Web: No Search Result Found", ElementType.BY_XPATH);
    }

    public WebUiControl movieAsset() {
        return getUIControl("//*[@id='title-card-0-0']", "Web: No Search Result Found", ElementType.BY_XPATH);
    }

    public WebUiControl movieAssetPlayBtn() {
        return getUIControl("//*[@type='button']", "Web: No Search Result Found", ElementType.BY_XPATH);
    }

    public WebUiControl moviePlayer() {
        return getUIControl("//*[@data-uia='video-canvas']", "Web: No Search Result Found", ElementType.BY_XPATH);
    }

    public WebUiControl playerPauseBtn() {
        return getUIControl("//*[@arial-label='Pause']", "Web: No Search Result Found", ElementType.BY_XPATH);
    }

    public WebUiControl playerBackBtn() {
        return getUIControl("//*[@arial-label='Seek Back']", "Web: No Search Result Found", ElementType.BY_XPATH);
    }

    public WebUiControl playerForwardBtn() {
        return getUIControl("//*[@arial-label='Seek Forward']", "Web: No Search Result Found", ElementType.BY_XPATH);
    }





}
