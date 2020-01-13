package com.icss.mvp.service.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.icss.mvp.dao.report.IReportKpiConfigDao;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.report.MetricsItemConfig;
import com.icss.mvp.entity.report.ReportKpiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class ReportKpiConfigService extends ServiceImpl<IReportKpiConfigDao, ReportKpiConfig> implements IService<ReportKpiConfig> {

    @Resource
    private ReportConfigService reportConfigService;
    @Resource
    private MetricsItemConfigService metricsItemConfigService;
    @Resource
    private IReportKpiConfigDao reportKpiConfigDao;
    @Autowired
    private ReportKpiConfigRefService kpiConfigRefService;

    /**
     * 删除报表指标配置及关联度量配置项
     *
     * @param ids
     * @return
     */
    @Override
    public boolean removeByIds(Collection ids) {
        Boolean exists = kpiConfigRefService.existKpiConfigRef(ids);
        if (!exists) {
            return super.removeByIds(ids);
        }
        return false;
    }

    //返回指标配置（包含关联度量项列表）
    public ReportKpiConfig getFullById(Integer id) {
        ReportKpiConfig reportKpiConfig = getById(id);
        if (reportKpiConfig != null) {
            List<MetricsItemConfig> mics = metricsItemConfigService.getByReportKpiConfigId(reportKpiConfig.getId());
            reportKpiConfig.setMetricsItemConfigs(mics);
        }
        return reportKpiConfig;
    }

    public List<ReportKpiConfig> getByMictricsItemConfigId(Integer mictricsItemConfigId) {
        List<ReportKpiConfig> resultList = reportKpiConfigDao.getByMictricsItemConfigId(mictricsItemConfigId);
        return resultList;
    }

    public TableSplitResult queryByName(String kpiName, PageRequest pageRequest) {
        TableSplitResult result = new TableSplitResult();

        Page page = new Page();
        page.setCurrent(pageRequest.getPageNumber());
        page.setSize(pageRequest.getPageSize());

        QueryWrapper wrapper = Wrappers.query().like("kpi_name", kpiName).orderByAsc("id");
        IPage data = super.page(page, wrapper);

        result.setRows(data.getRecords());
        result.setTotal((int) page.getTotal());
        result.setPage(pageRequest.getPageNumber());
        return result;
    }

    @Override
    public boolean saveOrUpdate(ReportKpiConfig reportKpiConfig) {
        boolean val = super.saveOrUpdate(reportKpiConfig);
        getBaseMapper().updateMestricItemConfigs(reportKpiConfig);
        return val;
    }


    /**
     * 根据指标引用ID查询关联的KPI指标配置
     *
     * @param reportKpiConfigRefId
     * @return
     */
    public ReportKpiConfig getByReportKpiConfigRefId(Integer reportKpiConfigRefId) {
        return getBaseMapper().getByReportKpiConfigRefId(reportKpiConfigRefId);
    }


    /**
     * 查询指标未关联的度量项配置项
     *
     * @param mictricsItemConfigIds
     * @return
     */
    public TableSplitResult<List<MetricsItemConfig>> queryExcluedMictricsItemsByIds(String mictricsItemConfigIds, PageRequest pageRequest) {
        return metricsItemConfigService.queryExcluedMictricsItemsByIds(mictricsItemConfigIds, pageRequest);
    }

    public Integer countByMictricsItemConfigId(Integer mictricsItemConfigId) {
        return reportKpiConfigDao.countByMictricsItemConfigId(mictricsItemConfigId);
    }
}
