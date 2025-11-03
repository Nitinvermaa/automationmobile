package com.gl.testngfw.datadriven.model;

/**
 * Specifies a model for values of test data records
 */
public class Values {

    private String name;
    private Object value;

    /**
     * Creates an instance of {@link
     *
     * @param name  a name of value
     * @param value a value
     */
    public Values(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns a name of the value
     *
     * @return {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a name of value
     *
     * @param name a name of value
     */
    public void setName(String name) {
        this.name = name;
    }

    public <T> T getValue() {
        return value != null ? (T) value : null;
    }

    /**
     * Sets a value
     *
     * @param value a value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Returns a string value
     *
     * @return {@link String}
     */
    public String getStringValue() {
        return value != null ? this.<String>getValue() : null;
    }

    /**
     * Returns a double value
     *
     * @return {@link Double}
     */
    public Double getNumericValue() {
        return value != null ? this.<Double>getValue() : null;
    }

    /**
     * Returns an integer value
     *
     * @return {@link Integer}
     */
    public Integer getIntegerValue() {
        return value != null ? this.<Integer>getValue() : null;
    }

    /**
     * Returns a long value
     *
     * @return {@link Long}
     */
    public Long getLongValue() {
        return value != null ? this.<Long>getValue() : null;
    }

    /**
     * Returns a boolean value
     *
     * @return {@link Boolean}
     */
    public Boolean getBooleanValue() {
        return value != null ? this.<Boolean>getValue() : null;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", value=" + String.valueOf(value) +
                '}';
    }
}
