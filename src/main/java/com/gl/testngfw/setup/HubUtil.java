package com.gl.testngfw.setup;

import com.gl.testngfw.api.RestAPIExtension;
import com.gl.testngfw.common.Constants;
import com.gl.testngfw.common.DBUpdater;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.report.model.ConfigData;
import com.gl.testngfw.report.model.HubDetails;
import com.gl.testngfw.utility.CommandLineExecutor;
import com.gl.testngfw.utility.FileUtil;
import com.gl.testngfw.utility.Util;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.exec.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HubUtil {
    private static final Logger LOGGER = Logger.getLogger(HubUtil.class.getName());
    private static HubDetails hubDetails = new HubDetails();
    private static DBUpdater dbUpdater = new DBUpdater();
    private static ConfigData configData = TestExecutor.getConfigData();

    private HubUtil() {
    }

    public static void startCommandLineGrid() {
        String seleniumJar = null;
        try {
            seleniumJar = Util.downloadJar(Constants.SERVER_JAR_URL, Constants.SERVER_VER);
            String availablePort = Util.getAvailablePort();
            String ipAddress = Constants.getLocalIp();
            String servletJar = FileUtil.getFilePath(System.getProperty("user.dir"), "GAFServlet.jar");
            String command;
            if (configData.isGafCloud()) {
                command = String.format(" -cp %s;%s org.openqa.grid.selenium.GridLauncherV3 -role hub -host %s -port %s -servlet grid.GAFNodeListServlet", seleniumJar, servletJar, Constants.getLocalIp(), Integer.parseInt(availablePort));
            } else {
                command = String.format("java -cp %s; org.openqa.grid.selenium.GridLauncherV3 -role hub -host %s -port %s", seleniumJar, Constants.getLocalIp(), availablePort);
            }

            System.out.println("Commandline grid:: "+command);
            command = Util.isWindows() ? command : command.replaceAll(";", ":");
            if (!configData.isGafCloud()) {
              //  CommandLineExecutor.executeCommand(command);
                Util.executeCommand(command);
                Util.waitTillAllServerIsLaunched(availablePort, 80000L);
            }
            Constants.getHubDetails().put(ipAddress, availablePort);
            Constants.setGridHubUrl(String.format("http://%s:%s/wd/hub", ipAddress, availablePort));
            setHubDetails(availablePort, ipAddress);
            if (configData.isGafCloud()) {
                while (true) {
                    checkRestartHub(command, 20000);
                    Thread.sleep(5000);
                    LOGGER.log(Level.INFO, "Restarting Hub");
                }
            }
            Constants.setIsHubRunning(true);
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    private static void setHubDetails(String availablePort, String ipAddress) throws MalformedURLException {
        hubDetails.setHubUrl(new URL(String.format("http://%s:%s/wd/hub", ipAddress, availablePort)));
        hubDetails.setIpAddress(ipAddress);
        hubDetails.setPort(Integer.valueOf(availablePort));
        hubDetails.setHubRegistrationUrl(new URL(String.format("http://%s:%s/grid/register", ipAddress, availablePort)));
        hubDetails.setProjectUniqueKey(configData.getProjectID());
        if (configData.isUseDB()) {
            hubDetails = dbUpdater.postHubDetails(hubDetails);
        }
        LOGGER.log(Level.INFO, String.valueOf(hubDetails.getHubUrl()));
    }

    public static void shutDownGrid() {
        RestAPIExtension restAPIHelper = new RestAPIExtension(false);
        URL appiumURL = !configData.isGafCloud() ? getHubURL() : dbUpdater.getHubDetails(configData.getProjectID()).getHubUrl();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("Content-Type", "application/json");
        paramMap.put("charset", "utf-8");
        paramMap.put("action", "shutdown");
        restAPIHelper.postRequestWithParameter(appiumURL.toString().replaceAll("wd/hub", "lifecycle-manager"), paramMap);
    }

    public static ValidatableResponse getNodeDetails() {
        RestAPIExtension restAPIHelper = new RestAPIExtension(false);
        URL appiumURL = getHubURL();
        return restAPIHelper.getRequest(appiumURL.toString().replaceAll("wd/hub", "/grid/admin/GAFNodeListServlet/"));
    }

    public static HubDetails getHubDetails() {
        return hubDetails;
    }

    public static URL getHubURL() {
        URL appiumURL = null;
        try {
            appiumURL = configData.isGafCloud() ? new URL(Constants.CLOUD_URL) : hubDetails.getHubUrl();
        } catch (MalformedURLException e) {
            LOGGER.log(Level.INFO, e.getMessage());
        }
        return appiumURL;
    }

    private static void checkRestartHub(String command, long interval)
            throws IOException, InterruptedException {
        CommandLine cmdLine = new CommandLine("java");
        cmdLine.addArguments(command);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(new PumpStreamHandler());
        executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
        DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler();
        executor.execute(cmdLine, handler);
        while (!handler.hasResult()) {
            Thread.sleep(interval);
        }
        if (handler.hasResult()) {
            ExecuteException e = handler.getException();
            LOGGER.log(Level.INFO, e.getMessage());
        }
    }

    public static void main(String[] args) {
        startCommandLineGrid();
    }
}
