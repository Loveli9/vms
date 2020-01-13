package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.supports.entity.IEntity;

/**
 * @program: wmp
 * @description:
 * @author: Chenjiabin
 * @create: 2018-04-22 20:29
 **/
@TableName(value = "report_kpi_config_reference")
public class ReportKpiConfigRef implements IEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField
    private Boolean hidden;
    //是否是自动计算，自动计算时不可编辑
    @TableField(value = "auto_calcu")
    private Boolean readOnly;
    //亮灯规则相关
    @TableField(value = "max_value")
    private Double maxValue;
    @TableField(value = "min_value")
    private Double minValue;
    @TableField(value = "target_value")
    private Double targetValue;
    //上下限优先
    @TableField(value = "light_up_rule")
    private String lightUpRule;
    @TableField
    private Integer idx = 0;
    //关联KPI配置ID
    @TableField(value = "report_kpi_config_id")
    private Integer reportKpiConfigId;
    //关联报表配置ID
    @TableField(value = "report_config_id")
    private Integer reportConfigId;
    //关联KPI配置name
    @TableField(exist = false)
    private String kpiName;
    //关联KPI配置name
    @TableField(exist = false)
    private ReportKpiConfig reportKpiConfig;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public String getLightUpRule() {
        return lightUpRule;
    }

    public void setLightUpRule(String lightUpRule) {
        this.lightUpRule = lightUpRule;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public Integer getReportKpiConfigId() {
        return reportKpiConfigId;
    }

    public void setReportKpiConfigId(Integer reportKpiConfigId) {
        this.reportKpiConfigId = reportKpiConfigId;
    }

    public Integer getReportConfigId() {
        return reportConfigId;
    }

    public void setReportConfigId(Integer reportConfigId) {
        this.reportConfigId = reportConfigId;
    }

    public String getKpiName() {
        return kpiName;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public void setReportKpiConfig(ReportKpiConfig reportKpiConfig) {
        this.reportKpiConfig = reportKpiConfig;
    }

    public ReportKpiConfig getReportKpiConfig() {
        return reportKpiConfig;
    }

    @Override
    public String toString() {
        return "ReportKpiConfigRef{" +
                "id=" + id +
                ", hidden=" + hidden +
                ", readOnly=" + readOnly +
                ", maxValue=" + maxValue +
                ", minValue=" + minValue +
                ", targetValue=" + targetValue +
                ", lightUpRule='" + lightUpRule + '\'' +
                ", idx=" + idx +
                ", reportKpiConfigId=" + reportKpiConfigId +
                ", reportConfigId=" + reportConfigId +
                '}';
    }

}
