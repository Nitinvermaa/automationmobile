package com.gl.testngfw.setup;

public class JunitConfig {
    private static ThreadLocal<String> localPlatform = new ThreadLocal<>();
    private static ThreadLocal<String> localExecutionType = new ThreadLocal<>();
    private static ThreadLocal<String> localTotalTestCase = new ThreadLocal<>();

    public JunitConfig(String platform, String executionType, String totalTestCase) {
        localPlatform.set(platform);
        localExecutionType.set(executionType);
        localTotalTestCase.set(totalTestCase);
    }

    static String getLocalPlatform() {
        return localPlatform.get();
    }

    static String getLocalExecutionType() {
        return localExecutionType.get();
    }

    static String getLocalTotalTestCase() {
        return localTotalTestCase.get();
    }
}
