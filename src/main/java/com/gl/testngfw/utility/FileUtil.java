package com.gl.testngfw.utility;

import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.report.model.PropertyModel;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.openqa.selenium.logging.LogEntry;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileUtil {
    private static final Logger LOGGER = Logger.getLogger(FileUtil.class.getName());
    private static final String USER_DIR = System.getProperty("user.dir");

    /**
     * Write data in file
     *
     * @param path : File path
     * @param data : value which need to write in file.
     */
    public static synchronized void writeInFile(String path, String data) {
        try {
            File file = new File(path);
            if (!file.exists() && !file.createNewFile()) {
                FrameworkLogger.logFail("File not created");
            }
            BufferedWriter bw;
            try (FileWriter fw = new FileWriter(file.getAbsoluteFile())) {
                bw = new BufferedWriter(fw);
            }
            bw.write(data);
            bw.close();

        } catch (Exception ex) {
            FrameworkLogger.logError(ex);
        }
    }

    /**
     * Method to get Properties Map
     *
     * @param server Execution Server
     * @return Property Map
     */
    public static Map<String, String> getPropertyMAP(String server) {
        try {
            String path = USER_DIR + "/resources/serverURL/" + server + "/config.properties";
            File config = new File(path);
            Map<String, String> properties;
            try (FileInputStream input = new FileInputStream(config.getAbsolutePath())) {
                Properties prop = new Properties();
                prop.load(input);
                properties = new HashMap<>();
                for (Object key : prop.keySet()) {
                    properties.put(key.toString(), prop.getProperty(key.toString()));
                }
            }
            return properties;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "", e);
        }
        return null;
    }


    /**
     * Method to get Properties Map from property file
     *
     * @return : Property Map
     */
    public static Map<String, String> getConfigMAP() {
        try {
            String path = USER_DIR + "/Project.Properties";
            File config = new File(path);
            Map<String, String> properties;
            try (FileInputStream input = new FileInputStream(config.getAbsolutePath())) {
                Properties prop = new Properties();
                prop.load(input);
                properties = new HashMap<>();
                for (Object key : prop.keySet()) {
                    properties.put(key.toString(), prop.getProperty(key.toString()));
                }
            }
            return properties;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "", e);
        }
        return null;
    }

    /**
     * Method to get Properties Map from property file
     *
     * @return : Property Map
     */
    public synchronized static Map<String, PropertyModel> getConfigMAP(String fileName) {
        try {
            String path = USER_DIR + "/" + fileName;
            File config = new File(path);
            LinkedHashMap<String, PropertyModel> properties;
            try (FileInputStream input = new FileInputStream(config.getAbsolutePath())) {
                Properties prop = new Properties();
                prop.load(input);
                properties = new LinkedHashMap<>();
                for (Object key : prop.keySet()) {
                    Gson gson = new Gson();
                    properties.put(key.toString(), gson.fromJson(prop.getProperty(key.toString()), PropertyModel.class));
                }
            }
            return properties;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "", e);
        }
        return null;
    }

    /**
     * Write data in device properties file.
     *
     * @param data : Value to write in file.
     */
    public static void writeInDevicePropertiesFile(String data) {
        try {
            File file = new File("DeviceInfo.Properties");
            if (!file.exists() && !file.createNewFile()) {
                FrameworkLogger.logFail("DeviceInfo.Properties file not created");
            }
            BufferedWriter bw;
            try (FileWriter fw = new FileWriter(file.getAbsoluteFile())) {
                bw = new BufferedWriter(fw);
            }
            bw.write(data);
            bw.close();
        } catch (Exception ex) {
            FrameworkLogger.logError(ex);
        }
    }

    /**
     * Method to write data in report properties file.
     *
     * @param data : Value to write in file.
     */
    public static void writeInReportPropertiesFile(String data) {
        try {
            File file = new File("Report.Properties");
            if (!file.exists() && !file.createNewFile()) {
                FrameworkLogger.logFail("Report.Properties file not created");
            }
            BufferedWriter bw;
            try (FileWriter fw = new FileWriter(file.getAbsoluteFile())) {
                bw = new BufferedWriter(fw);
            }
            bw.write(data);
            bw.close();
        } catch (Exception ex) {
            FrameworkLogger.logError(ex);
        }
    }

    /**
     * Used to read value for a particular key from property file.
     *
     * @param key - key to value in property file.
     * @return - value for a key from property file.
     */
    public static String readProperties(String key) {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("Project.Properties"));
            return prop.getProperty(key);
        } catch (Exception ex) {
            FrameworkLogger.logError(ex);
        }
        return "";
    }

    /**
     * Read property files
     *
     * @param filename : File Name
     * @param property : property
     * @return :
     */
    public synchronized static String readFile(String filename, String property) {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(filename);
            prop.load(input);
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "", e);
                }
            }
        }
        return prop.getProperty(property);
    }

    /**
     * Read property files
     *
     * @param filename : File Name
     * @return :
     */
    public static Map<String, String> getPropertyMap(String filename) {
        Properties prop = new Properties();
        InputStream input = null;
        Map<String, String> propertyMap = new HashMap<>();
        try {
            input = new FileInputStream(filename);
            prop.load(input);
            Set<String> property = prop.stringPropertyNames();
            for (String key : property) {
                propertyMap.put(key, prop.getProperty(key));
            }
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "", e);
                }
            }
        }
        return propertyMap;
    }

    /*
     * Getting list of class files.
     */
    public static List<String> getClassFiles(String directory, String packageName) {
        File dir = new File(directory);
        List<String> fileNames = new ArrayList<>();
        try {
            List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            for (File file : files) {
                if (System.getProperty("os.name").contains("Windows")) {
                    if (!file.toString().contains("$") && file.toString().contains(packageName) && file.toString().contains(".class")) {
                        String fileName = file.toString().substring(file.toString().lastIndexOf("com\\")).
                                replace(".class", "").replace("\\", ".");
                        fileNames.add(fileName);
                    }
                } else {
                    if (!file.toString().contains("$") && file.toString().contains(packageName)&& file.toString().contains(".class")) {
                        String fileName = file.toString().substring(file.toString().lastIndexOf("com/")).
                                replace(".class", "").replace("/", ".");
                        fileNames.add(fileName);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "", e);
        }
        return fileNames;
    }

    /**
     * Set property
     *
     * @param filename :
     * @param property :
     * @param value    :
     * @throws IOException io exception
     */
    public synchronized static void setProjectProperty(String filename, String property, String value) throws IOException {
        File propertyFile = new File(filename);
        if (!propertyFile.exists() && !propertyFile.createNewFile()) {
            FrameworkLogger.logFail(filename + " file not created");
        }
        Properties pop = new Properties();
        pop.load(new FileInputStream(propertyFile));
        pop.put(property, value);
        FileOutputStream output = new FileOutputStream(propertyFile);
        pop.store(output, "This is overwrite file");

    }

    public static void setProjectProperty(String filename, Map<String, PropertyModel> properties) throws IOException {
        File propertyFile = new File(filename);
        if (!propertyFile.exists() && !propertyFile.createNewFile()) {
            FrameworkLogger.logFail(filename + " file not created");
        }
        Properties pop = new Properties();
        pop.load(new FileInputStream(propertyFile));
        for (String key : properties.keySet()) {
            Gson gson = new Gson();
            pop.put(key, gson.toJson(properties.get(key)));
        }
        FileOutputStream output = new FileOutputStream(propertyFile);
        pop.store(output, "This is overwrite file");


    }

    /**
     * Set property
     *
     * @param filename :
     * @throws IOException io exception
     */
    public static void clearProjectProperty(String filename) throws IOException {
        File propFile = new File(filename);
        if (propFile.exists()) {
            Properties pop = new Properties();
            pop.load(new FileInputStream(propFile));
            pop.clear();
            FileOutputStream output = new FileOutputStream(propFile);
            pop.store(output, "This is overwrite file");
        }
    }

    public static void writeLogInFile(String fileName, List<LogEntry> data) {
        try {
            File file = new File(fileName);
            if (file.exists() && !file.delete()) {
                FrameworkLogger.logFail(fileName + " file not deleted");
            }
            if (!file.createNewFile()) {
                FrameworkLogger.logFail(fileName + " file not created");
            }

            try (FileWriter fw = new FileWriter(file.getAbsoluteFile(), true)) {
                for (LogEntry line : data) {
                    fw.write(String.valueOf(line));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    public static String getFilePath(String directory, String fileName) {
        File root = new File(directory);

        String filePath = "";
        try {
            Collection files = FileUtils.listFiles(root, null, true);

            for (Object file1 : files) {
                File file = (File) file1;
                if (file.getName().equals(fileName))
                    filePath = file.getAbsolutePath();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "", e);
        }
        return filePath;
    }

    public static String getFilePath(String directory, String fileName, String[] fileExtensions) {
        File root = new File(directory);

        String filePath = "";
        try {
            Collection files = FileUtils.listFiles(root, fileExtensions, true);

            for (Object file1 : files) {
                File file = (File) file1;
                if (file.getName().equals(fileName))
                    filePath = file.getAbsolutePath();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "", e);
        }
        return filePath;
    }

    public static String getFilePathFromResourcePath(String fileName) {
        URL url = Util.class.getClassLoader().getResource(fileName);
        String path = null;
        if (url != null) {
            File file = new File(url.getPath());
            path = file.getAbsolutePath();
        }
        return path;
    }

    public static String getFilePathFromResourcePath(Class cl, String fileName) {
        URL url = cl.getClassLoader().getResource(fileName);
        String path = null;
        if (url != null) {
            File file = new File(url.getPath());
            path = file.getAbsolutePath();
        }
        return path;
    }
}
