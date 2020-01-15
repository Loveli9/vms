package com.icss.mvp.entity.report;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.icss.mvp.supports.entity.IEntity;

import java.util.Date;
import java.util.List;

/**
 * @NAME: 度量表行数据
 * @Author: wwx550362
 * @Date: 2019/12/10 14:46
 * @Version 1.0
 */
@TableName(value = "metrics_row")
public class MetricsRow implements IEntity {
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("period")
    private String period;

    @TableField("period_id")
    private String periodId;


    @TableField("period_name")
    private String periodName;

    @TableField("period_start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date periodStartDate;

    @TableField(value = "period_end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date periodEndDate;
    //工号或任务编号（根据报表类型决定）
    @TableField("code")
    private String code;

    //姓名或任务名（根据报表类型决定）
    @TableField("name")
    private String name;

    @TableField("metrics_Table_Id")
    private Integer metricsTableId;
    @TableField(exist = false)
    private List<MetricsItem> metricsItems;

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public List<MetricsItem> getMetricsItems() {
        return metricsItems;
    }

    public void setMetricsItems(List<MetricsItem> metricsItems) {
        this.metricsItems = metricsItems;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public Integer getMetricsTableId() {
        return metricsTableId;
    }

    public void setMetricsTableId(Integer metricsTableId) {
        this.metricsTableId = metricsTableId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(Date periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public Date getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(Date periodEndDate) {
        this.periodEndDate = periodEndDate;
    }
}
