package com.gl.testngfw.execution;

import com.gl.testngfw.common.PackageUtil;
import com.gl.testngfw.report.model.TestCase;
import com.gl.testngfw.setup.JunitConfig;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JunitRunner {
    private Map<String, TestCase> testList;
    private String executionType;
    private String platform;
    private PackageUtil packageUtil = new PackageUtil();

    JunitRunner(String platform, String type, Map<String, TestCase> testCasesList) {
        testList = testCasesList;
        executionType = type;
        this.platform = platform;
        initJunitConfig();
    }

    private void initJunitConfig() {
        JunitConfig config = new JunitConfig(platform, executionType, String.valueOf(testList.size()));
    }

    private Map<String, Class> getJunitApiTests() {
        Map<String, Class> finalClassList = new HashMap<>();
        List<Class> classList = TestExecutor.getClassList();
        for (Class cls : classList) {
            for (String key : testList.keySet()) {
                if (testList.get(key) != null && isJunitClass(cls, testList.get(key))) {
                    finalClassList.put(testList.get(key).getTestCaseName(), cls);
                }
            }
        }
        return finalClassList;
    }

    private static boolean isJunitClass(Class cls, TestCase testCase) {
        boolean isJunitClass = false;
        Method[] declaredMethods = cls.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Test.class) && testCase.getTestCaseName().equals(method.getName())) {
                isJunitClass = true;
            }
        }
        return isJunitClass;
    }

    void executeJunitTest() {
        Map<String, Class> tests = getJunitApiTests();
        if (!tests.isEmpty()) {
            JUnitCore runner = new JUnitCore();
           // runner.addListener(new JunitListener());
            for (String test : tests.keySet()) {
                Request request = Request.method(tests.get(test), test);
                runner.run(request);
            }
        }
    }
}
