package com.gl.testngfw.setup;

import com.gl.testngfw.common.Constants;
import com.gl.testngfw.common.DBUpdater;
import com.gl.testngfw.enums.Browser;
import com.gl.testngfw.enums.DeviceStatus;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.report.model.ConfigData;
import com.gl.testngfw.report.model.GridNode;
import com.gl.testngfw.report.model.HubDetails;
import com.gl.testngfw.report.model.UserDevice;
import com.gl.testngfw.utility.CommandLineExecutor;
import com.gl.testngfw.utility.Util;
import org.apache.commons.exec.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class NodeUtil {
    private static final Logger LOGGER = Logger.getLogger(NodeUtil.class.getName());
    private static DBUpdater dbUpdater = new DBUpdater();
    private static GridNode node = new GridNode();
    private static ConfigData configData = TestExecutor.getConfigData();

    private NodeUtil() {
    }

    public static void registerWebGridNode(Browser browser) {
        HubDetails details = configData.isGafCloud() ? dbUpdater.getHub() : HubUtil.getHubDetails();
        String registrationURL = details.getHubRegistrationUrl().toString();
        String port = null;
        try {
            port = Util.getAvailablePort();

            String jarPath = Util.downloadJar(Constants.SERVER_JAR_URL, Constants.SERVER_VER);
            String driverpath = Util.getWebDriverBinaryPath(browser).replace("Optional[","").replace("]","");
            driverpath = "/Users/shalishtrakroo/GLChildJOB/src/main/resources/chromedriver";
            System.out.println("DriverPath:::: "+driverpath);

            String command = String.format("java -Dwebdriver.chrome.driver=%s -jar %s -role node " +
                    "-hub %s -port %s -browser browserName=\"%s\"," +
                    "maxInstances=%s -timeout 20000",
                    driverpath, jarPath, registrationURL, port, browser.getValue(), 1);
            System.out.println("Node register command:: "+command);

            if (configData.isGafCloud()) {
                UserDevice device = addDevice(browser.getValue());
                node.setDeviceId(device.getId());
                node.setPort(Integer.parseInt(port));
                node.setHubId(details.getHubId());
                node = dbUpdater.addGridNode(node);
                while (true) {
                    checkRestartNode(command, 10000, details, device);
                    Thread.sleep(5000);
                    LOGGER.log(Level.INFO, "Restarting Node");
                }
            } else {
               // command = "java " + command;
                CommandLineExecutor.executeCommand(command);
                Util.waitTillAllServerIsLaunched(port, 60000L);
                Constants.getBrowserPorts().put(browser.name(), Integer.valueOf(port));
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    private static void checkRestartNode(String command, long interval, HubDetails hubDetails, UserDevice device)
            throws IOException, InterruptedException {
        CommandLine cmdLine = new CommandLine("java");
        cmdLine.addArguments(command);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(new PumpStreamHandler());
        executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
        DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler();
        executor.execute(cmdLine, handler);
        String status = device.getStatus().equals(DeviceStatus.ACQUIRED.name()) ? DeviceStatus.ACQUIRED.name() : DeviceStatus.AVAILABLE.name();
        device.setStatus(status);
        dbUpdater.updateUserDevices(device);
        while (!handler.hasResult()) {
            Thread.sleep(interval);
        }
        if (handler.hasResult()) {
            hubDetails.getGridNodes().remove(node);
            dbUpdater.removeGridNode(node);
            ExecuteException e = handler.getException();
            LOGGER.log(Level.INFO, e.getMessage());
        }
    }

    private static UserDevice addDevice(String browser) {
        List<UserDevice> deviceList = new ArrayList<>();
        UserDevice device = new UserDevice();
        device.setDeviceName(browser);
        device.setDeviceOS(System.getProperty("os.name"));
        device.setStatus(DeviceStatus.PENDING.name());
        device.setHostIp(Util.getIPAddress());
        device.setHostName(Util.getHostName());
        device.setProjectUniqueKey(configData.getProjectID());
        deviceList.add(device);
        List<UserDevice> list = dbUpdater.addUserDevice(deviceList);
        return list.get(0);
    }

    public static void shoutDownAllNodes() {
        Util.clearSeleniumProcess();
    }

    public static void main(String[] args) throws Exception {
        registerWebGridNode(Browser.CHROME);
    }
}
