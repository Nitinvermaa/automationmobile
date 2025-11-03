package com.gl.testngfw.report.model;

import java.io.Serializable;

public class Screenshot implements Serializable {

    /**
     * Auto generate primary key while inserting data
     */
    private String screenShotId;

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

    public Screenshot() {
        super();
    }

    public String getScreenShotId() {
        return screenShotId;
    }

    public void setScreenShotId(String screenShotId) {
        this.screenShotId = screenShotId;
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

}
