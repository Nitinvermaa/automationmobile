package com.gl.testngfw.common;

import com.gl.testngfw.enums.ExecutorType;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.execution.TestExecutor;
import com.gl.testngfw.report.model.ConfigData;
import com.gl.testngfw.report.model.Steps;
import com.gl.testngfw.report.model.TestCase;
import com.gl.testngfw.setup.BaseInitializer;
import com.gl.testngfw.setup.InitializerScript;
import io.cucumber.core.gherkin.messages.internal.gherkin.Gherkin;
import io.cucumber.messages.IdGenerator;
import io.cucumber.messages.Messages;
import io.cucumber.messages.Messages.GherkinDocument;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleWrapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@CucumberOptions(
        features = {"src/test/java/"},
        glue = {"StepDefinitions"},
        dryRun = true
)
public class CucumberParser {
    private static ConfigData configData = new ConfigData();
    private static List<TestCase> testCases = new ArrayList<>();

    public static List<TestCase> getCucumberTests(List<Class> classList) {
        IdGenerator idGenerator = new IdGenerator.Incrementing();
        List<String> paths = getFeatureFilePath();
        boolean includeSource = false;
        boolean includeAst = true;
        boolean includePickles = false;
        List<Messages.Envelope> envelopes = Gherkin.fromPaths(paths, includeSource, includeAst, includePickles, idGenerator).collect(Collectors.toList());
        for (Messages.Envelope envelope : envelopes) {
            GherkinDocument gherkinDocument = envelope.getGherkinDocument();
            GherkinDocument.Feature feature = gherkinDocument.getFeature();
            List<GherkinDocument.Feature.FeatureChild> scenarios = feature.getChildrenList();
            for (GherkinDocument.Feature.FeatureChild scenario : scenarios) {
                TestCase testCase = new TestCase();
                testCase.setFeature(feature.getName());
                setPlatforms(scenario.getScenario().getTagsList(), testCase);
                testCase.setTestCaseClassName(getTestClass(feature.getName(), classList));
                testCase.setTestCaseName("feature");
                testCase.setTestCaseDesc(scenario.getScenario().getName());
                testCase.setProjectUniqueKey(configData.getProjectID());
                testCase.setApiInstance(1);
                List<Steps> stepList = new ArrayList<>();
                for (GherkinDocument.Feature.Step s : scenario.getScenario().getStepsList()) {
                    Steps steps = new Steps();
                    steps.setLabel(s.getKeyword());
                    steps.setStep(s.getText());
                    steps.setLineNumber(s.getLocation().getLine());
                    stepList.add(steps);
                }
                testCase.setSteps(stepList);
                testCase.setExecutorType(ExecutorType.CUCUMBER);
                testCases.add(testCase);
            }

        }
        return testCases;
    }

    private static String getTestClass(String feature, List<Class> classList) {
        String classname = "";
        for (Class cls : classList) {
            if (cls.isInstance(InitializerScript.class)) {
                CucumberOptions option = (CucumberOptions) cls.getAnnotation(CucumberOptions.class);
                if (Arrays.toString(option.features()).contains(feature)) {
                    classname = cls.getSimpleName();
                    break;
                }
            }
        }
        return classname;
    }

    private static void setPlatforms(List<GherkinDocument.Feature.Tag> tags, TestCase testCase) {
        List<Platforms> platforms = new ArrayList<>();
        for (GherkinDocument.Feature.Tag tag : tags) {
            String tagName = tag.getName().contains("(") ? tag.getName().substring(0, tag.getName().indexOf("(")) : tag.getName().replaceAll(",", "");
            switch (tagName) {
                case "@TestId":
                    String id = tag.getName();
                    id = id.substring(id.indexOf("(") + 1, id.indexOf(")"));
                    testCase.setTestCaseId(id);
                    testCase.setTestCaseUniqueKey(id);
                    break;
                case "@RequirementId":
                    String requirement = tag.getName();
                    requirement = requirement.substring(requirement.indexOf("(") + 1, requirement.indexOf(")"));
                    testCase.setRequirementId(requirement);
                    break;
                case "@Web":
                    platforms.add(Platforms.WEB);
                    break;
                case "@Android":
                    platforms.add(Platforms.ANDROID);
                    break;
                case "@IOS":
                    platforms.add(Platforms.IOS);
                    break;
                case "@API":
                    platforms.add(Platforms.API);
                    break;
                case "@AndroidBrowser":
                    platforms.add(Platforms.ANDROID_CHROME);
                    break;
                case "@IosBrowser":
                    platforms.add(Platforms.IOS_SAFARI);
                    break;
                case "@STB":
                    platforms.add(Platforms.STB);
                    break;
            }
        }
        testCase.setSupportedPlatform(platforms);
    }

    public static Object[][] getScenarios(String platform, Object[][] scenarios) {
        List<Object[]> outerList = new ArrayList<Object[]>();
        for (int i = 0; i < scenarios.length; i++) {
            Object[] inner = scenarios[i];
            if (inner != null) {
                List<Object> list = new ArrayList<Object>();
                int index = 0;
                for (int j = 0; j < inner.length; j++) {
                    if (!ignoreTest(((PickleWrapper) scenarios[i][0]).getPickle().getTags(), platform)) {
                        list.add(inner[index]);
                        index++;
                    }
                }
                if (!list.isEmpty()) {
                    outerList.add(list.toArray(new Object[0]));
                }
            }
        }
        if (outerList.isEmpty()) {
            return new Object[0][0];
        }
        return outerList.toArray(new Object[outerList.size()][]);
    }

    private static boolean ignoreTest(List<String> tags, String platform) {
        if (tags.isEmpty() || tags.contains("Ignore")) {
            return true;
        } else if (BaseInitializer.getConfigData().isDebugMode()) {
            return false;
        }
        for (String tag : tags) {
            if (tag.contains("TestId")) {
                String id = tag.substring(tag.indexOf("(") + 1, tag.indexOf(")"));
                return TestExecutor.getTestCasesList().get(platform).values().stream().noneMatch(c -> c.getTestCaseUniqueKey().equalsIgnoreCase(id));
            }
        }
        return false;
    }

    private static List<String> getFeatureFilePath() {
        List<String> featureList = new ArrayList<>();
        File root = new File(System.getProperty("user.dir") + "/src/test/resources/");
        Collection files = FileUtils.listFiles(root, new String[]{"feature"}, true);
        for (Object file1 : files) {
            File file = (File) file1;
            featureList.add(file.getAbsolutePath());
        }
        return featureList;
    }
}