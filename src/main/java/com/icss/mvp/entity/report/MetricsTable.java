package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.ArrayList;
import java.util.List;

/**
 * @NAME: 度量表
 * @Author: wwx550362
 * @Date: 2019/12/10 14:46
 * @Version 1.0
 */
@TableName(value = "metrics_table")
public class MetricsTable {
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("table_name")
    private String tableName;

    @TableField("type")
    private String type;

    @TableField("project_id")
    private String projectId;

    @TableField("metrics_table_config_id")
    private Integer metricsTableConfigId;

    @TableField(exist = false)
    private List<MetricsRow> metricsRows;

    public List<MetricsRow> getMetricsRows() {
        return metricsRows;
    }

    public void setMetricsRows(List<MetricsRow> metricsRows) {
        this.metricsRows = metricsRows;
    }

    public Integer getMetricsTableConfigId() {
        return metricsTableConfigId;
    }

    public void setMetricsTableConfigId(Integer metricsTableConfigId) {
        this.metricsTableConfigId = metricsTableConfigId;
    }

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
