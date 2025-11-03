package com.testscripts.mobile;

import com.commonUtils.BaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gl.testngfw.api.DeviceHelper;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.enums.Suites;
import com.gl.testngfw.execution.HeaderData;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.logging.VerificationFailException;
import com.gl.testngfw.logging.Verify;
import io.restassured.RestAssured;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyMCMDeviceBranding extends BaseTest {
    static Logger LOGGER = Logger.getLogger(VerifyMCMDeviceBranding.class.getName());
    private static final String BASE_URL = "https://mobilesolutionsuzudev.t-mobile.com";
    String clientId= "211B41F6350F011079600EF95903091F1FCA856DE6917F493791DAAC4FB8B755";
    String secretKey= "aKuhnL9xAYcQgCYoh14o0l7hTF3DvbWyDT8rxZ67XIpwqYS94IV2Ybn9CdVqEZx";
    //String clientId= "AIzawx4mytTMVDJIY/4ZeW2usEnPlc3F9rrD/AtmJx/U6BUBzKP8ZPohyPzrceqs";
    //String secretKey= "6muGNRJre8Kd+KbzQgXMsfqAIzaYOn8WbAl4RMeUD03gqU0R16HaAUwHxQ0pYsUY";
    String token = "";


    @HeaderData(
            testCaseUniqueId = "MCM_1",
            featureName = "Device Branding",
            executableFor = {Platforms.ANDROID},
            testDescription = "Verify successfull MCM OOBE flow.",
            testCaseId = "MCM_1",
            requirementID = "Req_ver_0.0.1_ID-3.0.0",
            suite = {Suites.REGRESSION, Suites.P1}

    )
    @Test(priority = 1)
    public void verifyMCMOOBEFlow() throws VerificationFailException, InterruptedException, IOException, SQLException, ClassNotFoundException {
        String deviceUDID = getAndroidDriver().getSessionDetails().get("deviceUDID").toString();
        long currentTime = System.currentTimeMillis();

        System.out.println("Test case started");

        List installedPkgs = unInstalledApplication();

        //androidMCMAdminScreen.getMCMBtn().click();
        System.out.println("Branding BTN is present:::: "+androidMCMAdminScreen.getBrandingBtn().isFound(3));
        FrameworkLogger.logStep("Branding BTN is present:::: "+androidMCMAdminScreen.getBrandingBtn().isFound(3));
        androidMCMAdminScreen.getBrandingBtn().click();
        Thread.sleep(2000);


        //System.out.println("---------------------- Get PageSource ----------------------------\n");
        //System.out.println(getAndroidDriver().getPageSource());
        //System.out.println("\n---------------------- End of PageSource ----------------------------");

        Verify.verifyTrue(androidMCMAdminScreen.getTriggerBtn().isFound(2), "The MCM button clicked");
        Thread.sleep(1000);

        //androidMCMAdminScreen.getSettingBtn().click();
        //Thread.sleep(2000);
        /*if (androidMCMAdminScreen.getMoreOptionBtn().isFound(5)){

            androidMCMAdminScreen.getMoreOptionBtn().click();

            Thread.sleep(1000);

            androidMCMAdminScreen.getUninstallUpdatesBtn().click();

            androidMCMAdminScreen.getOKBtn().click();
        }*/

        //getAndroidDriver().navigate().back();
        //Thread.sleep(1000);
        androidMCMAdminScreen.getTriggerBtn().click();
        Thread.sleep(2000);

        getAndroidDriver().openNotifications();
        Thread.sleep(5000L);

        androidMCMAdminScreen.getNotificationCMLabel().isFound(5);

        ;
        while (!androidMCMAdminScreen.getApplicationInstalledLabel().isFound(3)){
            System.out.println("--------- Waiting for Application to install -----------");
            //Thread.sleep(5000L);
            if (androidMCMAdminScreen.getApplicationInstalledLabel().isFound(1)){
                Verify.verifyTrue(true, "Verify Application Installed notification is displayed.");
                androidMCMAdminScreen.getApplicationInstalledLabel().tap(204,740);
                //androidMCMAdminScreen.getApplicationInstalledLabel().click();
                break;
            }
        }

        //Thread.sleep(5000L);
        Verify.verifyTrue(androidMCMAdminScreen.getApplicationInstalledLabel().isFound(2),
                "Verify Application Installed notification is displayed.");
        androidMCMAdminScreen.getApplicationInstalledLabel().click();




        /*System.out.println("---------------------- Get PageSource ----------------------------\n");
        System.out.println(getAndroidDriver().getPageSource());
        System.out.println("\n---------------------- End of PageSource ----------------------------");*/

        Verify.verifyTrue(androidMCMAdminScreen.getInstalledLabelApplicationTitle().isFound(2),
                "Verify Installed Applications Title is displayed.");



        /*Verify.verifyTrue(androidMCMAdminScreen.getAmazonShoppingLabel().isFound(5),
                "Verify Amazon Shopping label is displayed.");*/



        Verify.verifyTrue(androidMCMAdminScreen.getTMobilePlayLabel().isFound(2),
                "Verify T-Mobile Play label is displayed.");

        //Verify.verifyTrue(androidMCMAdminScreen.getAppHubLabel().isFound(5),
         //       "Verify AppHub label is displayed.");

        //Verify.verifyTrue(androidMCMAdminScreen.getTLifeLabel().isFound(5),
          //      "Verify TLife label is displayed.");

        //System.out.println("---------------------- Get PageSource ----------------------------\n");
        //System.out.println(getAndroidDriver().getPageSource());
        //System.out.println("\n---------------------- End of PageSource ----------------------------");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(apiResponse());
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();

        // Convert object to pretty JSON string
        String prettyJson = writer.writeValueAsString(jsonNode);
        System.out.println(prettyJson);
        String deviceContextId = String.valueOf(jsonNode.get("Device").findValues("deviceContextId")).replace("[", "").replace("]", "");
        List<JsonNode> elements = jsonNode.findValues("packageName");
        Thread.sleep(5000);


        for (int j=0 ;j< elements.size();j++){
            for (int i = 0; i < installedPkgs.size(); i++){
                if(elements.get(j).toString().contains((installedPkgs.get(i).toString()))){

                    System.out.println("------------------ Verify Installed Packages ---------------------\n");
                    System.out.println("Package From Device:: " + installedPkgs.get(i).toString());
                    System.out.println("Package From response Json:: " + elements.get(j));
                    Verify.verifyTrue(true, "Verify package Installed:: " + installedPkgs.get(i));

                    System.out.println("---------------------------------------------------------------------\n");
                }

            }
        }





        /*for (int i = 0; i < installedPkgs.size(); i++) {
              for (int j=0 ;j< elements.size();j++){
            if (elements.get(j).toString().replace("\"", "").contains(installedPkgs.get(i).toString())) {
                System.out.println("Package From response Json:: " + elements.get(i));
                while (!DeviceHelper.isAppInstalled(deviceUDID, elements.get(i).toString().replace("\"", ""))) {
                    System.out.println("Waiting for app to be installed");
                    // getAndroidDriver().manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
                    Thread.sleep(2000L);
                }

                if (DeviceHelper.isAppInstalled(deviceUDID, elements.get(i).toString().replace("\"", ""))) {
                    System.out.println("------------------------- Installed Packages ----------------------\n "+DeviceHelper.isAppInstalled(deviceUDID, elements.get(i).toString()));
                    System.out.println("Verify package Installed:: " + installedPkgs.get(i));
                    Verify.verifyTrue(true, "Verify package Installed:: " + installedPkgs.get(i));

                }
            }
           }
        }*/


       // FrameworkLogger.logStep("*********************************** DB Validation ********************************************");
        //for (Object installedPkg : elements) {
        //   String packName =  installedPkg.toString().replace("\"","");
        //    String rptQuery = "select /*+ parallel(ds,6) */ * from device_appl_stus ds  where ds.DEVICE_CONTEXT_FK in ('" + deviceContextId + "') AND ds.PACKAGE_NME='" + packName + "' AND trunc(ds.MAINT_LAST_TS) = trunc(sysdate) AND ds.device_log_txt LIKE 'Install Successful'";
        //    ResultSet resultSet = sqlDBConnection().executeQuery(rptQuery);

        //    while (resultSet.next()) {
        //        System.out.println("Row Value :: "+resultSet.getRow());
        //        if (resultSet.getRow()==1&&resultSet.getString(16).contains("Install Successful") && resultSet.getString(15).contains(packName)) {
        //            System.out.println("Package "+packName+" Installed: " + resultSet.getString(16));
        //            Verify.verifyTrue(true, "DB Data --> Package "+packName+" :: " + resultSet.getString(16));
        //        }
        //    }
        //}

        DeviceHelper.home(deviceUDID);
    }

    public String apiResponse() {
        System.out.println("-------------------------- Sending API Request -------------------\n");
        String deviceUDID = getAndroidDriver().getSessionDetails().get("deviceUDID").toString();

        System.out.println("deviceUDID:: "+deviceUDID);

        String APIEndpoint = "https://mobilesolutionsuzudev.t-mobile.com/MCM/oem/v1/device/token";
        System.out.println("APIEndpoint   :"+APIEndpoint);
        //System.setProperty("jsse.enableSNIExtension", "false");
        Map<String, String> header = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        String response;

        header.put("Content-Type", "application/json");
        //header.put("charset", "utf-8");

        body.put("clientId", clientId);
        body.put("secretKey", secretKey);
        body.put("meid", "12312131313317");


        response = RestAssured.given()
                //.relaxedHTTPSValidation("ssl")
                // .contentType(ContentType.JSON)
                .headers(header)
                .body(body)
                .when()
                .post(APIEndpoint).asString();

        System.out.println("--------------------- Dev API Response ---------------------------------\n");
        System.out.println("API Call: "+APIEndpoint +" \n");
        System.out.println("Response is: " + response);

        token =response.split(":")[1].replace("\"","").replace("\"","").replace("}","");
        System.out.println("Token is::: "+token);



        header.put("Authorization", "Bearer " + token);
        header.put("Content-Type", "application/json");
        header.put("charset", "utf-8");
        if (deviceUDID.equals("R9TTA08VZZK")) {
            System.out.println("UDID OF Samsung Device:: " + deviceUDID);

            body.put("applicationType", "CM");
            body.put("carrier", "TMOBILE");
            body.put("clientVersion", "2130600035");
            body.put("deviceColor", "NA");
            body.put("deviceId", "350545460017627");
            body.put("firmwareVersion", "AutomationRequest");
            body.put("localeId", "en_US");
            body.put("mdn", "1234567890");
            body.put("operatingSystem", "ANDROID");
            body.put("osVersion", "31");
            body.put("deviceMake", "samsung");
            body.put("deviceModel", "a14xm");
            body.put("transitionFlow", "false");
        } else if (deviceUDID.equals("LMQ730WCAAHA5XWKGA")) {
            System.out.println("UDID OF LG Device:: " + deviceUDID);
            body.put("applicationType", "MCM");
            body.put("carrier", "SPRINT");
            body.put("clientVersion", "2130600032");
            body.put("deviceColor", "NA");
            body.put("deviceId", "354527110006156");
            body.put("firmwareVersion", "QKQ1.200216.002");
            body.put("localeId", "en_US");
            body.put("mdn", "9136388201");
            body.put("operatingSystem", "ANDROID");
            body.put("osVersion", "30");
            body.put("deviceMake", "LGE");
            body.put("deviceModel", "mdh50lm");
            body.put("transitionFlow", "false");
        }

         APIEndpoint = "https://mobilesolutionsuzudev.t-mobile.com/MCM/oem/v1/device/register";

        System.out.println("Register APIEndpoint:: "+APIEndpoint);

         response = RestAssured.given()
                .relaxedHTTPSValidation("ssl")
                // .contentType(ContentType.JSON)
                .headers(header)
                .body(body)
                .when()
                .post(APIEndpoint).asString();
        //System.out.println("Response is: " + response);
        //MCM/oem/v1/device/register

        return response;

    }

    public List unInstalledApplication() {

        System.out.println("--------- Uninstalling Packages --------");
        String deviceUDID = getAndroidDriver().getSessionDetails().get("deviceUDID").toString();

        DeviceHelper.clearApplicationCache(deviceUDID, "com.tmobile.dm.cm");

        List<String> packageNames = new ArrayList<>();
        packageNames.add("com.tmobile.m1");
        packageNames.add("com.amazon.mShop.android.shopping");
        packageNames.add("com.tmobile.tuesdays");
        /*packageNames.add("com.deaddropgames.stuntmountain.android");
        packageNames.add("com.le.carracing");
        packageNames.add("com.BuddyMattEnt.ChainReaction");
        packageNames.add("com.andregal.android.billard");
        packageNames.add("com.androbaby.game2048");
        packageNames.add("com.shootbubble.bubbledexlue");
        packageNames.add("com.jetstartgames.chess");
        packageNames.add("com.fevdev.nakedbrowser");
        packageNames.add("com.buak.Link2SD");
        packageNames.add("com.socialnmobile.dictapps.notepad.color.note");*/



        for (String packageName : packageNames) {
            boolean isInstalled = DeviceHelper.isAppInstalled(deviceUDID, packageName);

            if (isInstalled) {
                System.out.println("Running pre-condition:: UnInstalling the package " + isInstalled+" /n");
                DeviceHelper.uninstallApplication(deviceUDID, packageName);
                System.out.println("Is Package available after uninstalling:: " + DeviceHelper.isAppInstalled(deviceUDID, packageName)+"/n");
            }
        }
        return packageNames;
    }
}