package com.commonUtils;

import java.sql.*;

public class ConnectionManager {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        PreparedStatement dateStatement = null;
        PreparedStatement prepStmt = null;
        PreparedStatement schemaStatement = null;

       /* String  url="jdbc:oracle:thin:@(description=(address_list=(load_balance=on)(failover=on)(address=(protocol=tcp)(host=plsaa806.corp.sprint.com)(port=1521)))(connect_data=(service_name=IIDR101)(failover_mode=(type=select)(method=basic))))";
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
        System.out.println( dateStatement.executeQuery());

       // String rptQuery = "select /*+ parallel(ds,6) */ * from device_appl_stus ds where ds.DEVICE_CONTEXT_FK in ('287293') order by ds.MAINT_LAST_TS DESC";
        String rptQuery = "select /*+ parallel(ds,6) */ * from device_appl_stus ds  where ds.DEVICE_CONTEXT_FK in ('287293') AND ds.PACKAGE_NME='com.le.carracing' AND trunc(ds.MAINT_LAST_TS) = trunc(sysdate) AND ds.device_log_txt LIKE 'Install Successful'";

       // String rptQuery = "select /*+ parallel(ds,6) */ * from device_appl_stus ds where ds.DEVICE_CONTEXT_FK in ('215929861') AND trunc(ds.MAINT_LAST_TS) = trunc(sysdate) order by ds.MAINT_LAST_TS DESC";
        Statement smt = connection.createStatement();



        ResultSet rs = smt.executeQuery(rptQuery);

        //System.out.println(rs.first());
        while (rs.next()) {

            System.out.println("First row Data:::: "+rs.getString(16));
        }


    }
}
