package com.testscripts.web;

import com.commonUtils.BaseTest;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.enums.Suites;
import com.gl.testngfw.execution.HeaderData;
import com.gl.testngfw.logging.VerificationFailException;
import com.gl.testngfw.logging.Verify;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class TMobile_Portal extends BaseTest {



    @HeaderData(
            testCaseUniqueId = "TMobile_Portal_1",
            featureName = "TMobile Login portal",
            executableFor = {Platforms.WEB},
            testDescription = "Verify User is able to login in TMobile portal",
            testCaseId = "TMobile_Portal_1",
            requirementID = "Req_ver_0.0.1_ID-3.0.0",
            suite = {Suites.REGRESSION, Suites.P1}
    )
    @Test(priority = 1)
    public void validateSuccessfulTMobileLogin() throws VerificationFailException, InterruptedException {

        /*if(managementPortalScreen.getMyAccountBtn().isFound(5)){
            if (managementPortalScreen.getRejectBtn().isFound(5)){
                managementPortalScreen.getRejectBtn().click();
            }
            managementPortalScreen.getMyAccountBtn().click();
            managementPortalScreen.getLoginBtn().click();
            managementPortalScreen.getUserNameTextBox().sendText("hcbsdhcsdh");
            managementPortalScreen.getNextBtn().click();
            managementPortalScreen.getPasswordTextBox().sendText("bcnscvbnscvnx");
            managementPortalScreen.getLoginBtn2().click();

        }*/


        Thread.sleep(5000L);
        Verify.verifyTrue(managementPortalScreen.getMyAccountBtn().isFound(4),
                "Verify MyAccount Btn is displayed");

        getWebDriver().navigate().to(navigateToSite());
        Thread.sleep(5000L);
        managementPortalScreen.getUserNameTextBox().sendText("9133260234");
        Thread.sleep(5000L);
        managementPortalScreen.getNextBtn().click();
        Thread.sleep(5000L);
        managementPortalScreen.getPasswordTextBox().sendText("May@tmobile05");
        Thread.sleep(5000L);
        managementPortalScreen.getLoginBtn2().click();
        Thread.sleep(5000L);


        //*[text()='Text a code to ']


        //*[@type='submit'] btn

        Verify.verifyTrue(managementPortalScreen.getSendCodeBtn().isFound(4),
                "Verify User is able to login in to T-Mobile portal");
    }


    /*@HeaderData(
            testCaseUniqueId = "ManagementPortal_3",
            featureName = "PCM Login portal",
            executableFor = {Platforms.WEB},
            testDescription = "Verify User should able to login in PCM portal",
            testCaseId = "ManagementPortal_3",
            requirementID = "Req_ver_0.0.1_ID-3.0.0",
            suite = {Suites.REGRESSION, Suites.P1}
    )

    @Test(priority = 3)
    public void validatePCMLogin() throws VerificationFailException, InterruptedException {

        if(managementPortalScreen.getUserName().isFound(5)){
            Thread.sleep(3000L);
            managementPortalScreen.getUserName().sendText("admin2Pcmm");
            Thread.sleep(3000L);
            managementPortalScreen.getPassword().sendText("Admin2024*");
            Thread.sleep(3000L);
            managementPortalScreen.getLoginButton().click();
            Thread.sleep(3000L);
        }

        Verify.verifyFalse(true, "Verify User is able to login in to PCM portal");
        Thread.sleep(3000L);
    }*/

    private String navigateToSite(){

        String url = "https://account.t-mobile.com/signin/v2/?redirect_uri=https:%2F%2Fwww.t-mobile.com%2Fsignin&scope=TMO_ID_profile%2520associated_lines%2520ban_roles%2520billing_information%2520associated_billing_accounts%2520extended_lines%2520token%2520openid%2520vault%2520role%2520consent&client_id=MYTMO&access_type=ONLINE&response_type=code&approval_prompt=auto&prompt=select_account&state=eyJpbnRlbnQiOiJMb2dpbiIsImJvb2ttYXJrVXJsIjoiaHR0cHM6Ly93d3cudC1tb2JpbGUuY29tL2FjY291bnQvZGFzaGJvYXJkIn0";

        return url;
    }
}
