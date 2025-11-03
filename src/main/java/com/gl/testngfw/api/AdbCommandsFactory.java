package com.gl.testngfw.api;

/**
 * <p>
 * AdbCommandsFactory specifies a factory for creation of adb -s %s commands
 * </p>
 */
public class AdbCommandsFactory {
    private AdbCommandsFactory() {
    }

    public static String getDevicesCommand() {
        if (System.getProperty("os.name").contains("Windows")) {
            return "adb devices | findstr /E device";
        } else {
            return "adb devices | grep -w device";
        }
    }

    public static String getKillServerCommand() {
        return "adb kill-server";
    }

    public static String getStartServerCommand() {
        return "adb start-server";
    }

    public static String getServerStatusCommand() {
        return "adb get-status";
    }

    public static String getBatteryCommand() {
        return "adb -s %s shell dumpsys battery | grep 'level'";
    }

    public static String getMemoryCommand(String device, final String packageName) {
        return String.format("adb -s %s shell dumpsys meminfo '%s'", device, packageName);
    }

    public static String getCPUInfoCommand(String device, final String packageName) {
        return String.format("adb -s %s shell dumpsys cpuinfo | grep '%s'", device, packageName);
    }

    static String getCaptureScreenCommand(String device, final String tabletFilePath) {
        return String.format("adb -s %s shell screencap -p \"%s\"", device, tabletFilePath);
    }

    static String getPullCommand(String device, final String tabletFilePath, final String testStationPath) {
        return String.format("adb -s %s pull \"%s\" \"%s\"", device, tabletFilePath, testStationPath);
    }

    public static String getPushCommand(String device, final String filePath, final String tabletPath) {
        return String.format("adb -s %s push \"%s\" \"%s\"", device, filePath, tabletPath);
    }

    public static String getRemoveFileCommand(String device, final String tabletFilePath) {
        return String.format("adb -s %s shell rm \"%s\"", device, tabletFilePath);
    }

    static String getRebootCommand(String device) {
        return String.format("adb -s %s reboot", device);
    }

    public static String getCloseKeyboardCommand(String device) {
        return String.format("adb -s %s shell input tap 200 200", device);
    }

    public static String getClickCommand(String device, int x, int y) {
        return String.format("adb -s %s shell input tap %d %d", device, x, y);
    }

    public static String getSwipeCommand(String device, int xStart, int yStart, int xEnd, int yEnd) {
        return String.format("adb -s %s shell input swipe %d %d %d %d", device, xStart, yStart, xEnd, yEnd);
    }

    public static String getSwipeCommand(String device, int xStart, int yStart, int xEnd, int yEnd, int duration) {
        return String.format("adb -s %s shell input swipe %d %d %d %d %d", device, xStart, yStart, xEnd, yEnd, duration);
    }

    static String getLockCommand(String device) {
        return String.format("adb -s %s shell input keyevent 26", device);
    }

    static String getUnlockCommand(String device) {
        return String.format("adb -s %s shell input keyevent 82", device);
    }

    static String getHomeCommand(String device) {
        return String.format("adb -s %s shell input keyevent 3", device);
    }

    static String getCallCommand(String device, String phoneNumber) {
        return String.format("adb -s %s shell am start -a android.intent.action.CALL -d tel: %s", device, phoneNumber);
    }

    static String getAcceptCallCommand(String device) {
        return String.format("adb -s %s shell input keyevent 5", device);
    }

    static String getEndCallCommand(String device) {
        return String.format("adb -s %s shell input keyevent 6", device);
    }

    static String getVolumeUpCommand(String device) {
        return String.format("adb -s %s shell input keyevent 24", device);
    }

    static String getVolumeDownCommand(String device) {
        return String.format("adb -s %s shell input keyevent 25", device);
    }

    static String getMuteCommand(String device) {
        return String.format("adb -s %s shell input keyevent 164", device);
    }

    static String getSleepCommand(String device) {
        return String.format("adb -s %s shell input keyevent 223", device);
    }

    static String getWakeUpCommand(String device) {
        return String.format("adb -s %s shell input keyevent 224", device);
    }

    static String getChangeTimeFormatCommand(String device, String timeFormat) {
        return String.format("adb -s %s shell content insert --uri content://settings/system  --bind name:s:time_12_24 " +
                "--bind value:s: %s", device, timeFormat);
    }

    static String getContactPackageCommand(String device) {
        if (System.getProperty("os.name").contains("Windows")) {
            return String.format("adb -s %s  shell pm list packages -f | findstr android.contact", device);
        } else {
            return String.format("adb -s %s  shell pm list packages -f | grep android.contact", device);
        }
    }

    public static String getInstallApplicationCommand(String device, String pathToAppFile) {
        return String.format("adb -s %s install -r %s", device, pathToAppFile);
    }

    public static String getUninstallCommand(String device, String packageName) {
        return String.format("adb -s %s uninstall %s", device, packageName);
    }

    public static String isAppPackageAvailable(String device, String packageName) {
        return String.format("adb -s %s shell pm list packages | grep %s", device, packageName);
    }


    public static String getStartCommand(String device, String activityName) {
        return String.format("adb -s %s shell am start -n %s", device, activityName);
    }

    public static String getStopCommand(String device, String packageName) {
        return String.format("adb -s %s shell am force-stop %s", device, packageName);
    }

    public static String getIsApplicationLaunchedCommand(String device, String packageName) {
        return String.format("adb -s %s shell ps | grep %s", device, packageName);
    }

    static String getClearApplicationData(String device, String packageName) {
        return String.format("adb -s %s shell pm clear %s", device, packageName);
    }

    public static String getDeviceInfoCommand(String device) {
        return String.format("adb -s %s shell getprop | grep -E \"ro.product.manufacturer\\|ro.product.model\\| ro.build.version.release\\|ro.product.name\\|ro.serialno\"", device);
    }

    public static String getLogcatCommand(String device, String path) {
        return String.format("adb -s %s shell logcat -d -v time -d > %s", device, path);
    }

    public static String getDateCommand(String device) {
        return String.format("adb -s %s shell date", device);
    }

    public static String getDeleteAllContactsCommand(String device) {
        return String.format("adb -s %s shell content delete --uri content://com.android.contacts/raw_contacts/", device);
    }

    public static String getDeleteSpecificContactsCommand(String device, String contactId) {
        return String.format("adb -s %s shell content delete --uri content://com.android.contacts/raw_contacts/ " +
                "--where \"_id='%s'\"", device, contactId);
    }

    public static String getDeleteSocialContactsCommand(String device, String accountType) {
        return String.format("adb -s %s shell content delete --uri content://com.android.contacts/raw_contacts/ " +
                "--where \"account_type='%s'\"", device, accountType);
    }

    public static String getDeleteOnlyNativeContactsCommand(String device) {
        return String.format("adb -s %s shell content delete --uri content://com.android.contacts/raw_contacts/ " +
                "--where \"account_type!='com.google'\"", device);
    }

    public static String getVerifyDeletedContactCommand(String device, String contactId) {
        return String.format("adb -s %s shell content query --uri content://com.android.contacts/raw_contacts/  " +
                "--projection deleted --where \"_id='%s'\"", device, contactId);
    }

    public static String getImportContactsCommand(String device, String remotePath, String vcfFileName, String contactPackageName) {
        return String.format("adb -s %s shell am start -t \"text/x-vcard\" -d \"file://%s/%s\" " +
                "-a android.intent.action.VIEW %s", device, remotePath, vcfFileName, contactPackageName);
    }

    public static String getIsFileExitsCommand(String device, final String filePath) {
        return String.format("adb -s %s shell ls %s", device, filePath);
    }

    static String getDeviceVersionCommand(String device) {
        return String.format("adb -s %s shell getprop ro.build.version.release", device);
    }

    static String getAppBuildVersionCommand(String device, String packageName) {
        return String.format("adb -s %s shell dumpsys package %s | grep versionName", device, packageName);
    }

    static String getDeviceModelCommand(String device) {
        return String.format("adb -s %s shell getprop ro.product.model", device);
    }

    static String getDeviceManufacturerCommand(String device) {
        return String.format("adb -s %s shell getprop ro.product.manufacturer", device);
    }

    static String getApplicationVersionCommand(String device, String packageName) {
        return String.format("adb -s %s shell dumpsys package %s", device, packageName);
    }

    private static String getContactDataTableCommand(String device) {
        return String.format("adb -s %s shell content query --uri content://com.android.contacts/data/ --projection ",
                device);
    }

    public static String getAllContactIdsCommand(String device) {
        return getContactDataTableCommand(device) + " raw_contact_id --where \"mimetype='vnd.android.cursor.item/name'\"";
    }

    public static String getContactIdCommand(String device, String projection, String firstName, String mimeType) {
        return String.format(getContactDataTableCommand(device) + " %s  --where \"data2='%s' " +
                "and mimetype='%s'\"", projection, firstName, mimeType);
    }

    public static String getContactCountCommand(String device) {
        return getContactDataTableCommand(device) + " raw_contact_id --where \"mimetype_id='7'\"";
    }

    public static String getUpdateContactCommand(String device, String newConName, String id, String mimetype) {
        return String.format("adb -s %s shell content update --uri content://com.android.contacts/data/ " +
                "--bind data2:s:%s --where \"raw_contact_id='%s' and mimetype='%s'\"", device, newConName, id, mimetype);
    }

    public static String getUpdateContactPhoneCommand(String device, String newPhone, String id, String mimetype) {
        return String.format("adb -s %s shell content update --uri content://com.android.contacts/data/ " +
                "--bind data1:s:%s --where \"raw_contact_id='%s' and mimetype='%s'\"", device, newPhone, id, mimetype);
    }

    public static String getContactCommand1(String device, String projection, String contactId, String mimeType) {
        return String.format(getContactDataTableCommand(device) + " %s  --where \"raw_contact_id='%s' " +
                "and mimetype='%s'\"", projection, contactId, mimeType);
    }

    public static String getContactCommand2(String device, String projection, String contactId, String mimeType, String data2Column) {
        return String.format(getContactDataTableCommand(device) + " %s  --where \"raw_contact_id='%s' " +
                "and mimetype='%s' and data2='%s'\"", projection, contactId, mimeType, data2Column);
    }

    public static String getContactCommand3(String device, String projection, String contactId, String mimeType, String data5Column) {
        return String.format(getContactDataTableCommand(device) + " %s  --where \"raw_contact_id='%s' " +
                "and mimetype='%s' and data5='%s'\"", projection, contactId, mimeType, data5Column);
    }

    public static String getContactCommand4(String device, String projection, String contactId, String mimeType, String data1Column) {
        return String.format(getContactDataTableCommand(device) + " %s  --where \"raw_contact_id='%s' " +
                "and mimetype='%s' and data1='%s'\"", projection, contactId, mimeType, data1Column);
    }

    public static String getContactNameCommand(String device, String id) {
        return String.format(getContactDataTableCommand(device) + " data1 --where \"raw_contact_id='%s' " +
                "and mimetype_id='7'\"", id);
    }

    public static String getContactIdCommand(String device) {
        return getContactDataTableCommand(device) + " raw_contact_id";
    }

    public static String getContactVersionCommand(String device, String contactId) {
        return String.format("adb -s %s shell content query --uri content://com.android.contacts/raw_contacts/ " +
                "--projection version --where \"_id='%s'\"", device, contactId);
    }

    public static String getRecentAppCommand(String device) {
        return String.format("adb -s %s shell input keyevent KEYCODE_APP_SWITCH", device);
    }

    public static String getSelectRecentAppCommand(String device) {
        return String.format("adb -s %s shell input keyevent KEYCODE_DPAD_DOWN", device);
    }

    public static String getCloseRecentAppCommand(String device) {
        return String.format("adb -s %s shell input keyevent DEL", device);
    }

    public static String getCurrentActivityCommand(String device) {
        return String.format("adb -s %s shell \"dumpsys window windows | grep -E 'mCurrentFocus'\"", device);
    }

    public static String getKeyboardVisibleCommand(String device) {
        return String.format("adb -s %s shell \"dumpsys window InputMethod | grep -E 'mHasSurface'\"", device);
    }

    public static String getBatteryLowCommand(String device, String batterPercentage) {
        return String.format("adb -s %s shell dumpsys battery set level %s", device, batterPercentage);
    }

    public static String getClearADBLogsCommand(String device) {
        return String.format("adb -s %s logcat -c", device);
    }

    public static String getADBLogsMaxBufferSizeCommand(String device) {
        return String.format("adb -s %s logcat -G 16M", device);
    }

    public static String getCaptureADBLogsCommand(String device, String packageName, String filterName) {
        return String.format("adb -s %s logcat -d %s |findstr %s", device, packageName, filterName);
    }

    public static String getCaptureADBLogsCommand(String device, String filePath) {
        return String.format("adb -s %s logcat -d > %s", device, filePath);
    }

    public static String getRecordCommand(String deviceName, String videoName) {
        return String.format("adb -s %s shell screenrecord /sdcard/%s", deviceName, videoName);
    }

    public static String getFileRemoveCommand(String deviceName, String videoName) {
        return String.format("adb -s %s shell rm -f /sdcard/%s", deviceName, videoName);
    }

    public static String getNetworkStatus(String deviceName) {
        return String.format("adb -s %s shell \"dumpsys connectivity | grep NetworkAgentInfo'\"", deviceName);
    }

    public static String getBluetoothStatus(String deviceName) {
        return String.format("adb -s %s shell settings get global bluetooth_on", deviceName);
    }
}
