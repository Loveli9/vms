package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.supports.entity.IEntity;

@TableName(value = "report_kpi")
public class ReportKpi implements IEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //字段名称
    @TableField(value = "report_kpi_config_id")
    private Integer reportKpiConfigId;
    //指标对应的值
    @TableField()
    private String value;
    //关联报表ID
    @TableField(value = "report_row_id")
    private Integer reportRowId;
    //点灯状态，false=不正常，true=正常
    @TableField()
    private Boolean status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getValue() {
        return value;
    }

    public Integer getReportKpiConfigId() {
        return reportKpiConfigId;
    }

    public void setReportKpiConfigId(Integer reportKpiConfigId) {
        this.reportKpiConfigId = reportKpiConfigId;
    }

    public Integer getReportRowId() {
        return reportRowId;
    }

    public void setReportRowId(Integer reportRowId) {
        this.reportRowId = reportRowId;
    }

    @Override
    public String toString() {
        return "ReportKpi{" +
                "id=" + id +
                ", reportKpiConfigId=" + reportKpiConfigId +
                ", value='" + value + '\'' +
                ", reportRowId=" + reportRowId +
                ", status=" + status +
                '}';
    }
}
