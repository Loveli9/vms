package com.icss.mvp.entity.datacollection.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.entity.report.MetricsTableConfig;
import com.icss.mvp.supports.entity.IEntity;

import java.util.ArrayList;
import java.util.List;

@TableName(value = "collection_group")
public class CollectionGroup implements IEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer metricsTableConfigId;

    @TableField(exist = false)
    private MetricsTableConfig metricsTableConfig;

    @TableField(exist = false)
    private List<CollectionField> collectionFields = new ArrayList<CollectionField>(0);


    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMetricsTableConfigId() {
        return metricsTableConfigId;
    }

    public void setMetricsTableConfigId(Integer metricsTableConfigId) {
        this.metricsTableConfigId = metricsTableConfigId;
    }

    public MetricsTableConfig getMetricsTableConfig() {
        return metricsTableConfig;
    }

    public void setMetricsTableConfig(MetricsTableConfig metricsTableConfig) {
        this.metricsTableConfig = metricsTableConfig;
    }

    public List<CollectionField> getCollectionFields() {
        return collectionFields;
    }

    public void setCollectionFields(List<CollectionField> collectionFields) {
        this.collectionFields = collectionFields;
    }
}