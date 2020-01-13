package com.icss.mvp.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目报表
 */
@Deprecated
public class ProjectReport {

    private String              title;
    private String              name;
    private Map<String, String> reportMap;

    public ProjectReport(){
    }

    public ProjectReport(String title, String name){
        this.title = title;
        this.name = name;
        this.reportMap = new HashMap<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getReportMap() {
        return reportMap;
    }

    public void setReportMap(Map<String, String> reportMap) {
        this.reportMap = reportMap;
    }

    // @Override
    // public boolean equals(Object o) {
    // if (this == o) return true;
    // if (o == null || getClass() != o.getClass()) return false;
    // ProjectReport that = (ProjectReport) o;
    // return Objects.equals(title, that.title) &&
    // Objects.equals(name, that.name);
    // }
    //
    // @Override
    // public int hashCode() {
    //
    // return Objects.hash(title, name);
    // }
}
