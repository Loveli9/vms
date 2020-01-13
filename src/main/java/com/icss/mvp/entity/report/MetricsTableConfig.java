package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.icss.mvp.supports.entity.IEntity;

import java.beans.Transient;
import java.util.List;

/**
 * @Name: 度量表配置
 * @Author: wwx550362
 * @Date: 2019/12/10 15:02
 * @Version 1.0
 */
public class MetricsTableConfig implements IEntity {
    public final static String PERIOD_PROJECT = "项目内";
    public final static String PERIOD_ITERATION = "迭代内";
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("table_name")
    private String tableName;

    private String alias;

    //数据类型（项目、人员、需求）
    private String type;

    //统计周期（迭代、项目）
    private String period;

    private String description;

    @TableField("virtual_table")
    private Boolean virtualTable = false;

    @TableField(exist = false)
    private List<MetricsItemConfig> metricsItemConfigs;

    public void setMetricsItemConfigs(List<MetricsItemConfig> metricsItemConfigs) {
        this.metricsItemConfigs = metricsItemConfigs;
    }

    @Transient
    public List<MetricsItemConfig> getMetricsItemConfigs() {
        return metricsItemConfigs;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getVirtualTable() {
        return virtualTable;
    }

    public void setVirtualTable(Boolean virtualTable) {
        this.virtualTable = virtualTable;
    }
}
