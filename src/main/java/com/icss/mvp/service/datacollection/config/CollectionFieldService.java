package com.icss.mvp.service.datacollection.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.datacollection.config.ICollectionFieldDao;
import com.icss.mvp.entity.datacollection.config.CollectionField;
import com.icss.mvp.entity.datacollection.config.CollectionGroup;
import com.icss.mvp.entity.report.MetricsItemConfig;
import com.icss.mvp.service.report.MetricsItemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CollectionFieldService extends ServiceImpl<ICollectionFieldDao, CollectionField> implements IService<CollectionField> {

    @Autowired
    private MetricsItemConfigService metricsItemConfigService;

    public List<CollectionField> listFullByCollectionGroupId(Integer collectionGroupId) {
        QueryWrapper wrapper = Wrappers.query().eq("collection_group_id", collectionGroupId);
        List<CollectionField> fields = list(wrapper);
        for (CollectionField collectionField : fields){
            Integer metricsItemConfigId = collectionField.getMetricsItemConfigId();
            MetricsItemConfig metricsItemConfig = metricsItemConfigService.getById(metricsItemConfigId);
            if (metricsItemConfig != null) {
                collectionField.setMetricsItemConfig(metricsItemConfig);
            }
        }
        return fields;
    }
}
