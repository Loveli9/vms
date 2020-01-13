package com.icss.mvp.service.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.report.IReportConfigDao;
import com.icss.mvp.entity.report.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

@Service
@SuppressWarnings("All")
@Transactional
public class ReportConfigService extends ServiceImpl<IReportConfigDao, ReportConfig> implements IService<ReportConfig> {
    @Autowired
    private ReportKpiConfigRefService kpiConfigRefService;
    @Autowired
    private ReportKpiConfigService kpiConfigService;
    @Autowired
    private ReportConfigExcludedService reportConfigExcludedService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ChartConfigService chartConfigService;
    @Autowired
    private ChartConfigAxisService axisService;
    @Autowired
    private ChartConfigSeriesService seriesService;

    /**
     * 根据ID获取报表配置，结果中包含指标引用及指标配置
     *
     * @param id
     * @return
     */
    public ReportConfig getFullById(Serializable id) {
        ReportConfig report = super.getById(id);
        if (report != null) {
            List<ReportKpiConfigRef> refs = kpiConfigRefService.getByReportConfigId(id);
            for (ReportKpiConfigRef reportKpiConfigRef : refs) {
                ReportKpiConfig reportKpiConfig = kpiConfigService.getById(reportKpiConfigRef.getReportKpiConfigId());
                reportKpiConfigRef.setKpiName(reportKpiConfig.getKpiName());
                reportKpiConfigRef.setReportKpiConfig(reportKpiConfig);
            }
            report.setCharts(chartConfigService.getFullByReportConfigId(report.getId()));
            report.setKpiConfigRefs(refs);
        }
        return report;
    }


    public void saveReportConfig(ReportConfig reportConfig) {
        Integer id = reportConfig.getId();
        if (id != null) {
            super.updateById(reportConfig);
            //清理指标配置引用关系
            kpiConfigRefService.removeReportConfigRefById(reportConfig.getId());
            chartConfigService.removeChartByReportConfigId(reportConfig.getId());
        } else {
            getBaseMapper().insert(reportConfig);
        }
        reportConfig.getKpiConfigRefs().forEach(reportKpiConfigRef -> {
            reportKpiConfigRef.setReportConfigId(reportConfig.getId());
        });
        reportConfig.getCharts().forEach(chart -> {
            chart.setReportConfigId(reportConfig.getId());
        });

        reportService.updateReportName(reportConfig.getId(), reportConfig.getName());
        kpiConfigRefService.saveOrUpdateBatch(reportConfig.getKpiConfigRefs());
        chartConfigService.saveFull(reportConfig.getCharts());
    }

    public List<ReportConfig> listExcludedByProjectNo(String projectNo) {
        QueryWrapper<ReportConfig> query = Wrappers.query();
        query.like("project_id", projectNo).or().isNull("project_id");
        List<ReportConfig> reportConfigs = super.list(query);
        reportConfigs.forEach(reportConfig -> {
            List<ReportKpiConfigRef> refs = kpiConfigRefService.getByReportConfigId(reportConfig.getId());
            reportConfig.setKpiConfigRefs(refs);
            refs.forEach(reportKpiConfigRef -> {
                ReportKpiConfig reportKpiConfig = kpiConfigService.getByReportKpiConfigRefId(reportKpiConfigRef.getId());
                reportKpiConfigRef.setReportKpiConfig(reportKpiConfig);
            });
        });
        List<ReportConfigExcluded> excludeds = reportConfigExcludedService.listByProjectNo(projectNo);
        Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>() {{
            excludeds.forEach(excluded -> {
                if (containsKey(excluded.getReportConfigId())) {
                    get(excluded.getReportConfigId()).add(excluded.getReportKpiConfigId());
                } else {
                    put(excluded.getReportConfigId(), new ArrayList<Integer>() {{
                        add(excluded.getReportKpiConfigId());
                    }});
                }
            });
        }};
        Iterator<ReportConfig> iterator = reportConfigs.iterator();
        while (iterator.hasNext()) {
            ReportConfig reportConfig = iterator.next();
            Iterator<ReportKpiConfigRef> refs = reportConfig.getKpiConfigRefs().iterator();
            while (refs.hasNext()) {
                ReportKpiConfigRef ref = refs.next();
                if (map.containsKey(ref.getReportConfigId()) && map.get(ref.getReportConfigId()).contains(ref.getReportKpiConfigId())) {
                    refs.remove();
                }
            }
            if (reportConfig.getKpiConfigRefs().isEmpty()) {
                iterator.remove();
            }
        }
        return reportConfigs;
    }

    public List<ReportConfig> listFullByProjectNo(String projectNo) {
        QueryWrapper<ReportConfig> query = Wrappers.query();
        query.like("project_id", projectNo).or().isNull("project_id");
        List<ReportConfig> reportConfigs = super.list(query);
        reportConfigs.forEach(reportConfig -> {
            List<ReportKpiConfigRef> refs = kpiConfigRefService.getByReportConfigId(reportConfig.getId());
            reportConfig.setKpiConfigRefs(refs);
            refs.forEach(reportKpiConfigRef -> {
                ReportKpiConfig reportKpiConfig = kpiConfigService.getByReportKpiConfigRefId(reportKpiConfigRef.getId());
                reportKpiConfigRef.setReportKpiConfig(reportKpiConfig);
            });
        });
        Iterator<ReportConfig> iterator = reportConfigs.iterator();
        while (iterator.hasNext()) {
            ReportConfig reportConfig = iterator.next();
            if (reportConfig.getKpiConfigRefs().isEmpty()) {
                iterator.remove();
            }
        }

        return reportConfigs;
    }

    public ReportConfig getExcludedByIdAndProjectNo(Integer id, String projectNo) {
        ReportConfig reportConfig = getFullById(id);
        List<ReportConfigExcluded> excludeds = reportConfigExcludedService.listByProjectNoAndReportConfigId(projectNo, id);
        List<Integer> excludedsReportKpiConfigIds = new ArrayList<Integer>() {{
            excludeds.forEach(excluded -> {
                if (!contains(excluded.getReportConfigId())) {
                    add(excluded.getReportKpiConfigId());
                }
            });
        }};
        Iterator<ReportKpiConfigRef> refs = reportConfig.getKpiConfigRefs().iterator();
        while (refs.hasNext()) {
            ReportKpiConfigRef ref = refs.next();
            if (excludedsReportKpiConfigIds.contains(ref.getReportKpiConfigId())) {
                refs.remove();
            }
        }
        return reportConfig;
    }
}
