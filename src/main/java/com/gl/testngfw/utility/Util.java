package com.gl.testngfw.utility;

import com.gl.testngfw.api.RestAPIExtension;
import com.gl.testngfw.enums.Browser;
import com.gl.testngfw.enums.ExecutorType;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.execution.HeaderData;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.logging.ServerNotStarted;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.lang3.EnumUtils;
import org.json.JSONArray;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Utility class
 */
public class Util {
    private static final Logger LOGGER = Logger.getLogger(Util.class.getName());
    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String[] WIN_RUNTIME = {"cmd.exe", "/c"};
    private static final String[] DEFAULT_SUPPORTED_LOCALES = {
            "ar", "bg", "cs", "da", "de", "el", "es", "et", "fi", "fr", "iw", "hr", "hu", "in", "is", "it", "ja", "ko", "lt", "lv", "ms", "nl", "no", "pl", "pt-BR", "pt-PT", "ru", "ro", "sk", "sl", "sr", "sq", "sv", "tr", "uk", "vi", "zh-CN", "zh-TW", "he", "ro", "ru", "pl", "el", "ja", "it", "is", "en"
    };
    private static String response;
    private static String startTime;

    private Util() {
    }

    /**
     * Method to create random string
     *
     * @param length : Length of string to generated
     * @return : random string
     */
    public static String randomString(int length) {
        char[] characterSet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
                'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1',
                '2', '3', '4', '5', '6', '7', '8', '9'};

        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        return new String(result);

    }

    /**
     * Method to convert string to hash map
     *
     * @param data :
     * @return : Map
     */
    public static Map<String, String> stringToHashMap(String data) {
        HashMap<String, String> mapRegion = new HashMap<>();
        String string = data;
        string = string.replace('{', ' ');
        string = string.replaceAll("}", "").trim();

        String[] mapArray = string.split(", ");
        for (String aMapArray : mapArray) {
            String key;
            String value;
            int indexOfEqual = aMapArray.indexOf("=");
            value = aMapArray.substring(indexOfEqual + 1);
            key = aMapArray.substring(0, indexOfEqual);
            mapRegion.put(key, value);
        }
        return mapRegion;
    }

    private static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static String getResponse() {
        return response;
    }

    public static void setResponse(String response) {
        Util.response = response;
    }

    /*
     * Getting list of test class files.
     */
    public static List getTestClassList(String directory, String packageName, List<String> suiteClass) {
        List<String> fileNames = FileUtil.getClassFiles(directory, packageName);
        return fileNames.stream().filter(suiteClass::contains).collect(Collectors.toList());
    }

    /**
     * Method to get results of windows commands in a list
     */
    public static List<String> runProcess(String... command) {
        LOGGER.log(Level.INFO, "command to run: " + command[0] + " ");
        LOGGER.log(Level.INFO, "\n");
        long startTime = System.currentTimeMillis();
        String[] allCommand;
        List<String> line = new ArrayList<>();
        try {
            if (isWindows()) {
                allCommand = concat(WIN_RUNTIME, command);
            } else {
                allCommand = concat(new String[]{"/bin/sh", "-c"}, command);
            }
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(allCommand);
            process.waitFor();
            InputStream inputStream = process.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

            String temp;
            if (in.ready()) {
                while ((temp = in.readLine()) != null) {
                    if ((System.currentTimeMillis() - startTime) > 180000L) {
                        FrameworkLogger.logFail("Command TimeOut : " + command[0]);
                        break;
                    }
                    line.add(temp);
                }
            }
            return line;
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "", e);
            return line;
        }
    }

    public static String getModelName(String... command) {
        LOGGER.log(Level.INFO, "Command to Get models Name::----" + command[0] + "\n");
        String[] allCommand;
        try {
            if (isWindows()) {
                allCommand = concat(WIN_RUNTIME, command);
            } else {
                allCommand = concat(new String[]{"/bin/sh", "-c"}, command);
            }

            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(allCommand);
            Thread.sleep(2000);
            String theString = IOUtils.toString(process.getInputStream(), Charset.defaultCharset());
            LOGGER.log(Level.INFO, " :: stream value model " + theString);

            return theString.substring(0, theString.indexOf("\r"));
        } catch (Exception e) {
            FrameworkLogger.logError(e);
            return null;
        }
    }

    /**
     * execute command get result matching search parameter
     *
     * @param data    search parameter
     * @param command execution command
     * @return list of strings
     */
    public static List<String> runProcessIOUtils(String data, String command) {
        List<String> newList = new ArrayList<>();
        List<String> list = CommandLineExecutor.runProcess(command);
        for (String eachItem : list) {
            if (eachItem.contains(data)) {
                newList.add(eachItem);
            }
        }
        return newList;
    }

    public static InetAddress getLocalHost() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    public static synchronized void createDirectory(String directoryName) {
        File directory = new File(String.valueOf(directoryName));
        if (!directory.exists() && !directory.mkdirs()) {
            LOGGER.log(Level.INFO, directoryName + " directory not created");
        }
    }

    public synchronized static void deleteDirectory(String directoryName) {
        File directory = new File(String.valueOf(directoryName));
        if (directory.exists() && !directory.delete()) {
            LOGGER.log(Level.INFO, directoryName + " directory not delete");
        }
    }

    public static int getIndex(Map<String, Integer> map, String str) {
        int i = 0;
        for (String eachKey : map.keySet()) {
            if (eachKey.equalsIgnoreCase(str)) {
                break;
            }
            i++;
        }
        return i;
    }

    public static void setStartTime() {
        startTime = DateTimeUtil.getCurrentTime("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    public static String getStartTime() {
        return startTime;
    }

    public static String getEndTime() {
        return DateTimeUtil.getCurrentTime("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    /**
     * Method to execute command
     */
    public static synchronized void executeCommand(String... command) {
        LOGGER.log(Level.INFO, "execute command : " + command[0] + " ");
        LOGGER.log(Level.INFO, "\n");
        String[] allCommand;
        try {
            if (isWindows()) {
                allCommand = concat(WIN_RUNTIME, command);
            } else {
                allCommand = concat(new String[]{"bash", "-c"}, command);
            }
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(allCommand);
        } catch (Exception e) {
            FrameworkLogger.logError(e);
        }
    }

    /**
     * Method to execute command
     */
    public static synchronized void executeCommand(int timeout, String... command) {
        LOGGER.log(Level.INFO, "execute command : " + command[0] + " ");
        LOGGER.log(Level.INFO, "\n");
        String[] allCommand;
        try {
            if (isWindows()) {
                allCommand = concat(WIN_RUNTIME, command);
            } else {
                allCommand = concat(new String[]{"bash", "-c"}, command);
            }
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(allCommand);
            if (!process.waitFor(timeout, TimeUnit.MINUTES)) {
                process.destroyForcibly();
            }
        } catch (Exception e) {
            FrameworkLogger.logError(e);
        }
    }

    public static int getProxyPort(String device) {
        Map<String, String> proxies = FileUtil.getPropertyMap("Project.properties");
        for (String eachKey : proxies.keySet()) {
            if (proxies.get(eachKey).equalsIgnoreCase(device)) {
                return Integer.parseInt(eachKey.split("_")[1]);
            }
        }
        return 0;
    }

    /**
     * Method to get 64bit encoded string of file
     *
     * @param path file path
     * @return encoded string
     */
    public static String getEncoded(String path) {
        try {
            if (!path.isEmpty()) {
                byte[] bytes = null;
                bytes = Files.readAllBytes(Paths.get(path));
                return Base64.getEncoder().encodeToString(bytes);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return "";
    }

    /**
     * Method to Decode Base 64 data
     *
     * @param enCodedVal: encoded string
     */
    public static void getDecoded(String enCodedVal, File file) {
        if (enCodedVal != null && !enCodedVal.isEmpty()) {
            try {
                Files.write(file.toPath(), Base64.getDecoder().decode(enCodedVal));
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "", e);
            }
        }
    }

    /*
     * Generates Random ports
     * Used during starting appium server
     */
    public static String getAvailablePort() {
        int port = 0;
        try {
            ServerSocket socket = new ServerSocket(0);
            socket.setReuseAddress(true);
            port = socket.getLocalPort();
            socket.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
        return String.valueOf(port);
    }

    public static List<File> getLatestExecutionReports(String directory, String[] extensions) {
        List<File> list = new ArrayList<>();
        File recentDirectory = new File(getRecentExecutionDirectory(directory));
        if (recentDirectory.exists()) {
            list = (List<File>) FileUtils.listFiles(recentDirectory, extensions, true);
        }
        return list;
    }

    public static JSONArray getExecutionResultList() throws IOException {
        List<File> fileList = Util.getLatestExecutionReports(System.getProperty("user.dir") + File.separator + "Reports", new String[]{"json"});
        JSONArray finalArray = new JSONArray();
        RestAPIExtension restAPIHelper = new RestAPIExtension(false);
        for (File file : fileList) {
            String jsonString = restAPIHelper.getJsonStringFromFile(file.getAbsolutePath());
            JSONArray jsonArray = new JSONArray(jsonString);
            for (Object obj : jsonArray) {
                finalArray.put(obj);
            }
        }
        return finalArray;
    }

    public static String getRecentExecutionDirectory(String directory) {
        File dir = new File(directory);
        File[] files = dir.listFiles();
        String filePath = "";
        if (files != null) {
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            for (File file : files) {
                if (file.isDirectory()) {
                    filePath = file.getAbsolutePath();
                    break;
                }
            }
        }
        return filePath;
    }

    public static String getWebDriverBinaryPath(Browser browser) {
        String driverPath = "";
        String browserName;
        String rootDirectory = USER_DIR + "/" + "webDriverBinaries";
        switch (browser) {
            case CHROME:
                browserName = isWindows() ? "chromedriver.exe" : "chromedriver";
                WebDriverManager.chromedriver().setup();
                driverPath = "-Dwebdriver.chrome.driver=" + WebDriverManager.chromedriver().getBrowserPath().toString(); //FileUtil.getFilePath(rootDirectory, browserName);
                break;
            case FIREFOX:
                browserName = isWindows() ? "geckodriver.exe" : "geckodriver";
                WebDriverManager.firefoxdriver().setup();
                driverPath = "-Dwebdriver.gecko.driver=" + WebDriverManager.firefoxdriver().getBrowserPath().toString();//FileUtil.getFilePath(rootDirectory, browserName);
                break;
            case SAFARI:
                break;
            case IE:
                browserName = "IEDriverServer.exe";
                WebDriverManager.iedriver().setup();
                driverPath = "-Dwebdriver.ie.driver=" + WebDriverManager.iedriver().getBrowserPath().toString();//FileUtil.getFilePath(rootDirectory, browserName);
                break;
            case EDGE:
                browserName = "MicrosoftWebDriver.exe";
                WebDriverManager.edgedriver().setup();
                driverPath = "-Dwebdriver.edge.driver=" + WebDriverManager.edgedriver().getBrowserPath().toString();//FileUtil.getFilePath(rootDirectory, browserName);
                break;
            default:
                FrameworkLogger.logWarning("Invalid Browser Type " + browser);
                break;
        }
        return driverPath;
    }

    public static String getWebBinaryPath(Browser browser) {
        String rootDirectory = USER_DIR + "/" + "webDriverBinaries";
        String driverPath;

        switch (browser) {
            case CHROME:
                driverPath = isWindows() ? "chromedriver.exe" : "chromedriver";
                System.out.println("driverPath:: "+driverPath);
                return FileUtil.getFilePath(rootDirectory, driverPath);
            case FIREFOX:
                driverPath = isWindows() ? "geckodriver.exe" : "geckodriver";
                return FileUtil.getFilePath(rootDirectory, driverPath);
            case IE:
                return FileUtil.getFilePath(rootDirectory, "IEDriverServer.exe");
            case EDGE:
                return FileUtil.getFilePath(rootDirectory, "MicrosoftWebDriver.exe");
            default:
                LOGGER.log(Level.INFO, "Invalid Browser Type " + browser);
                break;
        }
        return "Path Not Set";
    }

    public static Browser getBrowserName(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                return Browser.CHROME;
            case "firefox":
                return Browser.FIREFOX;
            case "ie":
                return Browser.IE;
            case "edge":
                return Browser.EDGE;
            case "safari":
                return Browser.SAFARI;
            default:
                LOGGER.log(Level.WARNING, "Invalid browserType" + browser);
                return Browser.CHROME;
        }

       /* if (browser.toLowerCase().contains("chrome")) {
            browserName = Browser.CHROME;
        } else if (browser.toLowerCase().contains("firefox")) {
            browserName = Browser.FIREFOX;
        } else if (browser.toLowerCase().contains("ie") ||
                browser.toLowerCase().contains("internet")) {
            browserName = Browser.IE;
        } else if (browser.toLowerCase().contains("edge")) {
            browserName = Browser.EDGE;
        } else if (browser.toLowerCase().contains("safari")) {
            browserName = Browser.SAFARI;
        }*/
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }

    /**
     * Method to wait till server is launched
     */
    public static void waitTillAllServerIsLaunched(String port, Long timeout) {
        long startTime = System.currentTimeMillis();
        String command;
        try {
            while (true) {
                List<String> list;
                Thread.sleep(3000L);
                if (System.getProperty("os.name").contains("Windows")) {
                    command = "netstat -anp tcp | findstr " + port;
                    list = CommandLineExecutor.runProcess(command);
                } else {
                    command = "netstat -anp tcp | grep " + port;
                    list = CommandLineExecutor.runProcess(command);
                }

                if (list != null && !list.isEmpty() && (list.get(0).contains("LISTENING") || list.get(0).contains("LISTEN"))) {
                    LOGGER.log(Level.INFO, "Server wait Time" + (System.currentTimeMillis() - startTime));
                    break;
                }
                if ((System.currentTimeMillis() - startTime) > timeout) {
                    throw new ServerNotStarted("Server Not Started on Given Time");
                }
            }
        } catch (ServerNotStarted | InterruptedException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    public static String downloadJar(String serverJarURL, String version) throws IOException {
        File serverJar = new File(String.format("src/main/resources/selenium-server-standalone-3.141%s.jar", version));
        if (!serverJar.exists()) {
            URL website = new URL(serverJarURL);
            FileUtils.copyURLToFile(website, serverJar);
        }
        return serverJar.getAbsolutePath();
    }

    public static List<String> getSeleniumServices() {
        String command;
        List<String> list;
        List<String> pidList = new ArrayList<>();
        if (System.getProperty("os.name").contains("Windows")) {
            command = "jps -l | findstr selenium";
            list = CommandLineExecutor.runProcess(command);
        } else {
            command = "jps -l | grep selenium";
            list = CommandLineExecutor.runProcess(command);
        }
        for (String line : list) {
            String pid = line.substring(0, line.indexOf(" ")).trim();
            if (pid.matches("[0-9]+")) {
                pidList.add(pid);
            }
        }
        return pidList;
    }

    public static void clearSeleniumProcess() {
        for (String pid : getSeleniumServices()) {
            try {
                executeCommand(String.format("taskkill /pid %s /F", pid));
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, e.getMessage());
            }

        }
    }

    /**
     * gets ipAddress
     *
     * @return ipAddress
     */
    public static String getIPAddress() {
        String ip = "";
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface iface = interfaces.nextElement();
                    if (iface.isLoopback() || !iface.isUp())
                        continue;

                    Enumeration<InetAddress> addresses = iface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress addr = addresses.nextElement();
                        if (!addr.getHostAddress().contains(":")) {
                            ip = addr.getHostAddress();
                            LOGGER.log(Level.INFO, iface.getDisplayName() + " " + ip);
                        }
                    }
                }
            } else {
                List<InterfaceAddress> ipList = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getInterfaceAddresses();
                for (InterfaceAddress address : ipList) {
                    if (address.getBroadcast() != null) {
                        ip = address.getAddress().getHostAddress();
                    }
                }
            }
        } catch (UnknownHostException | SocketException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
        return ip;
    }

    public static String getHostName() {
        InetAddress iAddress = null;
        try {
            iAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
        return iAddress != null ? iAddress.getHostName() : null;
    }

    public static Platforms getPlatform(String platform) {
        switch (platform.toUpperCase()) {
            case "MOBILE":
                return Platforms.MOBILE;
            case "WEB":
            case "CHROME":
            case "FIREFOX":
            case "IE":
            case "MICROSOFT_EDGE":
            case "SAFARI":
            case "EDGE":
            case "INTERNET_EXPLORER":
                return Platforms.WEB;
            case "API":
                return Platforms.API;
            case "CONTRACT":
                return Platforms.CONTRACT;
            case "PERFORMANCE":
                return Platforms.PERFORMANCE;
            case "STB":
                return Platforms.STB;
            case "ROKU":
                return Platforms.ROKU;
            case "DESKTOP":
                return Platforms.DESKTOP;
            default:
                if (isWebExecutionPresent(platform)) {
                    return Platforms.WEB;
                } else if (TestExecutor.getDeviceInfoMap().keySet().contains(platform)) {
                    return Platforms.MOBILE;
                }
                throw new RuntimeException(platform + "Platform Not Supported");
        }
    }

    public static ExecutorType getExecutorType(Method method, HeaderData headerData) {
        ExecutorType type = null;
        if (method.isAnnotationPresent(Test.class)) {
            type = ExecutorType.JUNIT;
        } else if (headerData.executableFor()[0].equals(Platforms.PERFORMANCE)) {
            type = ExecutorType.JMETER;
        } else if (headerData.executableFor()[0].equals(Platforms.STB)) {
            type = ExecutorType.STORMTEST;
        } else if (headerData.executableFor()[0].equals(Platforms.PERFORMANCE)) {
            type = ExecutorType.JMETER;
        } else if (method.isAnnotationPresent(org.testng.annotations.Test.class)) {
            type = ExecutorType.TESTNG;
        }
        return type;
    }

    private static boolean isWebExecutionPresent(String key) {
        return EnumUtils.isValidEnum(Browser.class, key.toUpperCase());
    }

    public void setLanguage() {
        Runtime runtime = Runtime.getRuntime();
        for (String language : DEFAULT_SUPPORTED_LOCALES) {
            String path = "C:/Project/Project/Medtronic/DBSDemoApp/ScreenShotsAfterICR2/Updated_screenshots/" + language + "/widget";
            Util.createDirectory(path);
            try {
                runtime.exec("adb shell pm grant net.sanapeli.adbchangelanguage android.permission.CHANGE_CONFIGURATION");

                runtime.exec("adb shell am start -n net.sanapeli.adbchangelanguage/.AdbChangeLanguage -e language " + language);
                Thread.sleep(10000);
                runtime.exec("adb shell screencap -p /sdcard/screencap.png");
                Thread.sleep(4000);
                runtime.exec("adb pull /sdcard/screencap.png " + path + "/widget.png");
                Thread.sleep(4000);
            } catch (IOException | InterruptedException e) {
                LOGGER.log(Level.WARNING, "", e);
            }
        }
    }

    /**
     * Zip code
     *
     * @param zipFileName
     */
    public static void zipFiles(String zipFileName) {
        try {
            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (File aFile : getReportList()) {
                zos.putNextEntry(new ZipEntry(aFile.getName()));

                byte[] bytes = Files.readAllBytes(Paths.get(aFile.getCanonicalPath()));
                zos.write(bytes, 0, bytes.length);
                zos.closeEntry();
            }
            zos.close();
        } catch (FileNotFoundException ex) {
            LOGGER.warning("A file does not exist: " + ex);
        } catch (IOException ex) {
            LOGGER.warning("I/O error: " + ex);
        }
    }

    public static List<File> getReportList() {
        List<File> list = new ArrayList<>();
        File recentDirectory = new File(System.getProperty("user.dir") + File.separator + "Reports");
        if (recentDirectory.exists()) {
            list = (List<File>) FileUtils.listFiles(recentDirectory, new String[]{"html"}, true);
        }
        return list;
    }
}