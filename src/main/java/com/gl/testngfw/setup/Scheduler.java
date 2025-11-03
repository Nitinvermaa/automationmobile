package com.gl.testngfw.setup;

import com.gl.testngfw.api.DeviceHelper;
import com.gl.testngfw.common.Constants;
import com.gl.testngfw.common.DBUpdater;
import com.gl.testngfw.enums.DeviceStatus;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.report.model.GridNode;
import com.gl.testngfw.report.model.HubDetails;
import com.gl.testngfw.report.model.UserDevice;
import com.gl.testngfw.utility.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Scheduler {
    private static final Logger LOGGER = Logger.getLogger(Scheduler.class.getName());
    private static DBUpdater dbUpdater = new DBUpdater();
    private ScheduledFuture<?> deviceMonitorScheduler = null;
    private ScheduledExecutorService executorService =
            Executors.newScheduledThreadPool(1);
    private Map<String, UserDevice> deviceMap = new HashMap<>();
    private Map<String, UserDevice> newDeviceMap = new HashMap<>();
    private List<String> disconnectedDevList = new ArrayList<>();

    private static UserDevice addDevice(UserDevice deviceModel) {
        List<UserDevice> deviceList = new ArrayList<>();
        UserDevice device = new UserDevice();
        device.setDeviceId(deviceModel.getDeviceId());
        device.setDeviceName(deviceModel.getDeviceName());
        device.setDeviceVersion(deviceModel.getDeviceVersion());
        device.setDeviceOS(deviceModel.getDeviceOS());
        device.setManufacturer(deviceModel.getManufacturer());
        device.setStatus(DeviceStatus.PENDING.name());
        device.setHostIp(Util.getIPAddress());
        device.setHostName(Util.getHostName());
        device.setProjectUniqueKey(TestExecutor.getConfigData().getProjectID());
        deviceList.add(device);
        List<UserDevice> list = dbUpdater.addUserDevice(deviceList);
        return list.get(0);
    }

    public static void main(String[] args) {
        String browser = System.getProperty("BrowserName");
        if (!browser.isEmpty()) {
            NodeUtil.registerWebGridNode(Util.getBrowserName(browser));
        }
        Scheduler scheduler = new Scheduler();
        scheduler.monitorConnectedDevices();
    }

    public void monitorConnectedDevices() {
        HubDetails details = dbUpdater.getHubDetails(TestExecutor.getConfigData().getProjectID());
        final Runnable deviceMonitor = () -> {
            try {
                newDeviceMap = DeviceHelper.initDeviceModelInfo();
                if (!deviceMap.keySet().equals(newDeviceMap.keySet())) {
                    addGridNode(details);
                    removeGridNode(details);
                }
            } catch (Exception e) {
                LOGGER.log(Level.INFO, "No new Device found", e);
            }
        };
        deviceMonitorScheduler = executorService.scheduleAtFixedRate(deviceMonitor, 10, 20, SECONDS);
    }

    private void removeGridNode(HubDetails details) throws InterruptedException {
        for (String dev : deviceMap.keySet()) {
            if (!newDeviceMap.containsKey(dev)) {
                if (disconnectedDevList.contains(dev)) {
                    removeGridDevice(details, dev);
                } else {
                    disconnectedDevList.add(dev);
                }
            }
        }
    }

    private void addGridNode(HubDetails details) throws IOException {
        for (String id : newDeviceMap.keySet()) {
            if (!deviceMap.containsKey(id)) {
                int port = startAppiumService(details, id);
                deviceMap.put(id, newDeviceMap.get(id));
                addGridNode(details, id, port);
            }
        }
    }

    private int startAppiumService(HubDetails details, String id) throws IOException {
        int port = Integer.parseInt(Util.getAvailablePort());
        AppiumManager.writeNodeConfigFile(details.getIpAddress(),
                details.getPort(), Constants.getLocalIp(), port, newDeviceMap.get(id));
        if (id.length() >= 40) {
            AppiumManager.appiumServerForIOS(id, port);
        } else {
            AppiumManager.appiumServerForAndroid(id, port);
        }
        return port;
    }

    private void removeGridDevice(HubDetails details, String dev) throws InterruptedException {
        AppiumManager.stopService(dev);
        deviceMap.remove(dev);
        disconnectedDevList.remove(dev);
        for (GridNode node : details.getGridNodes()) {
            UserDevice device = dbUpdater.getUserDevice(dev);
            if (!node.getDeviceId().equals(device.getId())) {
                dbUpdater.removeGridNode(node);
                device.setStatus(DeviceStatus.REMOVED.name());
                dbUpdater.updateUserDevices(device);
            }
        }
    }

    private void addGridNode(HubDetails details, String id, int port) {
        UserDevice device = addDevice(newDeviceMap.get(id));
        GridNode node = new GridNode();
        node.setDeviceId(device.getId());
        node.setPort(port);
        node.setHubId(details.getHubId());
        dbUpdater.addGridNode(node);
    }

    public void stopSchedulerService() {
        deviceMonitorScheduler.cancel(true);
    }
}
