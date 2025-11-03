package com.gl.testngfw.datadriven.reader.xml;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * A model for record tag in xml test data for data driven tests
 */
public class Record {

    private List<Element> elements;

    @XmlElement(name = "element")
    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> element) {
        this.elements = element;
    }
}
