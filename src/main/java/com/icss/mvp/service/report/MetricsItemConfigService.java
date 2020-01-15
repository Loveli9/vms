package com.icss.mvp.service.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.icss.mvp.dao.report.MetricsItemConfigDao;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.report.MetricsItemConfig;
import com.icss.mvp.entity.report.ReportKpiConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenjiabin
 * @createtime 2019-12-11
 */
@Service
@Transactional
public class MetricsItemConfigService extends ServiceImpl<MetricsItemConfigDao, MetricsItemConfig> implements IService<MetricsItemConfig> {
    @Autowired
    private ReportKpiConfigService reportKpiConfigService;

    public Integer deleteByMetricesTableConfigId(Serializable metricsTableConfigId) {
        QueryWrapper<MetricsItemConfig> wrapper = Wrappers.query();
        wrapper.eq("metrics_table_config_id", metricsTableConfigId);
        return getBaseMapper().delete(wrapper);
    }

    /**
     * 根据度量表配置ID获取所有关联度量项配置
     *
     * @param metricsTableConfigId
     * @return
     */
    public List<MetricsItemConfig> getByMetricesTableConfigId(Integer metricsTableConfigId) {
        QueryWrapper<MetricsItemConfig> query = Wrappers.query();
        query.eq("metrics_table_config_id", metricsTableConfigId).orderByAsc("idx");
        List<MetricsItemConfig> result = list(query);
        return result;
    }

    /**
     * 根据清的度量项配置列表，清理旧的度量项配置列表
     *
     * @param olds
     * @param news
     */
    public PlainResponse<List<MetricsItemConfig>> clearMetricsItemConfigs(List<MetricsItemConfig> olds, List<MetricsItemConfig> news) {
        List<MetricsItemConfig> clears = new ArrayList<>();
        for (MetricsItemConfig metricsItemConfig : olds) {
            boolean exists = exists(metricsItemConfig.getId(), news);
            if (!exists) {
                Integer kpiConfigIdCount = reportKpiConfigService.countByMictricsItemConfigId(metricsItemConfig.getId());
                if (kpiConfigIdCount > 0) {
                    //有项目的指标引用了该度量项配置，不能删除
                    return PlainResponse.fail("此度量项存在指标引用，不能删除。");
                }
                clears.add(metricsItemConfig);
            }
        }
        if (!clears.isEmpty()) {
            List<Integer> ids = clears.stream().map(MetricsItemConfig::getId).collect(Collectors.toList());
            removeByIds(ids);
        }
        return PlainResponse.ok(clears);
    }

    /**
     * 判断一个度量项配置ID在度量项配置列表中是否存在
     *
     * @param id
     * @param news
     * @return
     */
    private boolean exists(Integer id, List<MetricsItemConfig> news) {
        for (MetricsItemConfig metricsItemConfig : news) {
            if (metricsItemConfig.getId() != null && metricsItemConfig.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据度量项配置ID列表获取所有度量项配置
     *
     * @param ids
     * @return
     */
    public Map<Integer, MetricsItemConfig> getByMetricsItemIds(List<Integer> ids) {
        Collection<MetricsItemConfig> mics = this.listByIds(ids);
        Map<Integer, MetricsItemConfig> data = new HashMap<>();
        mics.forEach(metricsItemConfig -> {
            data.put(metricsItemConfig.getId(), metricsItemConfig);
        });
        return data;
    }

    /**
     * 查询与指标关联的数据项配置个数
     *
     * @param reportKpiConfigId
     * @return
     */
    public Integer countReferencedByReportKpiConfigId(Integer reportKpiConfigId) {
        return getBaseMapper().countByReportKpiConfigId(reportKpiConfigId);
    }

    /**
     * 查询与度量项配置关联的指标配置个数
     *
     * @param mictricsItemConfigId
     * @return
     */
    public Integer countByMictricsItemConfigId(Integer mictricsItemConfigId) {
        return getBaseMapper().countByMictricsItemConfigId(mictricsItemConfigId);
    }

    /**
     * 查询与指标关联的数据项配置个数
     *
     * @param reportKpiConfigId
     * @return
     */
    public List<MetricsItemConfig> getByReportKpiConfigId(Integer reportKpiConfigId) {
        return getBaseMapper().getByReportKpiConfigId(reportKpiConfigId);
    }

    public Map<Integer, MetricsItemConfig> listByIdsToMap(List<Integer> ids) {
        Map<Integer, MetricsItemConfig> datas = new HashMap<>();
        Collection<MetricsItemConfig> mics = listByIds(ids);
        mics.forEach(mic -> {
            datas.put(mic.getId(), mic);
        });
        return datas;
    }

    /**
     * 查询与度量id不匹配的度量项
     *
     * @param mictricsItemConfigIds
     * @return
     */
    public TableSplitResult<List<MetricsItemConfig>> queryExcluedMictricsItemsByIds(String mictricsItemConfigIds, PageRequest pageRequest) {
        TableSplitResult<List<MetricsItemConfig>> result = new TableSplitResult<List<MetricsItemConfig>>();

        com.github.pagehelper.Page page = PageHelper.startPage(
                (pageRequest.getPageNumber() == null ? 0 : pageRequest.getPageNumber() - 1) + 1,
                pageRequest.getPageSize());

        QueryWrapper<MetricsItemConfig> query = Wrappers.query();
        if (StringUtils.isNotBlank(mictricsItemConfigIds)) {
            query.notIn("id", mictricsItemConfigIds.split(","));
        }
        List<MetricsItemConfig> data = list(query);
        result.setRows(data);
        result.setTotal((int) page.getTotal());
        result.setPage(pageRequest.getPageNumber());
        return result;
    }

    /**
     * 根据度量项字段别名获取所有关联度量项配置
     *
     * @param fieldAlias
     * @return
     */
    public List<MetricsItemConfig> listByFieldAlias(String fieldAlias) {
        QueryWrapper<MetricsItemConfig> query = Wrappers.query();
        query.eq("field_alias", fieldAlias);
        List<MetricsItemConfig> result = list(query);
        return result;
    }

    /**
     * 根据度量项字段名获取所有关联度量项配置
     *
     * @param fieldName
     * @return
     */
    public List<MetricsItemConfig> listByFieldName(String fieldName) {
        QueryWrapper<MetricsItemConfig> query = Wrappers.query();
        query.eq("field_name", fieldName);
        List<MetricsItemConfig> result = list(query);
        return result;
    }

    public boolean existsName(Integer id, String fieldName) {
        List<MetricsItemConfig> list = listByFieldName(fieldName);
        if (list != null) {
            if (list.size() > 1) {
                return true;
            } else if (list.size() == 1 && !list.get(0).getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean existsAlias(Integer id, String fieldAlias) {
        List<MetricsItemConfig> list = listByFieldAlias(fieldAlias);
        if (list != null) {
            if (list.size() > 1) {
                return true;
            } else if (list.size() == 1 && !list.get(0).getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    //查询项目中所有引用了的度量项配置，过滤重复数据
    public List<MetricsItemConfig> queryNeedCollectByProjectId(String projectId) {
        List<MetricsItemConfig> metricsItemConfigs = new ArrayList<>(0);
        //找出项目中所有需要的KPI
        List<ReportKpiConfig> reportKpiConfigs = reportKpiConfigService.queryNeedCollectByProjectId(projectId);
        for (ReportKpiConfig reportKpiConfig : reportKpiConfigs) {
            for (MetricsItemConfig metricsItemConfig : reportKpiConfig.getMetricsItemConfigs()) {
                //去重
                if (!metricsItemConfigs.contains(metricsItemConfig)) {
                    metricsItemConfigs.add(metricsItemConfig);
                }
            }
        }
        return metricsItemConfigs;
    }
}
