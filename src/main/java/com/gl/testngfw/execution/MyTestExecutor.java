package com.gl.testngfw.execution;

import com.gl.testngfw.enums.Browser;
import com.gl.testngfw.enums.Platforms;
import com.gl.testngfw.report.model.ConfigData;

class MyTestExecutor {
    private MyTestExecutor() {
    }

    public static void execute() {
        TestExecutor testExecutor = new TestExecutor();
        ConfigData configData = TestExecutor.getConfigData();
        for (Platforms platform : configData.getExecuteFor()) {
            switch (platform) {
                case MOBILE:
                    for (String div : TestExecutor.getDeviceInfoMap().keySet()) {
                        TestExecutor.executionList.put(div, platform.name());
                    }
                    break;
                case WEB:
                    for (Browser browser : configData.getBrowserList()) {
                        TestExecutor.executionList.put(browser.name(), platform.name());
                    }
                    break;
                case API:
                    TestExecutor.executionList.put(platform.name(), platform.name());
                    break;
                case CONTRACT:
                    TestExecutor.executionList.put(platform.name(), platform.name());
                    break;
                case PERFORMANCE:
                    TestExecutor.executionList.put(platform.name(), platform.name());
                    break;
                case STB:
                    TestExecutor.executionList.put(platform.name(), platform.name());
                    break;
                case ROKU:
                    TestExecutor.executionList.put(platform.name(), platform.name());
                    break;
                case DESKTOP:
                    TestExecutor.executionList.put(platform.name(), platform.name());
                    break;
                default:
                    break;
            }
        }

        testExecutor.distributeTests(TestExecutor.executionList.size());
    }
}
