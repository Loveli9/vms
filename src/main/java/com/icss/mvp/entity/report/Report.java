package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.supports.entity.IEntity;

import java.util.List;

/**
 * 报表实体：一个报表包括多行，每行包含多个KPI（可以看作列）
 */
@TableName(value = "report")
public class Report implements IEntity {
    public static final String TYPE_PROJECT = "1";
    public static final String TYPE_DEMAND = "2";
    public static final String TYPE_PERSIONNEL = "3";
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    //报表类型：项目，人员，需求
    @TableField(value = "type")
    private String type;
    //项目ID
    private String projectId;
    //项目名称
    @TableField(value = "project_name")
    private String projectName;
    //关联报表ID
    @TableField(value = "report_config_id")
    private String reportConfigId;

    @TableField()
    private String reportName;

    //关联报表行列表
    @TableField(exist = false)
    private List<ReportRow> rows;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public List<ReportRow> getRows() {
        return rows;
    }

    public void setRows(List<ReportRow> rows) {
        this.rows = rows;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getReportConfigId() {
        return reportConfigId;
    }

    public void setReportConfigId(String reportConfigId) {
        this.reportConfigId = reportConfigId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", projectId='" + projectId + '\'' +
                ", reportConfigId='" + reportConfigId + '\'' +
                '}';
    }
}
