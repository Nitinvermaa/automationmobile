package com.gl.testngfw.execution;

import com.gl.testngfw.common.Constants;
import com.gl.testngfw.enums.ExecutorType;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.report.model.ConfigData;
import com.gl.testngfw.report.model.TestCase;
import com.gl.testngfw.report.model.UserDevice;
import com.gl.testngfw.setup.CucumberExecutor;
import io.cucumber.testng.CucumberOptions;
import org.testng.TestNG;
import org.testng.xml.*;

import java.util.*;
import java.util.stream.Collectors;

class TestNgRunner {
    private Map<String, TestCase> testList;
    private String executionType;
    private String platform;
    private ConfigData configData;
    private Map<String, UserDevice> deviceInfoMap = TestExecutor.getDeviceInfoMap();
    private List<String> deviceList = new ArrayList<>(deviceInfoMap.keySet());
    private boolean isCucumber;
    private static TestNgRunner runner;

    TestNgRunner(String platform, String type, Map<String, TestCase> testCasesList) {
        testList = testCasesList;
        executionType = type;
        this.platform = platform;
        configData = TestExecutor.getConfigData();
        isCucumber= testList.values().stream().anyMatch(x->x.getExecutorType().equals(ExecutorType.CUCUMBER));
        runner=this;
    }

    static Map<String, TestCase> getTestCases() {
        return runner.testList;
    }
    /**
     * Method to create Test Suite as per executableFor
     *
     * @param count: number of connected devices/browsers/API servers
     * @return : XML Suite
     */
    private List<XmlSuite> setXmlSuiteList(int count) {
        List<XmlSuite> suiteList = new ArrayList<>();
        String parameterValue = platform;
        String suiteName = getSuiteName();
        suiteName = configData.isLoadTesting() ? suiteName + "_" + testList.get(TestExecutor.record.get(count)).getTestCaseName() : suiteName;
        XmlTest xmlTest = new XmlTest();
        xmlTest.setName("Sample Test");
        xmlTest.addParameter("platform", parameterValue);
        xmlTest.addParameter("executionType", executionType);
        if(!isCucumber){
            xmlTest.setClasses(getXMLClassList(count));
        }else{
            xmlTest.setClasses(getCucumberClassList(TestExecutor.getClassList(),testList));
        }
        XmlSuite xmlSuite = new XmlSuite();
        xmlSuite.setName(suiteName + "_Suite");
        Map<String, String> suitParam = new HashMap<>();
        suitParam.put("platform", parameterValue);
        suitParam.put("executionType", executionType);
        xmlSuite.setParameters(suitParam);
        xmlSuite.setConfigFailurePolicy("continue");
        xmlTest.setSuite(xmlSuite);
        xmlSuite.setTests(Collections.singletonList(xmlTest));

        if (configData.getExecuteFor().contains(Platforms.API) &&
                configData.getExecutionType().equalsIgnoreCase(Platforms.LOAD_TESTING.name())) {
            xmlSuite.setThreadCount(testList.get(TestExecutor.record.get(count)).getApiInstance());
            xmlSuite.setParallel(XmlSuite.ParallelMode.TESTS);
        } else {
            xmlSuite.setThreadCount(configData.getInstances());
            xmlSuite.setParallel(XmlSuite.ParallelMode.CLASSES);
        }
        xmlSuite.setListeners(Collections.singletonList(TestListener.class.getCanonicalName()));
        if (!xmlTest.getClasses().isEmpty()) {
            suiteList.add(xmlSuite);
        }
        return suiteList;
    }

    /**
     * Method to get count of number of connected devices/browsers/API servers
     *
     * @return count
     */
    private String getSuiteName() {
        String parameter = platform;
        if (executionType.equalsIgnoreCase(Constants.MOBILE)) {
            parameter = deviceInfoMap.get(platform).getDeviceName();
        }
        return parameter;
    }

    /**
     * Method to get XML Class List
     *
     * @return XML Class List
     */
    private synchronized List<XmlClass> getXMLClassList(int deviceIndex) {
        List<XmlClass> list = new ArrayList<>();
        List<Class> classList = TestExecutor.getClassList();
        if (configData.getExecutionType().equalsIgnoreCase(Constants.LOAD_TESTING) && configData.getExecuteFor().contains(Platforms.API)) {
            addXmlApiTests(deviceIndex, list, classList, testList);
        } else if (configData.getExecutionType().equalsIgnoreCase(Constants.PARALLEL)) {
            addParallelXmlTests(deviceIndex, list, classList, testList);
        } else if (configData.getExecutionType().equalsIgnoreCase(Constants.DISTRIBUTED) &&
                configData.getExecuteFor().contains(Platforms.MOBILE)) {
            addDistributedXmlTests(deviceIndex, list, classList, testList);
        }
        return list;
    }

    private void addDistributedXmlTests(int deviceIndex, List<XmlClass> list, List<Class> classList, Map<String, TestCase> testList) {
        for (Class cls : classList) {
            XmlClass xmlClass = null;
            List<String> excludeList = new ArrayList<>();
            List<XmlInclude> includedMethods = new ArrayList<>();
            List<String> finalList = getXmlTestList(deviceIndex, testList);
            for (String key : finalList) {
                xmlClass = includeGroupTests(testList, cls, excludeList, includedMethods, key);
            }
            addTest(list, xmlClass, excludeList, includedMethods);
        }
    }

    private void addParallelXmlTests(int deviceIndex, List<XmlClass> list, List<Class> classList, Map<String, TestCase> testList) {
        for (Class cls : classList) {
            XmlClass xmlClass = null;
            List<String> excludeList = new ArrayList<>();
            List<XmlInclude> includedMethods = new ArrayList<>();
            for (String key : testList.keySet()) {
                if (testList.get(key) != null && testList.get(key).getExecutorType().equals(ExecutorType.TESTNG) &&
                        key.substring(0, key.lastIndexOf(".")).equals(cls.getSimpleName())) {
                    xmlClass = new XmlClass(cls);
                    includeTests(deviceIndex, testList, excludeList, includedMethods, key);
                }
            }
            addTest(list, xmlClass, excludeList, includedMethods);
        }
    }

    private List<XmlClass> getCucumberClassList(List<Class> classList, Map<String, TestCase> testList) {
        List<Class> cucumberClass = classList.stream().filter(c -> CucumberExecutor.class.isAssignableFrom(c)&&
                testList.values().stream().anyMatch(y->{
            CucumberOptions option = (CucumberOptions)c.getAnnotation(CucumberOptions.class);
            return Arrays.toString(option.features()).contains(y.getTestCaseClassName());
        })).collect(Collectors.toList());
        List<XmlClass> xmlClassList = new ArrayList<>();
        for(Class c:cucumberClass){
            xmlClassList.add(new XmlClass(c));
        }
        return xmlClassList;
    }

    private void addXmlApiTests(int deviceIndex, List<XmlClass> list, List<Class> classList, Map<String, TestCase> testList) {
        for (Class cls : classList) {
            XmlClass xmlClass = null;
            List<String> excludeList = new ArrayList<>();
            List<XmlInclude> includedMethods = new ArrayList<>();
            for (String key : testList.keySet()) {
                if (testList.get(key) != null && testList.get(key).getExecutorType().equals(ExecutorType.TESTNG)) {
                    xmlClass = includeApiTests(deviceIndex, cls, excludeList, includedMethods, key);
                }
            }
            addTest(list, xmlClass, excludeList, includedMethods);
        }
    }

    private XmlClass includeApiTests(int deviceIndex, Class cls, List<String> excludeList, List<XmlInclude> includedMethods, String key) {
        XmlClass xmlClass = null;
        String className = ".*" + key.substring(key.indexOf(".") + 1);
        if (TestExecutor.record.get(deviceIndex).equals(key)) {
            if (key.substring(0, key.lastIndexOf(".")).equals(cls.getSimpleName())) {
                xmlClass = new XmlClass(cls);
                includedMethods.add(new XmlInclude(className));
            }
        } else {
            excludeList.add(className);
        }
        return xmlClass;
    }

    private List<String> getXmlTestList(int deviceIndex, Map<String, TestCase> testList) {
        List<String> finalList;
        if (!Constants.isIsTestGrouped()) {
            finalList = groupTests(deviceIndex, testList);
        } else {
            finalList = new ArrayList<>(testList.keySet());
        }
        return finalList;
    }

    private XmlClass includeGroupTests(Map<String, TestCase> testList, Class cls, List<String> excludeList, List<XmlInclude> includedMethods, String key) {
        XmlClass xmlClass = null;
        if (key.substring(0, key.lastIndexOf(".")).equals(cls.getSimpleName())) {
            xmlClass = new XmlClass(cls);
            if (testList.get(key) == null) {
                excludeList.add(".*" + key.substring(key.indexOf(".") + 1));
            } else if (testList.get(key).getExecutorType().equals(ExecutorType.TESTNG)) {
                includedMethods.add(new XmlInclude(".*" + testList.get(key).getTestCaseName()));
            }
        }
        return xmlClass;
    }

    private List<String> groupTests(int deviceIndex, Map<String, TestCase> testList) {
        List<String> finalList;
        List<String> tests = new ArrayList<>();
        List<List<String>> partitions = new LinkedList<>();
        for (String key : testList.keySet()) {
            if (testList.get(key) != null) {
                tests.add(key);
            }
        }
        // for device specific distribution update final list for device
        int partitionSize = Math.round((float) tests.size() / deviceInfoMap.size());
        for (int i = 0; i < tests.size(); i += partitionSize) {
            partitions.add(tests.subList(i,
                    Math.min(i + partitionSize, tests.size())));
        }
        finalList = partitions.get(deviceIndex);
        return finalList;
    }

    private void includeTests(int deviceIndex, Map<String, TestCase> testList, List<String> excludeList, List<XmlInclude> includedMethods, String key) {
        if (!testList.get(key).getTargetOem().isEmpty() &&
                !testList.get(key).getTargetOem().contains(deviceInfoMap.get(deviceList.get(deviceIndex)).getDeviceName().split(" ")[0])) {
            excludeList.add(".*" + testList.get(key).getTestCaseName());
        } else if (!testList.get(key).getTargetVersion().isEmpty() &&
                !testList.get(key).getTargetVersion().contains(deviceInfoMap.get(deviceList.get(deviceIndex)).getDeviceOS())) {
            excludeList.add(".*" + testList.get(key).getTestCaseName());
        } else if (!testList.get(key).getTargetDeviceModel().isEmpty() &&
                !testList.get(key).getTargetDeviceModel().contains(deviceInfoMap.get(deviceList.get(deviceIndex)).getDeviceName())) {
            excludeList.add(".*" + testList.get(key).getTestCaseName());
        } else if (!testList.get(key).getTargetBrowser().isEmpty() &&
                !testList.get(key).getTargetBrowser().contains(configData.getBrowserList().get(deviceIndex).name())) {
            excludeList.add(".*" + testList.get(key).getTestCaseName());
        } else {
            includedMethods.add(new XmlInclude(".*" + testList.get(key).getTestCaseName()));
        }
    }

    private void addTest(List<XmlClass> list, XmlClass xmlClass, List<String> excludeList, List<XmlInclude> includedMethods) {
        if (xmlClass != null) {
            xmlClass.setExcludedMethods(excludeList);
            xmlClass.setIncludedMethods(includedMethods);
            list.add(xmlClass);
        }
    }

    void ExecuteTests(int count) {
        TestNG testng = new TestNG();
        testng.setAnnotationTransformer(new MyTransformer());
        List<XmlSuite> suites = setXmlSuiteList(count);
        if (!suites.isEmpty()) {
            testng.setXmlSuites(suites);
            testng.run();
        }
    }
}
