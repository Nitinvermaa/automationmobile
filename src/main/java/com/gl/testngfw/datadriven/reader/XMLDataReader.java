package com.gl.testngfw.datadriven.reader;

import com.gl.testngfw.datadriven.model.DataContainer;
import com.gl.testngfw.datadriven.model.TestDataRecord;
import com.gl.testngfw.datadriven.model.Values;
import com.gl.testngfw.datadriven.reader.xml.Element;
import com.gl.testngfw.datadriven.reader.xml.Record;
import com.gl.testngfw.datadriven.reader.xml.TestData;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.setup.InitializerScript;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Specify a way to read test data saved in Xml documents
 */
public class XMLDataReader extends TestDataReader {

    public static final String XML_RESOLUTION = ".xml";

    @Override
    protected String getResolution() {
        return XML_RESOLUTION;
    }

    @Override
    public <T extends InitializerScript> DataContainer readTestData(URL testDataUrl) {
        DataContainer result = new DataContainer();

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TestData.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            URI uri = testDataUrl.toURI();
            File file = new File(uri);
            TestData testData = (TestData) unmarshaller.unmarshal(file);
            List<Record> records = testData.getRecords();
            for (Record record : records) {
                TestDataRecord dataRecord = new TestDataRecord();
                List<Element> elements = record.getElements();
                for (Element element : elements) {
                    dataRecord.addValue(new Values(element.getName(), element.getValue()));
                }
                result.addTestData(dataRecord);
            }
        } catch (JAXBException | URISyntaxException e) {
            FrameworkLogger.logError(e);
        }
        return result;
    }
}
