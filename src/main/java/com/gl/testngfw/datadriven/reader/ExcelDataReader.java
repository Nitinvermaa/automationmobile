package com.gl.testngfw.datadriven.reader;

import com.gl.testngfw.datadriven.model.DataContainer;
import com.gl.testngfw.datadriven.model.TestDataRecord;
import com.gl.testngfw.datadriven.model.Values;
import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.setup.InitializerScript;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Specify a way to read test data saved in Excel documents
 */
public class ExcelDataReader extends TestDataReader {

    public static final String EXCEL_RESOLUTION = ".xlsx";

    @Override
    protected String getResolution() {
        return EXCEL_RESOLUTION;
    }

    @Override
    public <T extends InitializerScript> DataContainer readTestData(URL testDataUrl) {

        DataContainer result = new DataContainer();

        FileInputStream fileInputStream = null;
        try {
            URI uri = testDataUrl.toURI();
            File file = new File(uri);
            fileInputStream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            List<String> headers = null;
            int rowsNum = sheet.getPhysicalNumberOfRows();
            for (int i = 0; i < rowsNum; i++) {
                XSSFRow row = sheet.getRow(i);
                int cellsNum = row.getLastCellNum();
                if (i == 0) {
                    headers = processHeaderRow(row, cellsNum);
                } else {
                    result.addTestData(processDataRow(row, headers));
                }

            }
        } catch (IOException | URISyntaxException e) {
            FrameworkLogger.logError(e);
        }
        return result;
    }

    private List<String> processHeaderRow(XSSFRow row, int cellsNum) {
        List<String> result = new ArrayList<String>(cellsNum);

        for (int i = 0; i < cellsNum; i++) {
            XSSFCell cell = row.getCell(i);
            result.add(cell != null ? cell.getStringCellValue() : "");
        }
        return result;
    }

    private TestDataRecord processDataRow(XSSFRow row, List<String> headers) {
        TestDataRecord result = new TestDataRecord();
        for (int i = 0; i < headers.size(); i++) {
            XSSFCell cell = row.getCell(i);
            if (cell != null) {
                switch (cell.getCellType().ordinal()) {
                    case 1:
                        cell.getStringCellValue();
                        result.addValue(new Values(headers.get(i), cell.getStringCellValue()));
                        break;
                    case 0:
                        cell.getNumericCellValue();
                        result.addValue(new Values(headers.get(i), cell.getNumericCellValue()));
                        break;
                    case 4:
                        cell.getBooleanCellValue();
                        result.addValue(new Values(headers.get(i), cell.getBooleanCellValue()));
                        break;
                    default:
                }
            } else {
                result.addValue(new Values(headers.get(i), null));
            }
        }

        return result;
    }
}
