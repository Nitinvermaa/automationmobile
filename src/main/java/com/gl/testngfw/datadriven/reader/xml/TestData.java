package com.gl.testngfw.datadriven.reader.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A model for testdata tag in xml test data for data driven tests
 */
@XmlRootElement(name = "testdata")
public class TestData {

    private List<Record> records;

    @XmlElement(name = "record")
    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
