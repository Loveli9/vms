package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.supports.entity.IEntity;

import java.io.Serializable;

/**
 * 报表审核对应指标
 */
@TableName(value = "report_check_kpi")
public class ReportCheckKpi implements IEntity {
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    @TableField(value = "report_id")
    private Integer reportId;
    @TableField(value = "report_name")
    private String reportName;
    @TableField(value = "report_row_id")
    private Integer reportRowId;
    @TableField(value = "report_kpi_id")
    private Integer reportKpiId;
    @TableField(value = "report_kpi_name")
    private String reportKpiName;
    @TableField
    private String value;
    @TableField(value = "status")
    private Boolean status;
    @TableField
    private String description;
    @TableField(value = "report_check_id")
    private Integer reportCheckId;

    public Integer getReportCheckId() {
        return reportCheckId;
    }

    public void setReportCheckId(Integer reportCheckId) {
        this.reportCheckId = reportCheckId;
    }

    @Override
    public Serializable getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Integer getReportRowId() {
        return reportRowId;
    }

    public void setReportRowId(Integer reportRowId) {
        this.reportRowId = reportRowId;
    }

    public Integer getReportKpiId() {
        return reportKpiId;
    }

    public void setReportKpiId(Integer reportKpiId) {
        this.reportKpiId = reportKpiId;
    }

    public String getReportKpiName() {
        return reportKpiName;
    }

    public void setReportKpiName(String reportKpiName) {
        this.reportKpiName = reportKpiName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
