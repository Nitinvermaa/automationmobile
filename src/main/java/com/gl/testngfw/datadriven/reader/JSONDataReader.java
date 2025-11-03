package com.gl.testngfw.datadriven.reader;

import com.gl.testngfw.datadriven.model.DataContainer;
import com.gl.testngfw.datadriven.model.TestDataRecord;
import com.gl.testngfw.datadriven.model.Values;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.setup.InitializerScript;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Specify a way to read test data saved in Json documents
 */
public class JSONDataReader extends TestDataReader {

    public static final String JSON_RESOLUTION = ".js";

    @Override
    protected String getResolution() {
        return JSON_RESOLUTION;
    }

    @Override
    public <T extends InitializerScript> DataContainer readTestData(URL testDataUrl) throws org.json.simple.parser.ParseException {
        DataContainer result = new DataContainer();

        JSONParser parser = new JSONParser();
        try {
            URI uri = testDataUrl.toURI();
            File file = new File(uri);
            FileReader fileReader = new FileReader(file);
            JSONArray jsonArray = (JSONArray) parser.parse(fileReader);
            for (Object arrayObject : jsonArray) {
                JSONObject jsonObject = (JSONObject) arrayObject;
                TestDataRecord record = new TestDataRecord();
                for (Object key : jsonObject.keySet()) {
                    record.addValue(new Values(String.valueOf(key), jsonObject.get(key)));
                }
                result.addTestData(record);
            }
        } catch (IOException e) {
            FrameworkLogger.logError(e);
        } catch (URISyntaxException e) {
            FrameworkLogger.logError(e);
        }

        return result;
    }
}
