package com.gl.testngfw.utility;

import com.gl.testngfw.api.RestAPIExtension;
import com.gl.testngfw.report.model.ConfigData;
import io.restassured.response.ValidatableResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Network Log Helper class
 */
class NetworkLogCaptureHelper {
    private static final Logger LOGGER = Logger.getLogger(NetworkLogCaptureHelper.class.getName());
    private static RestAPIExtension restAPI = new RestAPIExtension(false);
    private static ConfigData configData = new ConfigData();
    private static String url = "http://localhost:8080/proxy/";

    private NetworkLogCaptureHelper() {
    }

    public static String startServer() throws InterruptedException {

        if (isWindows()) {
            try {
                Runtime.getRuntime().exec("cmd /c start " + configData.getBrowserMobProxyLocation() + " -port 8080");
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "", e);
            }
        } else {
            CommandLineExecutor.executeCommand(configData.getBrowserMobProxyLocation() + " -port 8080");
            waitTillAllServerIsLaunched("8080");
        }
        return "8080";
    }

    public static void startProxy(String port) throws InterruptedException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("trustAllServers", "true");
        paramMap.put("port", port);
        restAPI.postRequestWithParameter(url, paramMap);
        waitTillAllServerIsLaunched(port);
    }

    public static void startHARCapture(String harName, String port) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("initialPageRef", harName);
        restAPI.putRequestWithParameters(url + port + "/har", paramMap);

    }

    public static ValidatableResponse getHARCapture(String port) {
        return restAPI.getRequest(url + port + "/har");

    }

    public static ValidatableResponse closeProxy(String port) {
        return restAPI.deleteRequest(url + port);
    }

    public static void closeServer() {
        List<String> result = CommandLineExecutor.runProcess("lsof -i tcp:8080");
        if (!result.isEmpty()) {
            String pid = result.get(1).split(" ")[4];
            CommandLineExecutor.executeCommand("kill " + pid);
        }
    }

    private static void waitTillAllServerIsLaunched(String port) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        List list;
        do {
            do {
                do {
                    Thread.sleep(3000L);
                    String command;
                    if (isWindows()) {
                        command = "netstat -anp tcp | findstr " + port;
                        list = CommandLineExecutor.runProcess(command);
                    } else {
                        command = "netstat -anp tcp | grep " + port;
                        list = CommandLineExecutor.runProcess(command);
                    }
                } while (list == null);
            } while (list.isEmpty());
            LOGGER.log(Level.INFO, (String) list.get(0));
        }
        while (!((String) list.get(0)).contains("LISTENING") && !((String) list.get(0)).contains("LISTEN") &&
                System.currentTimeMillis() - startTime <= 25000L);
        LOGGER.log(Level.INFO, "Server wait Time" + (System.currentTimeMillis() - startTime));
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }
}
