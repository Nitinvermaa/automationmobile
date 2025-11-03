package com.gl.testngfw.execution;

import com.gl.testngfw.report.model.TestCase;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

class MyTransformer implements IAnnotationTransformer {
    private Integer invocationCount;

    @Override
    public void transform(ITestAnnotation iTestAnnotation, Class aClass, Constructor constructor, Method method) {
        iTestAnnotation.setRetryAnalyzer(RetryAnalyzer.class);
       if(method!=null){
        if (getMethodName(method).equals(method.getName())) {
            iTestAnnotation.setInvocationCount(invocationCount);
        }
       }
    }

    private String getMethodName(Method method) {
        Map<String, TestCase> testCasesList = TestNgRunner.getTestCases();
        String methodName="";
        if (testCasesList != null) {
            for (String test : testCasesList.keySet()) {
                String name = testCasesList.get(test).getTestCaseName();
                if (method.getName().equals(name)) {
                    methodName = name;
                    invocationCount = testCasesList.get(test).getApiInstance();
                }
            }
        }
        return methodName;
    }
}
