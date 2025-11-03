package com.gl.testngfw.common;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.gl.testngfw.enums.Browser;
import com.gl.testngfw.enums.OEM;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.enums.Suites;
import com.gl.testngfw.execution.HeaderData;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.report.model.*;
import com.gl.testngfw.utility.FileUtil;
import com.gl.testngfw.utility.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestSuiteUpdater {
    private static final Logger LOGGER = Logger.getLogger(TestSuiteUpdater.class.getName());
    private static DBUpdater dbUpdater = new DBUpdater();
    private static ConfigData configData = TestExecutor.getConfigData();

    private TestSuiteUpdater() {
    }

    /**
     * Method to Parse header annotation from scripts file.
     */
    public static Map<String, UpdateExecution> initTestSuite() throws FileNotFoundException {
        PackageUtil packageUtil = new PackageUtil();
        List<Class> classList = packageUtil.getTestClassList();
        TestExecutor.setClassList(classList);
        List<TestCase> testCaseList = CucumberParser.getCucumberTests(classList);
        UpdateExecution updateExecution = new UpdateExecution();
        Map<String, List<String>> suiteMap = new HashMap<>();
        List<TestSuite> suiteList = new ArrayList<>();
        List<Property> properties = new ArrayList<>();

        for (Class obj : classList) {
            if (obj != null) {
                Method[] declaredMethods = obj.getDeclaredMethods();
                for (Method method : declaredMethods) {
                    if (method.isAnnotationPresent(HeaderData.class)) {
                        TestCase testCase = new TestCase();
                        HeaderData headerData = method.getAnnotation(HeaderData.class);
                        testCase.setExecutorType(Util.getExecutorType(method,headerData));
                        testCase.setProjectUniqueKey(configData.getProjectID());
                        testCase.setTestCaseName(method.getName());
                        testCase.setFeature(headerData.featureName());
                        testCase.setTestCaseClassName(obj.getSimpleName());
                        testCase.setTestCaseDesc(headerData.testDescription());
                        testCase.setTestCaseId(headerData.testCaseId());
                        testCase.setRequirementId(headerData.requirementID());
                        testCase.setApiInstance(Integer.parseInt(headerData.instances()));
                        List<Platforms> supportedPlatform = Arrays.asList(headerData.executableFor());
                        // List<Platforms> testExecutableFor = Arrays.asList(headerData.executableFor());
                        testCase.setSupportedPlatform(supportedPlatform);
                        /*testCase.setForAndroid(testExecutableFor.toString().contains(Constants.ANDROID.toUpperCase()));
                        testCase.setForIos(testExecutableFor.toString().contains(Constants.IOS.toUpperCase()));
                        testCase.setForApi(testExecutableFor.toString().contains(Constants.API.toUpperCase()));
                        testCase.setForWeb(testExecutableFor.toString().contains(Constants.WEB.toUpperCase()));
                        testCase.setForAndroidBrowser(testExecutableFor.toString().contains(Constants.ANDROID_CHROME.toUpperCase()));
                        testCase.setForIosBrowser(testExecutableFor.toString().contains(Constants.IOS_SAFARI.toUpperCase()));*/
                        testCase.setTargetDeviceModel(Arrays.asList(headerData.targetDeviceModel()));
                        List<OEM> oems = Arrays.asList(headerData.targetOEM());
                        List<String> oemList = !oems.isEmpty() ? Arrays.asList(oems.toString().split(",")) : new ArrayList<>();
                        testCase.setTargetOem(oemList);
                        testCase.setTargetVersion(Arrays.asList(headerData.targetVersions()));
                        testCase.setTestCaseUniqueKey(headerData.testCaseUniqueId());
                        List<Browser> browser = Arrays.asList(headerData.targetBrowser());
                        List<String> browserList = !browser.isEmpty() ? Arrays.asList(browser.toString().split(",")) : new ArrayList<>();
                        testCase.setTargetBrowser(browserList);
                        File file = new File(FileUtil.getFilePath("src/test", obj.getSimpleName() + ".java"));
                        if (!"UpdateTestRecords".equals(Thread.currentThread().getStackTrace()[2].getMethodName())) {
                            parseMethod(file, method, testCase);
                        }
                        testCaseList.add(testCase);
                        getTestSuites(suiteMap, headerData);
                    }
                }
            }
        }
        updateSuiteList(suiteMap, suiteList);

        getProperties(properties);

        updateExecution.setTestCaseList(testCaseList);
        updateExecution.setTestSuites(suiteList);
        updateExecution.setProperties(properties);
        Map<String, UpdateExecution> body = new HashMap<>();
        body.put(configData.getProjectID(), updateExecution);
        return body;
    }

    private static void updateSuiteList(Map<String, List<String>> suiteMap, List<TestSuite> suiteList) {
        for (String key : suiteMap.keySet()) {
            TestSuite testSuite = new TestSuite(StringUtils.capitalize(key), suiteMap.get(key));
            suiteList.add(testSuite);
        }
    }

    private static void getTestSuites(Map<String, List<String>> suiteMap, HeaderData headerData) {
        List<Suites> suites = Arrays.asList(headerData.suite());
        suites.forEach(suite ->  addSuite(suiteMap, headerData, suite.name()));
    }

    private static void addSuite(Map<String, List<String>> suiteMap, HeaderData headerData, String suiteName) {
        if (!suiteMap.containsKey(suiteName)) {
            suiteMap.put(suiteName, new ArrayList<>());
        }
        suiteMap.get(suiteName).add(headerData.testCaseUniqueId());
    }

    private static void getProperties(List<Property> properties) {
        addProperty(properties, Constants.PROJECT_PROPERTIES);

        addProperty(properties, Constants.PROJECT_CONFIG_PROPERTIES);

        addProperty(properties, Constants.MAIL_CONFIG_PROPERTIES);
    }

    private static void addProperty(List<Property> properties, String projectProperties) {
        Property projectProperty = new Property();
        projectProperty.setProjectUniqueKey(configData.getProjectID());

        projectProperty.setPropertyFileName(projectProperties);
        projectProperty.setProperties(FileUtil.getConfigMAP(projectProperties));
        properties.add(projectProperty);
    }

    static Map<String, UpdateExecution> updateTestRecords() throws IOException {
        Map<String, UpdateExecution> body = new HashMap<>();
        List<TestCase> finalTestCaseList = new ArrayList<>();
        UpdateExecution updateExecution = new UpdateExecution();
        UpdateExecution newRecords = initTestSuite().get(configData.getProjectID());
        UpdateExecution existingRecord = dbUpdater.getTestSuiteData().extract().body().as(UpdateExecution.class);
        Map<String, TestCase> existingTestRecordMap = new HashMap<>();
        Map<String, TestCase> newTestRecordMap = new HashMap<>();

        getNewTests(newRecords, newTestRecordMap);
        checkDeletedTests(finalTestCaseList, existingRecord, existingTestRecordMap, newTestRecordMap);
        checkNewUpdatedTests(finalTestCaseList, newRecords, existingTestRecordMap);
        getUpdatedTests(body, finalTestCaseList, updateExecution, newRecords);
        writeTestSuite(body);
        return body;
    }

    private static void getUpdatedTests(Map<String, UpdateExecution> body, List<TestCase> finalTestCaseList, UpdateExecution updateExecution, UpdateExecution newRecords) {
        updateExecution.setTestCaseList(finalTestCaseList);
        updateExecution.setTestSuites(newRecords.getTestSuites());
        updateExecution.setProperties(newRecords.getProperties());
        body.put(configData.getProjectID(), updateExecution);
    }

    private static void checkNewUpdatedTests(List<TestCase> finalTestCaseList, UpdateExecution newRecords, Map<String, TestCase> existingTestRecordMap) {
        for (TestCase test : newRecords.getTestCaseList()) {
            if (existingTestRecordMap.containsKey(test.getTestCaseUniqueKey())) {
                boolean status = test.equals(existingTestRecordMap.get(test.getTestCaseUniqueKey()));
                if (!status) {
                    test.setStatus("modified");
                } else {
                    test.setStatus("noChange");
                }
            } else {
                test.setStatus("new");
            }
            finalTestCaseList.add(test);
        }
    }

    private static void checkDeletedTests(List<TestCase> finalTestCaseList, UpdateExecution existingRecord, Map<String, TestCase> existingTestRecordMap, Map<String, TestCase> newTestRecordMap) {
        for (TestCase test : existingRecord.getTestCaseList()) {
            existingTestRecordMap.put(test.getTestCaseUniqueKey(), test);
            if (!newTestRecordMap.containsKey(test.getTestCaseUniqueKey())) {
                test.setStatus("deleted");
                finalTestCaseList.add(test);
            }
        }
    }

    private static void getNewTests(UpdateExecution newRecords, Map<String, TestCase> newTestRecordMap) {
        for (TestCase test : newRecords.getTestCaseList()) {
            newTestRecordMap.put(test.getTestCaseUniqueKey(), test);
        }
    }

    private static void writeTestSuite(Map<String, UpdateExecution> body) {
        try {
            //write converted json data to a file
            Util.createDirectory(System.getProperty("user.dir") + File.separator + "/TestSuite");
            FileWriter writer = new FileWriter(System.getProperty("user.dir") + File.separator + "/TestSuite/TestSuite.json");
            Gson gson = new GsonBuilder().create();
            writer.write(gson.toJson(body));
            writer.close();

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    private static void parseMethod(File file, Method test, TestCase testCase) throws FileNotFoundException {
        // Go through all the types in the file
        FileInputStream in = new FileInputStream(file);
        // parse it
        CompilationUnit cu = JavaParser.parse(in);
        NodeList<TypeDeclaration<?>> types = cu.getTypes();
        for (TypeDeclaration<?> type : types) {
            // Go through all fields, methods, etc. in this type
            NodeList<BodyDeclaration<?>> members = type.getMembers();
            for (BodyDeclaration<?> member : members) {
                if (member instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) member;
                    List<LabeledStmt> labeledStmtList;
                    if (method.getNameAsString().equals(test.getName())) {
                        labeledStmtList = method.getBody().get().findAll(LabeledStmt.class);

                        List<Steps> stepsList = new ArrayList<>();
                        for (LabeledStmt labeledStmt : labeledStmtList) {
                            String statement = labeledStmt.getStatement().toString();
                            Steps steps = new Steps();
                            steps.setLabel(labeledStmt.getLabel().asString());
                            steps.setStep(statement.substring(statement.indexOf("(") + 2, statement.indexOf(")") - 1));
                            steps.setLineNumber(labeledStmt.getBegin().get().line);
                            stepsList.add(steps);
                        }
                        testCase.setSteps(stepsList);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
      //  updateTestRecords();
        DBUpdater.updateTestSuite();
    }
}
