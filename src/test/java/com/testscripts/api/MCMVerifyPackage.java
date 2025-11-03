package com.testscripts.api;

import com.commonUtils.BaseTest;
import com.commonUtils.Employee;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gl.testngfw.api.DeviceHelper;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.enums.Suites;
import com.gl.testngfw.execution.HeaderData;
import com.gl.testngfw.logging.VerificationFailException;
import com.gl.testngfw.logging.Verify;
import com.google.gson.Gson;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class MCMVerifyPackage extends BaseTest {
    static Logger LOGGER = Logger.getLogger(MCMVerifyPackage.class.getName());
    private static final String BASE_URL = "https://mobilesolutionsuzudev.t-mobile.com";
    String clientId= "211B41F6350F011079600EF95903091F1FCA856DE6917F493791DAAC4FB8B755";
    String secretKey= "aKuhnL9xAYcQgCYoh14o0l7hTF3DvbWyDT8rxZ67XIpwqYS94IV2Ybn9CdVqEZx";
    //String clientId= "AIzawx4mytTMVDJIY/4ZeW2usEnPlc3F9rrD/AtmJx/U6BUBzKP8ZPohyPzrceqs";
    //String secretKey= "6muGNRJre8Kd+KbzQgXMsfqAIzaYOn8WbAl4RMeUD03gqU0R16HaAUwHxQ0pYsUY";
    String token = "";


    @HeaderData(
            testCaseUniqueId = "MCM_API_01",
            featureName = "MCM server Authorization",
            executableFor = {Platforms.API},
            testDescription = "Verification of successful Authentication of MCM Server",
            testCaseId = "MCM_API_01",
            requirementID = "Req_ver_0.0.1_ID-3.0.0",
            suite = {Suites.REGRESSION, Suites.P1}
    )
    @Test(priority = 1)
    public void validateMCMServerAuthorization() throws VerificationFailException, SQLException, ClassNotFoundException, IOException {
        //System.setProperty("jsse.enableSNIExtension", "false");
        String responseBody;

        Map<String, String> header = new HashMap<>();

        header.put("Content-Type", "application/json");
        //header.put("charset", "utf-8");


        Map<String, String> body = new HashMap<>();

        body.put("clientId", clientId);
        body.put("secretKey", secretKey);
        body.put("meid", "12312131313317");

        LOGGER.info("Triggering API");
        //Passing complete URL
        ValidatableResponse response = getApiDriver().postRequest("https://mobilesolutionsuzudev.t-mobile.com/MCM/oem/v1/device/token", header, body);

        responseBody = response.extract().asString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();

        // Convert object to pretty JSON string
        String prettyJson = writer.writeValueAsString(jsonNode);
        System.out.println("----------------- Response is : ---------------------");
        System.out.println(prettyJson);

        //System.out.println("Response is : "+responseBody);
        token =responseBody.split(":")[1].replace("\"","").replace("\"","").replace("}","");
        System.out.println("Token is::: "+token);

        //The status code is being extracted from response
        int status = response.extract().statusCode();
        LOGGER.info("The status is---------: " + status);
        Verify.verifyTrue(status==200, "The API is passed");


    }



    @HeaderData(
            testCaseUniqueId = "MCM_API_02",
            featureName = "MCM server registration",
            executableFor = {Platforms.API},
            testDescription = "Verification of PCM Pack Success response for Samsung SM-A146U Device",
            testCaseId = "MCM_API_02",
            requirementID = "Req_ver_0.0.1_ID-3.0.0",
            suite = {Suites.REGRESSION, Suites.P1}
    )
    @Test(priority = 2)
    public void verifyMCMRegistrationStatusSuccess() throws VerificationFailException, SQLException, ClassNotFoundException, IOException {
        //System.setProperty("jsse.enableSNIExtension", "false");

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + token);
        header.put("Content-Type", "application/json");
        header.put("charset", "utf-8");


        Map<String, String> body = new HashMap<>();

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

        LOGGER.info("Triggering API");
        //Passing complete URL
        ValidatableResponse response = getApiDriver().postRequest("https://mobilesolutionsuzudev.t-mobile.com/MCM/oem/v1/device/register",
                header, body);
        //The status code is being extracted from response
        int status = response.extract().statusCode();
        LOGGER.info("The status is---------: " + status);
        Verify.verifyEquals(status, 200, "The API is passed");
        String response1 = response.extract().asString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response1);
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();

        // Convert object to pretty JSON string
        String prettyJson = writer.writeValueAsString(jsonNode);
        System.out.println("----------------- Response is : ---------------------");
        System.out.println(prettyJson);



        //System.out.println(jsonNode);
        String deviceContextId = String.valueOf(jsonNode.get("Device").findValues("deviceContextId")).replace("[", "").replace("]", "");

        System.out.println("Context ID: "+deviceContextId);
        List <JsonNode> appList_Server = jsonNode.findValues("packageName");
        List<String> apps = appList();
        Collections.sort(apps);

        for (JsonNode pack: appList_Server){

            for (String app : apps) {
                System.out.println("Package from CM Pack API response:: " + app + " Package from Pre-defined list:: " + pack);
                boolean isPackAvailable = pack.toString().replace("\"", "").equals(app);
                if (isPackAvailable){
                    Verify.verifyTrue(true, " Package from CM Pack API response:: " + pack + " Package from Pre-defined list:: " + app);
                }
            }
        }

        System.out.println(response);
        LOGGER.info("Server Response: " + response1);

    }




    public List<String> appList(){


        List <String> packageNames = new ArrayList<>();
        packageNames.add("com.tmobile.m1");
        packageNames.add("com.amazon.mShop.android.shopping");
        packageNames.add("com.tmobile.tuesdays");
       /* packageNames.add("com.deaddropgames.stuntmountain.android");
        packageNames.add("com.le.carracing");
        packageNames.add("com.BuddyMattEnt.ChainReaction");
        packageNames.add("com.andregal.android.billard");
        packageNames.add("com.androbaby.game2048");
        packageNames.add("com.shootbubble.bubbledexlue");
        packageNames.add("com.jetstartgames.chess");
        packageNames.add("com.fevdev.nakedbrowser");
        packageNames.add("com.buak.Link2SD");
        packageNames.add("com.socialnmobile.dictapps.notepad.color.note");
        packageNames.add("com.sprint.w.installer");*/


        return packageNames;
    }
}
