package com.icss.mvp.service.datacollection.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.datacollection.config.ICollectionGroupDao;
import com.icss.mvp.entity.datacollection.config.CollectionField;
import com.icss.mvp.entity.datacollection.config.CollectionGroup;
import com.icss.mvp.entity.report.MetricsTableConfig;
import com.icss.mvp.service.report.MetricsTableConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CollectionGroupService extends ServiceImpl<ICollectionGroupDao, CollectionGroup> implements IService<CollectionGroup> {
    @Autowired
    private CollectionFieldService collectionFieldService;
    @Autowired
    private MetricsTableConfigService metricsTableConfigService;

    public List<CollectionGroup> listFullByCollectionConfigId(Integer collectionConfigId) {
        QueryWrapper wrapper = Wrappers.query().eq("collection_config_id", collectionConfigId);
        List<CollectionGroup> groups = list(wrapper);
        if (groups != null && !groups.isEmpty()) {
            for (CollectionGroup group : groups) {
                List<CollectionField> collectionFields = collectionFieldService.listFullByCollectionGroupId(group.getId());
                group.setCollectionFields(collectionFields);
                MetricsTableConfig metricsTableConfig = metricsTableConfigService.getById(group.getMetricsTableConfigId());
                if (metricsTableConfig != null) {
                    group.setMetricsTableConfig(metricsTableConfig);
                }
            }
        }
        return groups;
    }
}
