package com.icss.mvp.entity;

import com.icss.mvp.util.UUIDUtil;

import java.util.Date;

public class IterationMeasureIndex {
    private String id;

    private String iterationId;

    private String measureId;

    private String value;

    private Date createTime;
    
    private String unit;
    
    private String name;

    public IterationMeasureIndex() {
    }

    public IterationMeasureIndex(String iterationId, String measureId, String value) {
        this.id = UUIDUtil.getNew();
        this.iterationId = iterationId;
        this.measureId = measureId;
        this.value = value;
        this.createTime = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getIterationId() {
        return iterationId;
    }

    public void setIterationId(String iterationId) {
        this.iterationId = iterationId == null ? null : iterationId.trim();
    }

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId == null ? null : measureId.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}