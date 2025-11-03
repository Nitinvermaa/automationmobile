package com.gl.testngfw.api;

import com.gl.testngfw.common.Constants;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.report.model.MobileNetwork;
import com.gl.testngfw.report.model.UserDevice;
import com.gl.testngfw.setup.InitializerScript;
import com.gl.testngfw.utility.CommandLineExecutor;
import com.gl.testngfw.utility.Util;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class for Device commands.
 */
public class DeviceHelper {
    private static final Logger LOGGER = Logger.getLogger(DeviceHelper.class.getName());

    private DeviceHelper() {
    }

    /**
     * Method to get Device Name
     *
     * @return : list of devices attached.
     */
    private static List<String> getDeviceUDID() {
        List<String> deviceName = getDeviceName(getConnectedDevices());
        if (deviceName.isEmpty()) {
            killADBServer();
            startADBServer();
            waitForAdb();
            LOGGER.log(Level.INFO, "Retry to get connected devices");
            deviceName = getDeviceName(getConnectedDevices());
        }
        if (deviceName.isEmpty()) {
            LOGGER.log(Level.INFO, "Device not Connected");
        }
        return deviceName;
    }

    public static List<String> getConnectedDevices() {
        List<String> devices = new ArrayList<>();
        if (isWindows()) {
            devices = getDeviceName(AdbCommandsFactory.getDevicesCommand());
        } else {
            List<String> androidDevices = CommandLineExecutor.runProcess("adb devices | grep -w 'device'");
            if (androidDevices != null && !androidDevices.isEmpty()) {
                devices.addAll(androidDevices);
            }
            List<String> iosDevices = CommandLineExecutor.runProcess("idevice_id -l");
            devices.addAll(iosDevices);
        }
        return devices;
    }

    private static List<String> getDeviceName(List<String> devices) {
        List<String> deviceName = new ArrayList<>();
        if (devices != null) {
            for (String device : devices) {
                if ("List of devices attached ".equals(device) || "".equals(device)) {
                    deviceName.remove(device);
                } else if (device.contains(":")) {
                    deviceName.add(device.replace(":", ""));
                } else {
                    deviceName.add(device.replace("\tdevice", ""));
                }
            }
        }
        return deviceName;
    }

    /**
     * Method to get device version
     *
     * @param deviceName : list of device name
     * @return : list of device version
     */
    public static List<String> deviceVersion(List<String> deviceName) {
        List<String> deviceVersion = new ArrayList<>();
        for (String aDeviceName : deviceName) {
            deviceVersion.add(getDeviceVersion(aDeviceName));
        }
        return deviceVersion;
    }

    public static Map<String, UserDevice> initDeviceModelInfo() {
        List<String> deviceList;
        Map<String, UserDevice> deviceInfoMap = new HashMap<>();
        deviceList = getDeviceUDID();
        for (String device : deviceList) {
            UserDevice dev = new UserDevice();
            dev.setDeviceId(device);
            dev.setDeviceName(getDeviceModelName(device));
            dev.setDeviceDisplayName(getDeviceDisplayName(device));
            dev.setDeviceVersion(getDeviceVersion(device));
            String os = device.length() >= 40 ? Constants.IOS : Constants.ANDROID;
            dev.setDeviceOS(os);
            dev.setManufacturer(getDeviceManufacturer(device));
            dev.setHostName(Util.getHostName());
            dev.setHostIp(Util.getIPAddress());
            dev.setBluetooth(getBluetoothStatus(device));
           // dev.setNetwork(getMobileNetwork(device));
           // dev.setBatteryStatus(getBatteryStatus(device));
            deviceInfoMap.put(device, dev);
        }
        return deviceInfoMap;
    }

    private static String getBatteryStatus(String deviceUDID) {
        String status;
        if (deviceUDID.length() >= 40) {
            status = CommandLineExecutor.runProcess(AdbCommandsFactory.getBatteryCommand()).get(0);
        } else {
            status = CommandLineExecutor.runProcess("ideviceinfo -u  " + deviceUDID + " -q com.apple.mobile.battery |grep BatteryCurrentCapacity").get(0);
        }
        return status;
    }

    private static MobileNetwork getMobileNetwork(String deviceID) {
        MobileNetwork mobileNetwork = new MobileNetwork();
        if (deviceID.length() <= 40) {
            List<String> network = Arrays.asList(CommandLineExecutor.runProcess(AdbCommandsFactory.getNetworkStatus(deviceID)).get(0).split(","));
            if (network.size() > 1) {
                //setNetworkState(mobileNetwork, network);
            } else {
              //  network = Arrays.asList(CommandLineExecutor.runProcess("adb -s " + deviceID + " shell dumpsys wifi | grep mNetworkInfo").get(0).split(","));
                //setNetworkState(mobileNetwork, network);
            }
        } else {
            //TODO set mobile network state for IOS
        }
        return mobileNetwork;
    }

    private static void setNetworkState(MobileNetwork mobileNetwork, List<String> network) {
        mobileNetwork.setType(network.get(0).substring(network.get(0).indexOf("["), network.get(0).indexOf("]")).trim());
        String name = network.get(3).contains("ssid") ? "" : network.get(3).substring(network.get(3).indexOf(":")).trim();
        mobileNetwork.setName(name);
        mobileNetwork.setAvailable(network.get(5).substring(network.get(5).indexOf(":")).trim());
        mobileNetwork.setRoaming(network.get(6).substring(network.get(6).indexOf(":"), network.get(6).indexOf("]")).trim());
        mobileNetwork.setState(network.get(1).contains("CONNECTED") ? "Connected" : "Disconnected");
    }

    private static String getBluetoothStatus(String deviceUDID) {
        boolean state = false;
        if (deviceUDID.length() < 40) {
            state = "1".equals(CommandLineExecutor.runProcess(AdbCommandsFactory.getBluetoothStatus(deviceUDID)).get(0));
        } else {
            //TODO set mobile bluetooth state for IOS
        }
        return String.valueOf(state);
    }

    private static String getDeviceVersion(String deviceUDID) {
        String osVersion = null;
        if (isWindows()) {
            String deviceId = deviceUDID.replace("\tdevice", "");
            osVersion = String.valueOf(CommandLineExecutor.runProcess(
                    AdbCommandsFactory.getDeviceVersionCommand(deviceId))).replace("[", "");

            osVersion = osVersion.replace("]", "");
        } else {
            String deviceId = deviceUDID.trim();

            if (deviceId.length() >= 40) {
                String iosDeviceId = CommandLineExecutor.runProcess(
                        "ideviceinfo -u " + deviceId + " |grep ProductVersion").get(0);
                osVersion = iosDeviceId.substring(iosDeviceId.indexOf(" ")).trim();
            } else {
                List<String> androidDeviceId = CommandLineExecutor.runProcess(AdbCommandsFactory.getDeviceVersionCommand(deviceId));
                if (androidDeviceId != null && !androidDeviceId.isEmpty()) {
                    osVersion = androidDeviceId.get(0);
                }
            }
        }

        return osVersion;
    }

    /**
     * Method to get Device id
     *
     * @param deviceNameList : list of device name
     * @return : list of device id
     */
    public static List<String> deviceID(List<String> deviceNameList) {
        List<String> deviceID = new ArrayList<>();
        deviceID.addAll(deviceNameList);
        return deviceID;
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }

    /**
     * Method to get iphone device list
     *
     * @return : iPhone models Map
     */
    private static Map<String, String> getIPhoneModelList() {
        Map<String, String> deviceMap = new HashMap<>();
        deviceMap.put("iPhone4,1", "iPhone 4S");
        deviceMap.put("iPhone5,1", "iPhone 5");
        deviceMap.put("iPhone5,2", "iPhone 5");
        deviceMap.put("iPhone5,3", "iPhone 5c");
        deviceMap.put("iPhone5,4", "iPhone 5c");
        deviceMap.put("iPhone6,1", "iPhone 5s");
        deviceMap.put("iPhone6,2", "iPhone 5s");
        deviceMap.put("iPhone7,1", "iPhone 6 Plus");
        deviceMap.put("iPhone7,2", "iPhone 6");
        deviceMap.put("iPhone8,1", "iPhone 6s");
        deviceMap.put("iPhone8,2", "iPhone 6s Plus");
        deviceMap.put("iPhone8,4", "iPhone SE");
        deviceMap.put("iPhone9,1", "iPhone 7");
        deviceMap.put("iPhone9,2", "iPhone 7 Plus");
        deviceMap.put("iPhone9,3", "iPhone 7");
        deviceMap.put("iPhone9,4", "iPhone 7 Plus");
        deviceMap.put("iPhone10,1", "iPhone 8");
        deviceMap.put("iPhone10,2", "iPhone 8 Plus");
        deviceMap.put("iPhone10,3", "iPhone X");
        deviceMap.put("iPhone10,4", "iPhone 8");
        deviceMap.put("iPhone10,5", "iPhone 8 Plus");
        deviceMap.put("iPhone10,6", "iPhone X");
        deviceMap.put("iPad1,1", "iPad");
        deviceMap.put("iPad2,1", "iPad 2");
        deviceMap.put("iPad2,2", "iPad 2");
        deviceMap.put("iPad2,3", "iPad 2");
        deviceMap.put("iPad2,4", "iPad 2");
        deviceMap.put("iPad2,5", "iPad mini");
        deviceMap.put("iPad2,6", "iPad mini");
        deviceMap.put("iPad2,7", "iPad mini");
        deviceMap.put("iPad3,1", "iPad");
        deviceMap.put("iPad3,2", "iPad");
        deviceMap.put("iPad3,3", "iPad");
        deviceMap.put("iPad3,4", "iPad");
        deviceMap.put("iPad3,5", "iPad");
        deviceMap.put("iPad3,6", "iPad");
        deviceMap.put("iPad4,1", "iPad Air");
        deviceMap.put("iPad4,2", "iPad Air");
        deviceMap.put("iPad4,3", "iPad Air");
        deviceMap.put("iPad4,4", "iPad mini 2");
        deviceMap.put("iPad4,5", "iPad mini 2");
        deviceMap.put("iPad4,6", "iPad mini 2");
        deviceMap.put("iPad4,7", "iPad mini 3");
        deviceMap.put("iPad4,8", "iPad mini 3");
        deviceMap.put("iPad4,9", "iPad mini 3");
        deviceMap.put("iPad5,1", "iPad mini 4");
        deviceMap.put("iPad5,2", "iPad mini 4");
        deviceMap.put("iPad5,3", "iPad Air 2");
        deviceMap.put("iPad5,4", "iPad Air 2");
        deviceMap.put("iPad6,3", "iPad Pro");
        deviceMap.put("iPad6,4", "iPad Pro");
        deviceMap.put("iPad6,7", "iPad Pro");
        deviceMap.put("iPad6,8", "iPad Pro");
        deviceMap.put("iPad6,11", "iPad");
        deviceMap.put("iPad6,12", "iPad");
        deviceMap.put("iPad7,3", "iPad Pro");
        deviceMap.put("iPad7,4", "iPad Pro");
        deviceMap.put("iPad7,5", "iPad Pro");
        deviceMap.put("iPad7,6", "iPad Pro");
        return deviceMap;
    }

    private static String getIOSDeviceModel(String productType) {
        return getIPhoneModelList().get(productType);
    }

    /**
     * Method to get Device model name
     *
     * @param deviceNameList : list of device name
     * @return : list of device model name
     */
    public static List<String> deviceModelName(List<String> deviceNameList) {
        List<String> deviceModelName = new ArrayList<>();
        for (String aDeviceNameList : deviceNameList) {
            deviceModelName.add(getDeviceModelName(aDeviceNameList));
        }
        return deviceModelName;
    }


    private static String getDeviceModelName(String deviceUDID) {
        String deviceModel = "";
        if (isWindows()) {
            String deviceId = deviceUDID.replace("\tdevice", "");
            deviceModel = getModelName(AdbCommandsFactory.getDeviceModelCommand(deviceId));
        } else {
            if (deviceUDID.length() >= 40) {
                String iosVersion = CommandLineExecutor.runProcess(
                        "ideviceinfo -u " + deviceUDID + " |grep ProductType").get(0);
                deviceModel = getIOSDeviceModel(iosVersion.substring(iosVersion.indexOf(" ")).trim());
            } else {
                deviceModel = CommandLineExecutor.runProcess(AdbCommandsFactory.getDeviceModelCommand(deviceUDID)).get(0);
            }
        }
        return deviceModel;
    }

    /**
     * Method to get Application Build Version
     *
     * @param deviceList : List of device name
     * @return : application build version
     */
    public static String getBuildVersion(List<String> deviceList, String packageName) {
        String buildVersion = "";
        for (String eachDevice : deviceList) {
            if (isWindows()) {
                List packageInfo = CommandLineExecutor.runProcess(AdbCommandsFactory.
                        getApplicationVersionCommand(eachDevice, TestExecutor.getConfigData().getPackageName()));
                for (Object eachInfo : packageInfo) {
                    if (eachInfo.toString().trim().contains("versionName")) {
                        buildVersion = eachInfo.toString().substring(eachInfo.toString().indexOf("=") + 1);
                        break;
                    }
                }
            } else {
                if (eachDevice.length() >= 40) {
                    String deviceId = eachDevice.trim();
                    List<String> build = CommandLineExecutor.runProcess(AdbCommandsFactory.getAppBuildVersionCommand(deviceId, packageName));
                    if (!buildVersion.isEmpty()) {
                        buildVersion = build.get(0).substring(build.indexOf("=" + 1), build.indexOf(","));
                    }
                } else {
                    List<String> buildNumber = CommandLineExecutor.runProcess(
                            " ideviceinstaller -u " + eachDevice + " -l -o xml |grep -A3 FacebookDisplayName");
                    buildVersion = buildNumber.get(3);
                    buildVersion = buildVersion.replaceAll("<string>", "").replaceAll("</string>", "").trim();
                }
            }
        }
        return buildVersion;
    }

    /**
     * Command to reboot device
     */
    public static void rebootDevice(String device) {
        if (isWindows()) {
            CommandLineExecutor.executeCommand(AdbCommandsFactory.getRebootCommand(device));
        } else {
            if (device.length() >= 40) {
                CommandLineExecutor.executeCommand("idevicediagnostics -u " + device + " restart");
            } else {
                CommandLineExecutor.executeCommand(AdbCommandsFactory.getRebootCommand(device));
            }
        }
    }

    /**
     * Method to clear Application Data
     */
    public static void clearApplicationCache(String deviceId, String packageName) {
        FrameworkLogger.logStep("Clearing Application Data");
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getClearApplicationData(deviceId, packageName));
    }





    /**
     * Method to install apk using .apk
     */
    public static void installApplication(String deviceId, File apkFilePath) throws IOException {
        CommandLineExecutor.executeCommand(AdbCommandsFactory.
                getInstallApplicationCommand(deviceId, apkFilePath.getCanonicalPath()));
    }

    /**
     * Method to uninstall application using package name
     */
    public static boolean uninstallApplication(String deviceId, String appPackageName) {
        List<String> list;
        boolean isUninstalled = false;
        list =  CommandLineExecutor.runProcess(AdbCommandsFactory.getUninstallCommand(deviceId, appPackageName));

        if(list.size()>0){
            String test = list.get(0);
            System.out.println("Package found on device " + test);
            isUninstalled = test.contains("Success");
        }
       return isUninstalled;
    }

    public static void installIOSApplication(String deviceId, String path) {
        String command = String.format("ideviceinstaller -u %s -i %s", deviceId, path);
        CommandLineExecutor.executeCommand(command);
    }

    public static void unInstallIOSApplication(String deviceId, String bundleID) {
        String command = String.format("ideviceinstaller -u %s -U %s", deviceId, bundleID);
        CommandLineExecutor.executeCommand(command);
    }

    public static void captureScreen(String fileName, String aDeviceName) {
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getCaptureScreenCommand(aDeviceName, "/sdcard/screen.png"));
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getPullCommand(aDeviceName, "/sdcard/screen.png ", fileName));
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getRemoveFileCommand(aDeviceName, "/sdcard/screen.png"));
    }

    public static void iosCaptureScreen(String fileName, String aDeviceName) {
        String path = System.getProperty("user.dir");
        Util.createDirectory(path + "/temp");
        CommandLineExecutor.runProcess("idevicescreenshot -u " + aDeviceName + " " + path + "/temp/temp.tiff");
        CommandLineExecutor.runProcess("sips -s format \"jpeg\" " + path + "/temp/temp.tiff --out " + fileName);
    }

    public static void placeCall(String deviceId, String phoneNumber) {
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getCallCommand(deviceId, phoneNumber));
    }

    public static void endCall(String deviceId) {
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getEndCallCommand(deviceId));
    }

    public static void acceptCall(String deviceId) {
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getAcceptCallCommand(deviceId));
    }

    public static void changeTimeFormat(String deviceId, String requiredTimeFormat) {
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getChangeTimeFormatCommand(deviceId, requiredTimeFormat));
    }

    public static List<String> getContactPackageName(String deviceId) {
        List<String> list;
        if (isWindows()) {
            list = CommandLineExecutor.runProcess(AdbCommandsFactory.getContactPackageCommand(deviceId));
        } else {
            list = CommandLineExecutor.runProcess(AdbCommandsFactory.getContactPackageCommand(deviceId));
        }
        return list;
    }

    public static boolean isAppInstalled(String deviceId, String packageName) {
        List<String> list;
        boolean isInstalled = false;
        if (isWindows()) {
            list = CommandLineExecutor.runProcess(AdbCommandsFactory.isAppPackageAvailable(deviceId,packageName));
        } else {
            list = CommandLineExecutor.runProcess(AdbCommandsFactory.isAppPackageAvailable(deviceId, packageName));
        }
        if(list.size()>0) {
            String packageInstalled = list.get(0).split(":")[1];
            String packageAvailable =  packageName;
            System.out.println("Package found on device " + packageInstalled);
            System.out.println("PackageName available "+packageAvailable);
            isInstalled = packageInstalled.equals(packageAvailable);
        }

        return isInstalled;
    }


    private static String getDeviceManufacturer(String deviceId) {
        return deviceId.length() >= 40 ? "Apple" :
                CommandLineExecutor.runProcess(AdbCommandsFactory.getDeviceManufacturerCommand(deviceId)).get(0);
    }


    public static void volumeUp(String deviceId) {
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getVolumeUpCommand(deviceId));
    }

    public static void volumeDown(String deviceId) {
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getVolumeDownCommand(deviceId));
    }

    public static void mute(String deviceId) {
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getMuteCommand(deviceId));
    }

    public static void wakeUp(String deviceId) {
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getWakeUpCommand(deviceId));
    }

    public static void lock(String deviceId) {
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getLockCommand(deviceId));
    }

    public static void unlock(String deviceId) {
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getUnlockCommand(deviceId));
    }


    public static void home(String deviceId) {
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getHomeCommand(deviceId));
    }



    public static void sleep(String deviceId) {
        if (isWindows()) {
            CommandLineExecutor.executeCommand(AdbCommandsFactory.getSleepCommand(deviceId));
        } else {
            if (InitializerScript.isAndroid()) {
                CommandLineExecutor.executeCommand(AdbCommandsFactory.getSleepCommand(deviceId));
            } else {
                CommandLineExecutor.executeCommand("idevicediagnostics -u " + deviceId + " sleep");
            }
        }
    }

    public static List<String> getDeviceManufacture(List<String> deviceID) {
        List<String> list = new ArrayList<>();
        for (String dev : deviceID) {
            if (dev.length() < 40) {
                String name = CommandLineExecutor.runProcess(AdbCommandsFactory.getDeviceManufacturerCommand(dev)).get(0);
                list.add(name);
            }
        }
        return list;
    }

    public static List<String> getDeviceDisplayName(List<String> deviceID, List<String> deviceModelList, List<String> deviceManufactureList) {
        List<String> list = new ArrayList<>();
        for (String dev : deviceID) {
            if (!isWindows()) {
                if (dev.length() >= 40) {
                    String name = CommandLineExecutor.runProcess("ideviceName -u " + dev).get(0);
                    list.add(name);
                }
            } else {
                int index = deviceID.indexOf(dev);
                String name = getDeviceName(deviceManufactureList.get(index), deviceModelList.get(index));
                list.add(name);
            }
        }
        return list;
    }

    private static String getDeviceDisplayName(String deviceID) {
        String name = "";
        if (!isWindows()) {
            if (deviceID.length() >= 40) {
                name = CommandLineExecutor.runProcess("ideviceName -u " + deviceID).get(0);
            }
        } else {
            name = getDeviceName(getDeviceManufacturer(deviceID), getDeviceModelName(deviceID));
        }
        return name;
    }

    public static void closeAllRecentApp(String deviceId) throws IOException {
        FrameworkLogger.logStep("Close All Recent App");
        int counter = 0;
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getRecentAppCommand(deviceId));
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getSelectRecentAppCommand(deviceId));
        CommandLineExecutor.executeCommand(AdbCommandsFactory.getCloseRecentAppCommand(deviceId));

        //check if still recent app is open.
        String currentActivity = "";
        List<String> currentActivityList = Util.runProcessIOUtils("mCurrentFocus",
                AdbCommandsFactory.getCurrentActivityCommand(deviceId));

        if (currentActivityList != null) {
            currentActivity = currentActivityList.get(0);
        } else {
            LOGGER.log(Level.INFO, "No Activity Found");
        }
        while ((currentActivity.contains("RecentsActivity") || currentActivity.contains("recents"))
                && counter <= 10) {
            CommandLineExecutor.executeCommand(AdbCommandsFactory.getSelectRecentAppCommand(deviceId));
            CommandLineExecutor.executeCommand(AdbCommandsFactory.getCloseRecentAppCommand(deviceId));

            currentActivityList = Util.runProcessIOUtils("mCurrentFocus",
                    AdbCommandsFactory.getCurrentActivityCommand(deviceId));
            if (currentActivityList != null) {
                currentActivity = currentActivityList.get(0);
            } else {
                LOGGER.log(Level.INFO, "No Activity Found");
            }
            counter++;
        }
    }

    private static void killADBServer() {
        CommandLineExecutor.runProcess(AdbCommandsFactory.getKillServerCommand());
    }

    private static void startADBServer() {
        CommandLineExecutor.runProcess(AdbCommandsFactory.getStartServerCommand());
    }


    /**
     * Method to get results of windows commands in a list
     */
    public static List<String> getDeviceName(String command) {
        List deviceList = CommandLineExecutor.runProcess(command);
        List<String> line = new ArrayList<>();
        if (!deviceList.contains("'adb' is not recognized as an internal or external command,")) {
            for (Object eachDevice : deviceList) {
                eachDevice = eachDevice.toString().substring(0, eachDevice.toString().indexOf("\t"));
                line.add(eachDevice.toString());
            }
        }
        return line;
    }

    private static String getModelName(String command) {
        LOGGER.log(Level.INFO, "Command to Get models Name::----" + command + "\n");
        String theString = CommandLineExecutor.runProcess(command).get(0);
        LOGGER.log(Level.INFO, " :: stream value model " + theString);
        return theString;
    }

    public static String getDeviceName(String manufacturer, String model) {
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private static void waitForAdb() {
        long startTime = System.currentTimeMillis();
        while (CommandLineExecutor.runProcess(AdbCommandsFactory.getServerStatusCommand()).contains("device")) {
            try {
                Thread.sleep(1000);
                if ((System.currentTimeMillis() - startTime) > 10000L) {
                    break;
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, "adb not started in given time", e);
            }
        }
    }

    public static class DeviceNotFoundException extends Exception {

        public DeviceNotFoundException(String message) {
            super(message);
        }

    }
}
