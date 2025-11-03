package com.gl.testngfw.logging;

import org.apache.log4j.PatternLayout;

import java.util.Map;

/**
 * Custom Pattern layout class allows to output events into a txt document,
 * adjust content of each log record,
 * add additional header information
 */
class CustomPatternLayout extends PatternLayout {

    private Map<String, String> headerData;

    public CustomPatternLayout(final String pattern, final Map<String, String> headerData) {
        super(pattern);
        this.headerData = headerData;
    }

    @Override
    public String getHeader() {

        StringBuilder builder = new StringBuilder();
        for (String key : headerData.keySet()) {
            builder.append(key).append(": ");
            builder.append(headerData.get(key));
            builder.append(System.getProperty("line.separator"));
        }
        builder.append("************************************************************************");
        builder.append(System.getProperty("line.separator"));
        return builder.toString();
    }
}