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
@TableName(value = "report_config_excluded")
public class ReportConfigExcluded implements IEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //关联报表配置ID
    @TableField(value = "report_config_id")
    private Integer reportConfigId;
    //关联KPI配置ID
    @TableField(value = "report_kpi_config_id")
    private Integer reportKpiConfigId;
    //关联项目ID
    @TableField(value = "project_id")
    private String projectId;
    //关联项目ID
    @TableField(value = "description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReportKpiConfigId() {
        return reportKpiConfigId;
    }

    public void setReportKpiConfigId(Integer reportKpiConfigId) {
        this.reportKpiConfigId = reportKpiConfigId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getReportConfigId() {
        return reportConfigId;
    }

    public void setReportConfigId(Integer reportConfigId) {
        this.reportConfigId = reportConfigId;
    }
}