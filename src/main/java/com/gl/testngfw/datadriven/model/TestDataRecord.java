package com.gl.testngfw.datadriven.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Specifies a model for a test data record
 */
public class TestDataRecord {
    private List<Values> values;

    public TestDataRecord() {
        values = new ArrayList<Values>();
    }

    /**
     * Adds a value to the test data record
     *
     * @param value - a value
     * @see
     */
    public void addValue(Values value) {
        values.add(value);
    }

    /**
     * Returns a value by the given index
     *
     * @param index - an index of required value
     * @return {@link
     * @see
     */
    public Values getValueByIndex(int index) {
        return values != null ? values.get(index) : null;
    }

    /**
     * Returns a value by the given name
     *
     * @param name - a name of required value
     * @return a value of the field
     */
    public <T> T getValueByName(String name) {
        for (Values value : values) {
            if (name != null && name.equals(value.getName())) {
                return value.<T>getValue();
            }
        }
        return null;
    }

    /**
     * Returns a string value by the given name
     *
     * @param name - a name of required value
     * @return {@link String}
     */
    public String getStringValueByName(String name) {
        return this.<String>getValueByName(name);
    }

    /**
     * Returns a double value by the given name
     *
     * @param name - a name of required value
     * @return {@link Double}
     */
    public Double getDoubleValueByName(String name) {
        return this.<Double>getValueByName(name);
    }

    /**
     * Returns a double value by the given name
     *
     * @param name - a name of required value
     * @return {@link Long}
     */
    public Long getLongValueByName(String name) {
        return this.<Long>getValueByName(name);
    }

    /**
     * Returns an integer value by the given name
     *
     * @param name - a name of required value
     * @return {@link Integer}
     */
    public Integer getIntegerValueByName(String name) {
        return this.<Integer>getValueByName(name);
    }

    /**
     * Returns an integer value by the given name
     *
     * @param name - a name of required value
     * @return {@link Boolean}
     */
    public Boolean getBooleanValueByName(String name) {
        return this.<Boolean>getValueByName(name);
    }

    /**
     * Returns true of test data contains a value with the given name
     *
     * @param name - a name of test data value
     * @return {@link Boolean}
     */
    public boolean contains(String name) {
        for (Values value : values) {
            if (name != null && name.equals(value.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a size of test data record
     *
     * @return {@link Integer}
     */
    public int size() {
        return values.size();
    }

    @Override
    public String toString() {
        return "TestDataRecord{" + values + '}';
    }
}
