package com.commonUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.report.model.PropertyModel;
import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.utility.FileUtil;
import com.screens.web.*;
import com.screens.web.mobile.android.AndroidLoginScreen;
import com.screens.web.mobile.android.AndroidMCMAdminScreen;
import com.screens.web.mobile.android.AndroidSearchScreen;
import com.screens.web.mobile.android.NetflixAndroidTestScreen;

import java.sql.*;
import java.util.Map;

public class BaseTest extends InitializerScript {
    protected static Map<String, PropertyModel> projectConfig = FileUtil.getConfigMAP("ProjectConfig.properties");
    protected String BASE_URL = null;
    //Initialised screens
    protected NetflixTestScreen netflixTestScreen;
    protected NetflixAndroidTestScreen netflixAndroidTestScreen;
    protected GLO_LoginScreen loginScreen;
    protected GLO_HomeScreen homeScreen;
    protected GLO_SearchScreen searchScreen;
    protected AndroidLoginScreen androidLoginScreen;
    protected AndroidSearchScreen androidSearchScreen;
    protected AndroidMCMAdminScreen androidMCMAdminScreen;

    protected ManagementPortalScreen managementPortalScreen;


    //For Web Platform
    @Override
    public void initScreens() {

        if (isWeb()) {
            loginScreen = new GLO_LoginScreen();
            searchScreen = new GLO_SearchScreen();
            homeScreen = new GLO_HomeScreen();
            netflixTestScreen = new NetflixTestScreen();

            managementPortalScreen = new ManagementPortalScreen();
            try {
                getWebDriver().get(projectConfig.get("BaseURL").getValue());
            } catch (Exception e) {
                FrameworkLogger.logError(e);
            }
        }
        if(isMobile()){
            androidLoginScreen = new AndroidLoginScreen();
            androidSearchScreen = new AndroidSearchScreen();
            netflixAndroidTestScreen = new NetflixAndroidTestScreen();
            androidMCMAdminScreen = new AndroidMCMAdminScreen();
        }
        if (isAPI()) {
            //For API Platform
            BASE_URL = projectConfig.get("API_BaseURL").getValue();
            FrameworkLogger.logStep("Base URL "+BASE_URL);
            System.out.println("Base URL "+BASE_URL);
        }
    }

   /* @BeforeClass
    public void startServer() {
        try {
            String jarPath = FileUtil.getFilePathFromResourcePath(this.getClass(), "EmployeeAPI.jar");
            CommandLineExecutor.executeCommand("java -jar " + jarPath);
            Util.waitTillAllServerIsLaunched("8080", 80000L);
        } catch (Exception e) {
        }
    }*/
   protected Map<String, Object> getExpectedMap(TestUser expectedResponse) {
       ObjectMapper mapper = new ObjectMapper();
       return mapper.convertValue(expectedResponse, new TypeReference<Map<String, Object>>() {
       });
   }

    /**
     * Method to setup db connection
     * @return
     */

    public Statement sqlDBConnection() throws ClassNotFoundException, SQLException {
        PreparedStatement dateStatement = null;
        PreparedStatement prepStmt = null;
        PreparedStatement schemaStatement = null;
/*
        String  url="jdbc:oracle:thin:@(description=(address_list=(load_balance=on)(failover=on)(address=(protocol=tcp)(host=plsaa806.corp.sprint.com)(port=1521)))(connect_data=(service_name=IIDR101)(failover_mode=(type=select)(method=basic))))";
        String user = "IID_RPT_USER";
        String key= "pwjzfHxj";*/

        String  url= "jdbc:oracle:thin:@(description=(address_list=(load_balance=on)(failover=on)(address=(protocol=tcp)(host=treaa537.test.sprint.com)(port=1525)))(connect_data=(service_name=IIDT1APPS)(failover_mode=(type=select)(method=basic))))";
        String user = "iid_app_user";
        String key= "App!T3st";

        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection connection = DriverManager.getConnection(url,user,key);

        // connection;

        String schemaSQL = "ALTER SESSION SET CURRENT_SCHEMA = IIDUSER";
        String dateSQL = "alter session set nls_date_format = 'DD-MON-YYYY HH24:mi:SS'";
        schemaStatement = connection.prepareStatement(schemaSQL);
        dateStatement = connection.prepareStatement(dateSQL);

        schemaStatement.executeQuery();
       // System.out.println( dateStatement.executeQuery());

       // String rptQuery = "select /*+ parallel(ds,6) */ * from device_appl_stus ds where ds.DEVICE_CONTEXT_FK in ('215929861') order by ds.MAINT_LAST_TS DESC";


        // String rptQuery = "select /*+ parallel(ds,6) */ * from device_appl_stus ds where ds.DEVICE_CONTEXT_FK in ('215929861') AND trunc(ds.MAINT_LAST_TS) = trunc(sysdate) order by ds.MAINT_LAST_TS DESC";
        Statement smt = connection.createStatement();
       // ResultSet resultSet = smt.executeQuery(rptQuery);
        return smt;

    }

}
