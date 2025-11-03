package com.gl.testngfw.common;

import com.gl.testngfw.api.RestAPIExtension;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.report.model.*;
import com.gl.testngfw.utility.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBUpdater {
    private static final Logger LOGGER = Logger.getLogger(DBUpdater.class.getName());
    private static RestAPIExtension restAPIHelper = new RestAPIExtension(false);
    private static ConfigData configData = TestExecutor.getConfigData();
    private static final String BASE_URL = configData.getFrameworkDBUrl();
    private static String authToken = "";
    private static String baseDir = System.getProperty("user.dir") + "/Project_logo/";

    public DBUpdater() {
        if (configData.isUseDB() && authToken.isEmpty()) {
            authToken = login();
            authToken = authToken.substring(authToken.indexOf(" ")).trim();
            Constants.setAuthToken(authToken);
        }
    }

    public static int updateTestSuite() throws IOException {
        Gson gson = new Gson();
        String body = gson.toJson(TestSuiteUpdater.updateTestRecords());
        Map<String, String> header = getHeaderData();
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "addTestCases", header, body);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
        return restAPIHelper.getResponseStatusCode(response);
    }

    private static Map<String, String> getHeaderData() {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("charset", "utf-8");
        header.put("Authorization", authToken);
        return header;
    }


    public String login() {
        Map<String, String> body = new HashMap<>();
        body.put("loginName", configData.getGafUserName());
        body.put("password", configData.getGafPassword());
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("charset", "utf-8");
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "login", header, body);
        LoginResponse loginResponse = response.extract().body().as(LoginResponse.class);
        return loginResponse.getJwtToken();
    }

    public int getTestSuite() {
        Map<String, String> header = getHeaderData();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("projectUniqueKey", configData.getProjectID());
        parameters.put("testSuiteName", "regression");
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL + "testCases", header, parameters);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
        return restAPIHelper.getResponseStatusCode(response);
    }

    public String addProject() {
        String sprintLogo = Util.getEncoded(baseDir + "Sprint.PNG");
        String rogersLogo = Util.getEncoded(baseDir + "rogers.PNG");
        String verizonLogo = Util.getEncoded(baseDir + "verizon.PNG");
        String medtronicLogo = Util.getEncoded(baseDir + "Medtronic.png");
        String xfinityLogo = Util.getEncoded(baseDir + "Xfinity.png");

        Project sprint = new Project("sprint-unique-key", "sprint-projectId", "sprint", sprintLogo);

        Project rogers = new Project("rogers-unique-key", "rogers-projectId-edited", "rogers", rogersLogo);
        Project verizon = new Project("verizon-unique-key", "verizon-projectId", "verizon", verizonLogo);
        Project medtronic = new Project("MedtronicPH-unique-key", "MedtronicPH-projectId", "MedtronicPH", medtronicLogo);
        Project smplus = new Project("Medtronic-SMPLUS-unique-key", "Medtronic-SMPLUS-projectId", "MedtronicSMPLUS", medtronicLogo);
        Project xfinity = new Project("Xfinity-unique-key", "Xfinity-projectId", "Xfinity", xfinityLogo);

        List<Project> projects = new ArrayList<>();
        projects.add(xfinity);
        projects.add(rogers);
        projects.add(verizon);
        projects.add(medtronic);
        projects.add(smplus);
        Gson gson = new Gson();
        String body = gson.toJson(projects);
        Map<String, String> header = getHeaderData();
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "addProjects", header, body);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
        return String.valueOf(response.extract().statusCode());
    }

    public void postExecutionData(List<Execution> suiteList) {
        Map<String, List<Execution>> projectExecutionMap = new HashMap<>();
        projectExecutionMap.put(configData.getProjectID(), suiteList);
        Gson gson = new Gson();
        String body = gson.toJson(projectExecutionMap);
        Map<String, String> header = getHeaderData();
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "postExecutionData", header, body);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
    }

    public void createUser(User user) {
        Gson gson = new Gson();
        String body = gson.toJson(user);
        Map<String, String> header = getHeaderData();
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "createUser", header, body);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
    }

    public Result postResultData(Result result) {
        Gson gson = new Gson();
        String body = gson.toJson(result);
        Map<String, String> header = getHeaderData();
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "result", header, body);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
        if (response.extract().statusCode() == 200) {
            return response.extract().body().as(Result.class);
        } else {
            return null;
        }
    }

    public ValidatableResponse getTestSuiteData() {

        Map<String, String> header = getHeaderData();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("projectUniqueKey", configData.getProjectID());
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL + "getUpdateExecution", header, parameters);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
        return response;
    }

    public ValidatableResponse getExecutionStatus() {

        Map<String, String> header = getHeaderData();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("projectUniqueKey", configData.getProjectID());
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL + "executionStatus", header, parameters);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
        return response;
    }

    public ValidatableResponse getLastExecutionId() {

        Map<String, String> header = getHeaderData();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("projectUniqueKey", configData.getProjectID());
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL + "getLastExecutionId", header, parameters);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
        return response;
    }

    public ValidatableResponse getLastExecutionInfo() {

        Map<String, String> header = getHeaderData();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("projectUniqueKey", configData.getProjectID());
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL + "getLastExecutionInfo", header, parameters);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
        return response;
    }

    public ValidatableResponse getExecutionParameters(String executionID) {

        Map<String, String> header = getHeaderData();
        Map<String, String> parameter = new HashMap<>();
        parameter.put("executionId", executionID);
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL + "getExecutionParams", header, parameter);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
        return response;
    }

    public ValidatableResponse getAllExecutionParameters() {

        Map<String, String> header = getHeaderData();
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL + "getAllExecutionParams", header);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
        return response;
    }

    public ValidatableResponse getExecutionPlatformReport(String executionID) {
        Map<String, String> header = getHeaderData();
        Map<String, String> parameter = new HashMap<>();
        parameter.put("executionId", executionID);
        return restAPIHelper.getRequestWithParameters(BASE_URL + "getExecutionIdInfo", header, parameter);
    }

    public String getScreenShot(String resultId) {
        Map<String, String> header = getHeaderData();
        Map<String, String> parameter = new HashMap<>();
        parameter.put("resultId", resultId);
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL + "screenshot", header, parameter);
        return response.extract().statusCode() == 200 ? response.extract().body().jsonPath().get("file") : "";
    }

    public String getLog(String resultId) {
        Map<String, String> header = getHeaderData();
        Map<String, String> parameter = new HashMap<>();
        parameter.put("resultId", resultId);
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL + "log", header, parameter);
        return response.extract().statusCode() == 200 ? response.extract().body().jsonPath().get("file") : "";
    }

    public String postExecutionParams() throws IOException {
        String body = restAPIHelper.getJsonStringFromFile(System.getProperty("user.dir") + File.separator + "/TestSuite/ExecutionSuite.json");
        Map<String, String> header = getHeaderData();
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "executionParams", header, body);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
        return String.valueOf(response.extract().statusCode());
    }

    public List<Node> getNodes() {
        Map<String, String> header = getHeaderData();
        ValidatableResponse response = restAPIHelper.getRequest(BASE_URL + "nodes", header);
        Gson gson = new Gson();
        Type nodeListType = new TypeToken<ArrayList<Node>>() {
        }.getType();
        return gson.fromJson(response.extract().body().asString(), nodeListType);
    }

    public Execution getExecutionInfo(String executionID) {
        Map<String, String> header = getHeaderData();
        Map<String, String> parameter = new HashMap<>();
        parameter.put("executionId", executionID);
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL + "executions", header, parameter);
        return response.extract().statusCode() == 200 ? response.extract().body().as(Execution.class) : null;
    }

    public String postNodes(Node node) {
        Map<String, String> header = getHeaderData();
        Gson gson = new Gson();
        String body = gson.toJson(node);
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "nodes", header, body);
        return String.valueOf(response.extract().statusCode());
    }

    public void postReport(String executionId, File file) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", authToken);
        Map<String, String> parameter = new HashMap<>();
        parameter.put("executionId", executionId);
        ValidatableResponse response = restAPIHelper.uploadFile(BASE_URL + "reports", header, parameter, file);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
    }

    public void postVideo(String resultId, File file) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", authToken);
        Map<String, String> parameter = new HashMap<>();
        parameter.put("resultId", resultId);
        ValidatableResponse response = restAPIHelper.uploadFile(BASE_URL + "video", header, parameter, file);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
    }

    public ValidatableResponse getReport(String executionId) {
        Map<String, String> header = getHeaderData();
        Map<String, String> parameter = new HashMap<>();
        parameter.put("executionId", executionId);
        return restAPIHelper.getRequestWithParameters(BASE_URL + "reports", header, parameter);
    }

    public String postExecutionStatus(ExecutionStatus status) {
        Map<String, String> header = getHeaderData();
        Gson gson = new Gson();
        String body = gson.toJson(status);
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "executionStatus", header, body);
        return String.valueOf(response.extract().statusCode());
    }

    public HubDetails postHubDetails(HubDetails hubDetails) {
        Map<String, String> header = getHeaderData();
        Gson gson = new Gson();
        String body = gson.toJson(hubDetails);
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "hubDetails", header, body);
        if (response.extract().statusCode() == 200) {
            hubDetails.setHubId(response.extract().body().jsonPath().get("hubId"));
        }
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
        return hubDetails;
    }

    public GridNode addGridNode(GridNode gridNode) {
        Map<String, String> header = getHeaderData();
        Gson gson = new Gson();
        String body = gson.toJson(gridNode);
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "addNode", header, body);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
        if (response.extract().statusCode() == HttpStatus.SC_OK) {
            return response.extract().body().as(GridNode.class);
        }
        return null;
    }

    public void removeGridNode(GridNode node) {
        Map<String, String> header = getHeaderData();
        Gson gson = new Gson();
        String body = gson.toJson(node);
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "removeNode", header, body);
        LOGGER.log(Level.INFO, String.valueOf(restAPIHelper.getResponseStatusCode(response)));
    }

    public HubDetails getHubDetails(String projectUniqueKey) {
        Map<String, String> header = getHeaderData();
        Map<String, String> parameter = new HashMap<>();
        parameter.put("projectUniqueKey", projectUniqueKey);
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL + "hubDetails", header, parameter);
        return response.extract().statusCode() == 200 ? response.extract().body().as(HubDetails.class) : null;
    }

    public HubDetails getHub() {
        Map<String, String> header = getHeaderData();
        ValidatableResponse response = restAPIHelper.getRequest(BASE_URL + "hub", header);
        if (response.extract().statusCode() == HttpStatus.SC_OK) {
            return response.extract().body().as(HubDetails.class);
        }
        return null;
    }

    public AcquireDevices addDevice(AcquireDevices acquireDevices) {
        Map<String, String> header = getHeaderData();
        Gson gson = new Gson();
        String jsonBody = gson.toJson(acquireDevices);
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "acquireDevices", header, jsonBody);
        return response.extract().statusCode() == 200 ? response.extract().body().as(AcquireDevices.class) : null;
    }

    public List<UserDevice> getDevices() {
        Map<String, String> header = getHeaderData();
        Map<String, String> parameter = new HashMap<>();
        parameter.put("projectUniqueKey", configData.getProjectID());
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL + "availableDevices", header, parameter);
        return response.extract().statusCode() == 200 ? Arrays.asList(response.extract().body().as(UserDevice[].class)) : new ArrayList<UserDevice>();
    }

    public List<ExecutionStatus> getExecutionStatus(String executionId) {
        Map<String, String> header = getHeaderData();
        Map<String, String> parameter = new HashMap<>();
        parameter.put("executionId", executionId);
        parameter.put("projectUniqueKey", configData.getProjectID());
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL +
                "executionStatus", header, parameter);
        return response.extract().statusCode() == 200 ? Arrays.asList(response.extract().body().as(ExecutionStatus[].class)) : new ArrayList<ExecutionStatus>();
    }

    public ResultResponseJson getResultStatus(String executionId) {
        Map<String, String> header = getHeaderData();
        Map<String, String> parameter = new HashMap<>();
        parameter.put("executionId", executionId);
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL +
                "getFeatureResult", header, parameter);
        return response.extract().statusCode() == 200 ? response.extract().body().as(ResultResponseJson.class) : null;
    }

    public List<UserDevice> addUserDevice(List<UserDevice> userDevice) {
        Map<String, String> header = getHeaderData();
        Gson gson = new Gson();
        String jsonBody = gson.toJson(userDevice);
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "addUserDevices", header, jsonBody);
        return response.extract().statusCode() == 200 ? Arrays.asList(response.extract().body().as(UserDevice[].class)) : new ArrayList<>();
    }

    public List<UserDevice> getUserDevices() {
        Map<String, String> header = getHeaderData();
        Map<String, String> parameter = new HashMap<>();
        parameter.put("projectUniqueKey", configData.getProjectID());
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL + "userDevices", header, parameter);
        return response.extract().statusCode() == 200 ? Arrays.asList(response.extract().body().as(UserDevice[].class)) : new ArrayList<>();
    }

    public UserDevice getUserDevice(String deviceId) {
        Map<String, String> header = getHeaderData();
        Map<String, String> parameter = new HashMap<>();
        parameter.put("deviceId", deviceId);
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL + "userdevice", header, parameter);
        return response.extract().statusCode() == 200 ? response.extract().body().as(UserDevice.class) : null;
    }

    public UserDevice updateUserDevices(UserDevice device) {
        Map<String, String> header = getHeaderData();
        Gson gson = new Gson();
        String jsonBody = gson.toJson(device);
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "editDevice", header, jsonBody);
        return response.extract().statusCode() == 200 ? response.extract().body().as(UserDevice.class) : null;
    }

    public UserDevice deleteUserDevice(String deviceID) {
        Map<String, String> header = getHeaderData();
        Gson gson = new Gson();
        String jsonBody = gson.toJson(deviceID);
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "editdevice", header, jsonBody);
        return response.extract().statusCode() == 200 ? response.extract().body().as(UserDevice.class) : null;
    }

    public String scheduleJob(ScheduleJob scheduleJob) {
        Map<String, String> header = getHeaderData();
        Gson gson = new Gson();
        String jsonBody = gson.toJson(scheduleJob);
        ValidatableResponse response = restAPIHelper.postRequest(BASE_URL + "schedule", header, jsonBody);
        return String.valueOf(response.extract().statusCode());
    }

    public String getPlatformName(String executionId) {
        String name = null;
        Map<String, String> header = getHeaderData();
        Map<String, String> parameter = new HashMap<>();
        parameter.put("executionId", executionId);
        ValidatableResponse response = restAPIHelper.getRequestWithParameters(BASE_URL + "platformName", header, parameter);
        List<Result> result = Arrays.asList(response.extract().body().as(Result[].class));
        if (result.size() > 0) {
            name = result.get(0).getPlatformName();
        }
        return name;
    }

    public static void main(String[] args) throws IOException {
        // LocalDateTime date = LocalDateTime.of(2020, 02, 12, 12, 15, 0);
        DBUpdater dbUpdater = new DBUpdater();
        //dbUpdater.addProject();
       // dbUpdater.getLastExecutionId();
        dbUpdater.updateTestSuite();
       /* User user = new User();
        user.setFullName("Mahesh");
        user.setLoginName("Mahesh");
        user.setPassword("Tech8092");
        user.setProjectUniqueKey(Collections.singletonList("sprint-unique-key"));
        user.setAuthorities(Collections.singletonList(Authority.ROLE_USER));
        dbUpdater.createUser(user);*/
      /*  ScheduleJob job = new ScheduleJob();
        job.setExecutionId("1234");
        job.setStartDate(System.currentTimeMillis() / 1000);
        job.setStartTime((System.currentTimeMillis() / 1000) + 60);
        job.setEndDate(date.atZone(ZoneId.systemDefault()).toEpochSecond());
        job.setName("TestJob");
        job.setProjectUniqueKey(configData.getProjectID());
        job.setTimeZone(TimeZone.getDefault().getID());
        job.setFrequency("Day");
        job.setInterval("2");
        System.out.println(dbUpdater.scheduleJob(job));*/
        // dbUpdater.getExecutionStatus();
        /* Map<String, String> devicesStatus = dbUpdater.getExecutionStatus("5cfa3b35e3989c0a5c1694b0").get(0).getStatus();*/
        // dbUpdater.addProject();
        // dbUpdater.getTestSuite();
        //dbUpdater.getResultStatus("5b7fe3dc14cfae3158ba841b");
        // updateTestSuite();
        //  postExecutionParams();
        // getTestSuiteData();
    /* Node node = new Node();
     SupportedPlatforms supportedPlatforms = new SupportedPlatforms();
     supportedPlatforms.setIsAndroid("true");
             node.setIpAddress("172.18.8.227");
             node.setLabel("Node3");
             node.setSupportedPlatforms(supportedPlatforms);
     postNodes(node);*/
        //  getExecutionPlatformReport("5acdd983261ef70c98348aa5");
        // getNodes();
        // getExecutionInfo("11April2018");
//     getScreenShot("5acdd983261ef70c98348aa5");

      /*  File file = new File(System.getProperty("user.dir")+"Reports/Execution_Report.html");
        postReport(Constants.EXECUTION_ID,file);*/
        // byte[]a= ;
        // List<Reports> reports = getReport("5b7fe3dc14cfae3158ba841b").extract().body().jsonPath().getList("",Reports.class);
//        List<Reports> reportsList = Arrays.asList(getReport("5b7fe3dc14cfae3158ba841b").extract().body().as(Reports[].class));
        //       FileUtils.writeByteArrayToFile(new File("C:/project/report.html"),reportsList.get(0).getFileBytes());
        // getAllExecutionParameters();
        //getHubDetails(TestExecutor.configData.getProjectID());
       /*  AcquireDevices acquireDevices = new AcquireDevices();
        UserDevice device = new UserDevice();
        device.setDeviceName("chrome");
        List<UserDevice> deviceList = dbUpdater.getUserDevices();*/
        // deviceList.add(device);
       /* acquireDevices.setDeviceList(deviceList);
        acquireDevices.setUserID("5d0e2057c6119d26209b1caf");
        dbUpdater.addDevice(acquireDevices);*/
        // getExecutionParameters("5b7fe3dc14cfae3158ba841b");
        //  List<UserDevice> deviceList = dbUpdater.getUserDevices();
    }
}
