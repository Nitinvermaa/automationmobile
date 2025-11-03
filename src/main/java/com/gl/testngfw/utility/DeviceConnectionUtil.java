package com.gl.testngfw.utility;

import com.gl.testngfw.api.DeviceHelper;
import com.gl.testngfw.common.DBUpdater;
import com.gl.testngfw.enums.DeviceStatus;
import com.gl.testngfw.report.model.ConfigData;
import com.gl.testngfw.report.model.UserDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.SECONDS;

public class DeviceConnectionUtil {
    private static final Logger LOGGER = Logger.getLogger(DeviceConnectionUtil.class.getName());
    private static DBUpdater dbUpdater = new DBUpdater();
    private static ConfigData configData = new ConfigData();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Map<String, UserDevice> deviceMap = new HashMap<>();
    private Map<String, UserDevice> newDeviceMap = new HashMap<>();

    public static void main(String[] args) {
        DeviceConnectionUtil scheduler = new DeviceConnectionUtil();
        scheduler.monitorConnectedDevices();
    }

    private void monitorConnectedDevices() {

        final Runnable deviceMonitor = () -> {
            try {
                deviceMap = new HashMap<>();
                newDeviceMap = DeviceHelper.initDeviceModelInfo();
                List<UserDevice> dbDeviceList = dbUpdater.getUserDevices();
                getDeviceMap(dbDeviceList);
                for (String id : newDeviceMap.keySet()) {
                    updateUserDevice(id);
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "No new Device found", e);
            }
        };
        scheduler.scheduleAtFixedRate(deviceMonitor, 10, 20, SECONDS);
    }

    private void getDeviceMap(List<UserDevice> dbDeviceList) {
        for (UserDevice device : dbDeviceList) {
            if (device.getHostName().equals(Util.getHostName()) &&
                    newDeviceMap.isEmpty() || !newDeviceMap.containsKey(device.getDeviceId())) {
                if (!device.getStatus().equals(DeviceStatus.REMOVED.name())) {
                    device.setStatus(DeviceStatus.REMOVED.name());
                    dbUpdater.updateUserDevices(device);
                }
            }
            deviceMap.put(device.getDeviceId(), device);
        }
    }

    private void updateUserDevice(String id) {
        if (deviceMap.containsKey(id) && !deviceMap.get(id).equals(newDeviceMap.get(id))) {
            UserDevice device = deviceMap.get(id);
            UserDevice newDevice = deviceMap.get(id);
            device.setDeviceId(newDevice.getDeviceId());
            device.setDeviceName(newDevice.getDeviceName());
            device.setDeviceVersion(newDevice.getDeviceVersion());
            device.setDeviceOS(newDevice.getDeviceOS());
            device.setManufacturer(newDevice.getManufacturer());
            device.setStatus(DeviceStatus.AVAILABLE.name());
            device.setHostIp(newDevice.getHostIp());
            device.setHostName(newDevice.getHostName());
            device.setProjectUniqueKey(configData.getProjectID());
            device.setNetwork(newDevice.getNetwork());
            dbUpdater.updateUserDevices(device);
        } else if (!deviceMap.containsKey(id)) {
            addUserDevice(id);
        }
    }

    private void addUserDevice(String id) {
        List<UserDevice> deviceList = new ArrayList<>();
        UserDevice device = new UserDevice();
        device.setDeviceId(newDeviceMap.get(id).getDeviceId());
        device.setDeviceName(newDeviceMap.get(id).getDeviceName());
        device.setDeviceVersion(newDeviceMap.get(id).getDeviceVersion());
        device.setDeviceOS(newDeviceMap.get(id).getDeviceOS());
        device.setManufacturer(newDeviceMap.get(id).getManufacturer());
        device.setStatus(DeviceStatus.AVAILABLE.name());
        device.setHostIp(Util.getIPAddress());
        device.setHostName(Util.getHostName());
        device.setProjectUniqueKey(configData.getProjectID());
        deviceList.add(device);
        dbUpdater.addUserDevice(deviceList);
    }
}