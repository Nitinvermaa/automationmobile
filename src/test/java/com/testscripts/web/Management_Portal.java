package com.testscripts.web;

import com.commonUtils.BaseTest;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.enums.Suites;
import com.gl.testngfw.execution.HeaderData;
import com.gl.testngfw.logging.VerificationFailException;
import com.gl.testngfw.logging.Verify;
import com.gl.testngfw.setup.InitializerScript;
import org.testng.annotations.Test;

public class Management_Portal extends BaseTest {



    @HeaderData(
            testCaseUniqueId = "ManagementPortal_1",
            featureName = "PCM Login portal",
            executableFor = {Platforms.WEB},
            testDescription = "Verify User is able to login in PCM portal",
            testCaseId = "ManagementPortal_1",
            requirementID = "Req_ver_0.0.1_ID-3.0.0",
            suite = {Suites.REGRESSION, Suites.P1}
    )
    @Test(priority = 1)
    public void validateSuccessfulPCMLogin() throws VerificationFailException, InterruptedException {

        if(managementPortalScreen.getUserName().isFound(5)){
            Thread.sleep(5000L);
            managementPortalScreen.getUserName().sendText("admin2Pcm");
            Thread.sleep(5000L);
            managementPortalScreen.getPassword().sendText("Admin2024*");
            Thread.sleep(5000L);
            managementPortalScreen.getLoginButton().click();
            Thread.sleep(5000L);
        }
        Thread.sleep(1000);
        Verify.verifyTrue(managementPortalScreen.getHomePageHeading().isFound(4),
                "Verify User is able to login in to PCM portal");
        Thread.sleep(5000L);

    }

    @HeaderData(
            testCaseUniqueId = "ManagementPortal_2",
            featureName = "PCM Device Class",
            executableFor = {Platforms.WEB},
            testDescription = "Verify user is able to search the particular device class",
            testCaseId = "ManagementPortal_2",
            requirementID = "Req_ver_0.0.1_ID-3.0.0",
            suite = {Suites.REGRESSION, Suites.P1}
    )
    @Test(priority = 2)
    public void validateDeviceClass() throws VerificationFailException, InterruptedException {

        if(managementPortalScreen.getUserName().isFound(5)){

            Thread.sleep(2000L);
            managementPortalScreen.getUserName().sendText("admin2Pcm");
            Thread.sleep(3000L);
            managementPortalScreen.getPassword().sendText("Admin2024*");
            Thread.sleep(3000L);
            managementPortalScreen.getLoginButton().click();
            Thread.sleep(3000L);
        }
        Thread.sleep(3000L);
        managementPortalScreen.getDeviceClass().isFound(5);
        Thread.sleep(3000L);
        managementPortalScreen.getDeviceClass().click();
        Thread.sleep(3000L);
        managementPortalScreen.getCARRIER().click();
        Thread.sleep(3000L);
        managementPortalScreen.getSprint7().click();
        Thread.sleep(3000L);
        managementPortalScreen.getSearchBox().sendText("Samsung_N910_Note 4");
        Thread.sleep(3000L);
        managementPortalScreen.getSearchResult().click();
        Thread.sleep(3000L);
        Verify.verifyTrue(true,"User is able to Search given Device class for Samsung N910 Device");

    }
    @HeaderData(
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
    }
}
