package com.gl.testngfw.execution;

import com.gl.testngfw.enums.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Class for Test Header Data information.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD}) //on Method level
public @interface HeaderData {


    String EMPTY_VALUE = "";

    /**
     * Configuration Enum for giving information about the executableFor to run in.
     */
    Platforms[] executableFor();

    /**
     * Giving information about Test script.
     */
    String testDescription();

    /**
     * Test case id of that script.
     */
    String testCaseId();

    /**
     * Test case id of that script.
     */
    String testCaseUniqueId();

    /**
     * Test case id of that script.
     */
    String featureName();

    /**
     * Requirement id of that script.
     */
    String requirementID();

    /**
     * A type of test data
     */
    DataType dataType() default DataType.EMPTY;

    /**
     * A path to test data
     */
    String testDataPath() default EMPTY_VALUE;

    /**
     * Suite Name
     */
    Suites[] suite();

    /**
     * Number of instance for concurrent execution
     */
    String instances() default "1";

    /**
     * OEM Name
     */
    OEM[] targetOEM() default {};

    /**
     * supported DeviceModel
     */
    String[] targetDeviceModel() default {};

    /**
     * supported AndroidVersion
     */
    String[] targetVersions() default {};

    /**
     * supported Browser
     */
    Browser[] targetBrowser() default {};
}