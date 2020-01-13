package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.icss.mvp.supports.entity.IEntity;

public class ReportCute implements IEntity {
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    //项目编号
    @TableField("project_id")
    private String projectId;
    //裁剪掉的报表指标配置
    @TableField("report_kpi_config_id")
    private Integer reportKpiConfigId;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getReportKpiConfigId() {
        return reportKpiConfigId;
    }

    public void setReportKpiConfigId(Integer reportKpiConfigId) {
        this.reportKpiConfigId = reportKpiConfigId;
    }
}
