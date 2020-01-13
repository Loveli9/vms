package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.supports.entity.IEntity;

import java.util.List;

@TableName(value = "report_config")
public class ReportConfig implements IEntity {

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    //报表名称
    @TableField
    private String name;
    //类型：1=项目，2=需求，3=人员
    @TableField
    private String type;
    //周期：项目，迭代
    @TableField
    private String period;
    //是否平铺（列自动分配）
    @TableField(value = "force_fit")
    private Boolean forceFit = false;
    //报表描述
    @TableField
    private String description;
    //关联项目ID
    @TableField(value = "project_id")
    private String projectId;

    //关联KPI引用
    @TableField(exist = false)
    private List<ReportKpiConfigRef> kpiConfigRefs;

    //关联KPI引用
    @TableField(exist = false)
    private List<ChartConfig> charts;

    public List<ChartConfig> getCharts() {
        return charts;
    }

    public void setCharts(List<ChartConfig> charts) {
        this.charts = charts;
    }

    public List<ReportKpiConfigRef> getKpiConfigRefs() {
        return kpiConfigRefs;
    }

    public void setKpiConfigRefs(List<ReportKpiConfigRef> kpiConfigRefs) {
        this.kpiConfigRefs = kpiConfigRefs;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeriod() {
        return period;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getForceFit() {
        return forceFit;
    }

    public void setForceFit(Boolean forceFit) {
        this.forceFit = forceFit;
    }

    public String getDescription() {
        return description;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "ReportConfig{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", period='" + period + '\'' +
                ", forceFit=" + forceFit +
                ", description='" + description + '\'' +
                ", projectId='" + projectId + '\'' +
                '}';
    }
}
