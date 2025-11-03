package com.gl.testngfw.common;

import com.gl.testngfw.utility.FileUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This class gets all the classpath for the tests
 * under the package specified
 */
public class PackageUtil {
    private static final Logger LOGGER = Logger.getLogger(PackageUtil.class.getName());

    public List<Class> getTestClassList() {
        List<Class> testClasses = new ArrayList<>();
        List<String> classes;
        URL dirUrl;

        try {
            //getting the list of compiled classes from build where all the compiled classes will be kept inside specific Automation project.
            String path = System.getProperty("user.dir");
            File file = new File(path + "/classes");

            if (file.exists()) {
                classes = FileUtil.getClassFiles(path + "/classes", "scripts"); //Ant Build
                dirUrl = new URL("file:" + path + "/classes/test");
            } else {
                classes = FileUtil.getClassFiles(path + "/target/test-classes", "test"); //Maven Build
                dirUrl = new URL("file:" + path + "/target/test-classes/");
            }

            URLClassLoader cl = new URLClassLoader(new URL[]{dirUrl},
                    this.getClass().getClassLoader());
            for (String cls : classes) {
                if (cls != null) {
                    testClasses.add(cl.loadClass(cls));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Test not found in package", e);
        }
        return testClasses;
    }
}
