package com.gl.testngfw.datadriven.model;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Specifies the container for obtained test data
 * </p>
 */
public class DataContainer {
    private final List<TestDataRecord> testDataList;

    public DataContainer() {
        testDataList = new ArrayList<TestDataRecord>();
    }

    /**
     * Returns a list with test data
     *
     * @return {@link List}
     * @see
     */
    public List<TestDataRecord> getTestDataList() {
        return testDataList;
    }

    /**
     * Adds a test data record to the list with test data
     *
     * @param testDataRecord a test data record
     * @see
     */
    public void addTestData(TestDataRecord testDataRecord) {
        testDataList.add(testDataRecord);
    }

    /**
     * Returns a test data by the given index
     *
     * @param index - an index of a test data in the list
     * @return {@link
     * @see
     */
    public TestDataRecord getTestDataByIndex(int index) {
        return testDataList.get(index);
    }
}
