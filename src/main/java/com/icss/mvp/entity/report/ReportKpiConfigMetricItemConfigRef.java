package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.supports.entity.IEntity;

import java.io.Serializable;

@TableName(value="report_kpi_config_ref_mictrics_item_config")
public class ReportKpiConfigMetricItemConfigRef implements IEntity {

    @TableField(value = "report_kpi_config_id")
    private Integer reportKpiConfigId;

    @TableField(value = "mictrics_item_config_id")
    private Integer mictricsItemConfigId;

    public Integer getReportKpiConfigId() {
        return reportKpiConfigId;
    }

    public void setReportKpiConfigId(Integer reportKpiConfigId) {
        this.reportKpiConfigId = reportKpiConfigId;
    }

    public Integer getMictricsItemConfigId() {
        return mictricsItemConfigId;
    }

    public void setMictricsItemConfigId(Integer mictricsItemConfigId) {
        this.mictricsItemConfigId = mictricsItemConfigId;
    }

    @Override
    public String toString() {
        return "ReportKpiConfigMetricItemConfigRef{" +
                "reportKpiConfigId=" + reportKpiConfigId +
                ", mictricsItemConfigId=" + mictricsItemConfigId +
                '}';
    }

    @Override
    public Serializable getId() {
        return null;
    }
}
