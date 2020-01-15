package com.icss.mvp.service.datacollection.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.datacollection.config.ICollectionConfigDao;
import com.icss.mvp.entity.datacollection.config.CollectionConfig;
import com.icss.mvp.entity.datacollection.config.CollectionField;
import com.icss.mvp.entity.datacollection.config.CollectionGroup;
import com.icss.mvp.entity.report.MetricsItemConfig;
import com.icss.mvp.entity.report.MetricsTableConfig;
import com.icss.mvp.entity.report.ReportConfig;
import com.icss.mvp.service.report.MetricsItemConfigService;
import com.icss.mvp.service.report.MetricsTableConfigService;
import com.icss.mvp.service.report.ReportConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class CollectionConfigService extends ServiceImpl<ICollectionConfigDao, CollectionConfig> implements IService<CollectionConfig> {
    @Autowired
    private CollectionGroupService collectionGroupService;
    @Autowired
    private ReportConfigService reportConfigService;
    @Autowired
    private MetricsItemConfigService metricsItemConfigService;
    @Autowired
    private MetricsTableConfigService metricsTableConfigService;

    //获取或创建一个完整的配置实例
    public CollectionConfig getOrCreateFullByProjectId(String projectId) {
        QueryWrapper wrapper = Wrappers.query().eq("project_id", projectId);
        CollectionConfig collectionConfig = getOne(wrapper);
        if (collectionConfig == null) {
            collectionConfig = this.createFull(projectId);
            return collectionConfig;
        }
        List<CollectionGroup> groups = collectionGroupService.listFullByCollectionConfigId(collectionConfig.getId());
        collectionConfig.setCollectionGroups(groups);
        return collectionConfig;
    }

    private CollectionConfig createFull(String projectId) {
        CollectionConfig collectionConfig = new CollectionConfig();
        collectionConfig.setProjectId(projectId);
        List<MetricsItemConfig> metricsItemConfigs = metricsItemConfigService.queryNeedCollectByProjectId(projectId);
        //添加缺失采集字段(新建采集配置会添加所有采集字段)
        addCollectionFields(collectionConfig, metricsItemConfigs);
        return collectionConfig;
    }

    //根据当前项目必须的数据源字段，添加缺失采集字段(只处理虚拟表源数据字段)
    private void addCollectionFields(CollectionConfig collectionConfig, List<MetricsItemConfig> metricsItemConfigs) {
        for (MetricsItemConfig metricsItemConfig : metricsItemConfigs) {
            MetricsTableConfig metricsTableConfig = metricsTableConfigService.getFullById(metricsItemConfig.getMetricsTableConfigId());
            if (metricsTableConfig == null || metricsTableConfig.getVirtualTable()) {
                continue;
            }
            CollectionGroup collectionGroup = new CollectionGroup();
            collectionGroup.setMetricsTableConfig(metricsTableConfig);
            collectionConfig.getCollectionGroups().add(collectionGroup);
            //创建采集字段并添加到分组中
            CollectionField collectionField = new CollectionField();
            collectionField.setMetricsItemConfig(metricsItemConfig);
            collectionGroup.getCollectionFields().add(collectionField);
        }
    }

    /**
     * 根据数据源表在采集配置中查找采集分组
     *
     * @param collectionConfig
     * @param metricsTableConfig
     * @return
     */
    private CollectionGroup findCollectionGroup(CollectionConfig collectionConfig, MetricsTableConfig metricsTableConfig) {
        List<CollectionGroup> collectionGroups = collectionConfig.getCollectionGroups();
        if (collectionGroups.isEmpty()){
            return null;
        }
        for (int i = 0; i < collectionGroups.size(); i++) {
            CollectionGroup collectionGroup = collectionGroups.get(i);
            if (collectionGroup.getMetricsTableConfig().getId().equals(metricsTableConfig.getId())) {
                return collectionGroup;
            }
        }
        return null;
    }

    private boolean findCollectionField(CollectionGroup collectionGroup, MetricsItemConfig metricsItemConfig) {
        for (CollectionField collectionField : collectionGroup.getCollectionFields()) {
            if (collectionField.getMetricsItemConfig().getId().equals(metricsItemConfig.getId())) {
                return true;
            }
        }
        return false;
    }
}