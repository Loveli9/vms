package com.icss.mvp.entity.datacollection.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.entity.report.MetricsItemConfig;
import com.icss.mvp.supports.entity.IEntity;

@TableName(value = "collection_field")
public class CollectionField implements IEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //采集源：如DTS，CI，...
    private String collectionTaskId;


    //数据录入类型：1=手工录入，2=自动收集，3=手工&自动
    private Integer inputType = 1;

    //采集结果中的属性名称
    private String propertyName;

    private Integer metricsItemConfigId;

    @TableField(exist = false)
    private MetricsItemConfig metricsItemConfig;


    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCollectionTaskId() {
        return collectionTaskId;
    }

    public void setCollectionTaskId(String collectionTaskId) {
        this.collectionTaskId = collectionTaskId;
    }

    public Integer getInputType() {
        return inputType;
    }

    public void setInputType(Integer inputType) {
        this.inputType = inputType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Integer getMetricsItemConfigId() {
        return metricsItemConfigId;
    }

    public void setMetricsItemConfigId(Integer metricsItemConfigId) {
        this.metricsItemConfigId = metricsItemConfigId;
    }

    public MetricsItemConfig getMetricsItemConfig() {
        return metricsItemConfig;
    }

    public void setMetricsItemConfig(MetricsItemConfig metricsItemConfig) {
        this.metricsItemConfig = metricsItemConfig;
    }
}