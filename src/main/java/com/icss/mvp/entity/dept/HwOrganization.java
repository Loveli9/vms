package com.icss.mvp.entity.dept;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by chengchenhui on 2019/12/12.
 */

public class HwOrganization {
    /**
     * <pre>
     *    "deptId": 1017,
     *    "deptName": "中央软件院",
     *    "deptLevel": 1,
     *    "parentId": 23,
     *    "parentName": null,
     *    "groudParentId": -9,
     *    "operateTime": -2209017600000,
     *    "ignoe": 0,
     *    "operateUser": "10000",
     *    "value": "1017",
     *    "text": "中央软件院"
     * </pre>
     */

    private String deptName;
    private String parentName;
    private int deptLevel;
    private int groudParentId;


    private long operateTime;

    private int deptId;
    private String operateUser;
    private String text;
    private String value;
    private int parentId;
    private int ignoe;

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setDeptLevel(int deptLevel) {
        this.deptLevel = deptLevel;
    }

    public void setGroudParentId(int groudParentId) {
        this.groudParentId = groudParentId;
    }

    public void setOperateTime(long operateTime) {
        this.operateTime = operateTime;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setIgnoe(int ignoe) {
        this.ignoe = ignoe;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getParentName() {
        return parentName;
    }

    public int getDeptLevel() {
        return deptLevel;
    }

    public int getGroudParentId() {
        return groudParentId;
    }

    public long getOperateTime() {
        return operateTime;
    }

    public int getDeptId() {
        return deptId;
    }

    public String getOperateUser() {
        return operateUser;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    public int getParentId() {
        return parentId;
    }

    public int getIgnoe() {
        return ignoe;
    }



}
