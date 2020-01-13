package com.icss.mvp.entity;/**
 * Created by chengchenhui on 2019/7/5.
 */

import java.util.Date;

/**
 * @author chengchenhui
 * @title: MeasureConfigRecord
 * @projectName mvp
 * @description: TODO
 * @date 2019/7/57:21
 */
public class MeasureConfigRecord {


    private String id;

    private String no;

    private String lableId;

    private String measureIds;

    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getLableId() {
        return lableId;
    }

    public void setLableId(String lableId) {
        this.lableId = lableId;
    }

    public String getMeasureIds() {
        return measureIds;
    }

    public void setMeasureIds(String measureIds) {
        this.measureIds = measureIds;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
