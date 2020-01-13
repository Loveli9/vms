package com.icss.mvp.service.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.report.ChartConfigDao;
import com.icss.mvp.entity.report.ChartConfig;
import com.icss.mvp.entity.report.ChartConfigAxis;
import com.icss.mvp.entity.report.ChartConfigSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChartConfigService extends ServiceImpl<ChartConfigDao, ChartConfig> implements IService<ChartConfig> {
    @Autowired
    private ChartConfigAxisService axisService;
    @Autowired
    private ChartConfigSeriesService seriesService;

    //根据报表ID查询关联的图配置(含轴及Series配置)
    public List<ChartConfig> getFullByReportConfigId(Integer reportConfigId) {
        QueryWrapper wrapper = Wrappers.query().eq("report_config_id", reportConfigId);
        List<ChartConfig> charts = list(wrapper);
        for (ChartConfig chartConfig : charts) {
            chartConfig.setSeries(seriesService.getByChartConfigId(chartConfig.getId()));
            chartConfig.setAxes(axisService.getByChartConfigId(chartConfig.getId()));
        }
        return charts;
    }

    //根据报表ID查询关联的图配置(含轴及Series配置)
    public List<ChartConfig> getByReportConfigId(Integer reportConfigId) {
        QueryWrapper wrapper = Wrappers.query().eq("report_config_id", reportConfigId);
        List<ChartConfig> charts = list(wrapper);
        return charts;
    }

    //根据报表ID删除关联的图配置
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeChartByReportConfigId(Integer reportConfigId) {
        List<ChartConfig> chartConfigs = getByReportConfigId(reportConfigId);
        for (ChartConfig chartConfig : chartConfigs) {
            axisService.removeByChartConfigId(chartConfig.getId());
            seriesService.removeByChartConfigId(chartConfig.getId());
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("report_config_id", reportConfigId);
        getBaseMapper().delete(wrapper);
    }

    public void saveFull(List<ChartConfig> chartConfigs) {
        for (ChartConfig chartConfig : chartConfigs) {
            super.saveOrUpdate(chartConfig);
            if (chartConfig.getAxes() != null && !chartConfig.getAxes().isEmpty()) {
                for (ChartConfigAxis axis : chartConfig.getAxes()) {
                    axis.setChartConfigId(chartConfig.getId());
                }
                axisService.saveOrUpdateBatch(chartConfig.getAxes());
            }
            if (chartConfig.getSeries() != null && !chartConfig.getSeries().isEmpty()) {
                for (ChartConfigSeries series : chartConfig.getSeries()) {
                    series.setChartConfigId(chartConfig.getId());
                }
                seriesService.saveOrUpdateBatch(chartConfig.getSeries());
            }
        }
    }
}
