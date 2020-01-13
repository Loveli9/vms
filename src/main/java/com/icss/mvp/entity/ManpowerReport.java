package com.icss.mvp.entity;

/**
 * 人员统计
 */
public class ManpowerReport {
    private String name;
    private int competenceNum;
    private int workingNum;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompetenceNum() {
        return competenceNum;
    }

    public void setCompetenceNum(int competenceNum) {
        this.competenceNum = competenceNum;
    }

    public int getWorkingNum() {
        return workingNum;
    }

    public void setWorkingNum(int workingNum) {
        this.workingNum = workingNum;
    }

}
