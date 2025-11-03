package com.gl.testngfw.report.model;

import java.io.Serializable;

public class Log implements Serializable {

    /**
     * Auto generate primary key while inserting data
     */
    private String logId;

    /**
     * Foreign key - primary key of Result table
     */
    private String resultId;

    /**
     * For rest calls, this would be base64 encoded string of the image, for
     * persisting this would be file location where image is saved on the local
     * drive
     */
    private String file;

    private String type;

    public Log() {
        super();
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
