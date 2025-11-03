package com.gl.testngfw.report.model;


import java.io.Serializable;
import java.util.List;

public class Report implements Serializable {
    private List<Reports> reportsList;

    public List<Reports> getReportsList() {
        return reportsList;
    }

    public void setReportsList(List<Reports> reportsList) {
        this.reportsList = reportsList;
    }
}
