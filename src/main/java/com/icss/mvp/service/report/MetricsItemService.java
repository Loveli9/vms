package com.icss.mvp.service.report;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.report.MetricsItemDao;
import com.icss.mvp.entity.report.MetricsItem;
import com.icss.mvp.entity.report.MetricsItemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author chenjiabin
 * @createtime 2019-12-11
 */
@Service
@Transactional
public class MetricsItemService extends ServiceImpl<MetricsItemDao, MetricsItem> implements IService<MetricsItem> {

    @Autowired
    private MetricsItemConfigService metricsItemConfigService;

    public Integer deleteByMetricesRowId(Serializable metricsRowConfigId) {
        QueryWrapper<MetricsItem> wrapper = Wrappers.query();
        wrapper.eq("metrics_row_id", metricsRowConfigId);
        return getBaseMapper().delete(wrapper);
    }


    public List<MetricsItem> getByMetricesRowId(Integer metricsRowId) {
        QueryWrapper<MetricsItem> query = Wrappers.query();
        query.eq("metrics_row_id", metricsRowId);
        List<MetricsItem> result = list(query);
        if (result != null && !result.isEmpty()) {
            List<Integer> ids = new ArrayList<>();
            result.forEach(mi -> {
                ids.add(mi.getMetricsItemConfigId());
            });
            Map<Integer, MetricsItemConfig> mics = metricsItemConfigService.listByIdsToMap(ids);
            result.forEach(mi -> {
                mi.setMetricsItemConfig(mics.get(mi.getMetricsItemConfigId()));
            });
        }
        return result;
    }

    public void clearMetricsItem(List<MetricsItem> olds, List<MetricsItem> news) {
        for (MetricsItem metricsItem : olds) {
            boolean exists = exists(metricsItem.getId(), news);
            if (!exists) {
                removeById(metricsItem.getId());
            }
        }
    }

    private boolean exists(Integer id, List<MetricsItem> news) {
        for (MetricsItem MetricsItem : news) {
            if (MetricsItem.getId() != null && MetricsItem.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
    *@description:查询与度量id不匹配的度量项
    *@author:xhy
    *@create:2019/12/18 15:15
    **/
    public List<MetricsItem> queryExcluedMictricsItemsByIds(String ids) {
        QueryWrapper<MetricsItem> query = Wrappers.query();
        query.notIn("id", ids.split(","));
        List<MetricsItem> result = list(query);
        return result;
    }

}
