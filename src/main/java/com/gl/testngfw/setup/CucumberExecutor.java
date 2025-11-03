package com.gl.testngfw.setup;

import com.gl.testngfw.execution.TestExecutor;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class CucumberExecutor extends InitializerScript {
    private static TestNGCucumberRunner testNGCucumberRunner;
    @BeforeClass
    public void initRunner() {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    public void executeTests(PickleWrapper pickle, FeatureWrapper cucumberFeature) {
        testNGCucumberRunner.runScenario(pickle.getPickle());
    }

    @DataProvider
    public Object[][] features() {
        if (testNGCucumberRunner == null) {
            testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
        }
        return getScenarios();
    }


    public static Object[][] getScenarios() {
        if (testNGCucumberRunner == null) {
            return new Object[0][0];
        }
        Object[][] scenarios = testNGCucumberRunner.provideScenarios();
        List<Object[]> outerList = new ArrayList<Object[]>();
        for (int i = 0; i < scenarios.length; i++) {
            Object[] inner = scenarios[i];
            if (inner != null) {
                List<Object> list = new ArrayList<Object>();
                int index = 0;
                for (int j = 0; j < inner.length; j++) {
                    if (!ignoreTest(((PickleWrapper) scenarios[i][0]).getPickle().getTags(), getCurrentPlatform())) {
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

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        if (testNGCucumberRunner == null) {
            return;
        }
        testNGCucumberRunner.finish();
    }
}
