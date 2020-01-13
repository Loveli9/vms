package com.icss.mvp.service.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.report.ChartConfigSeriesDao;
import com.icss.mvp.entity.report.ChartConfigSeries;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public class ChartConfigSeriesService extends ServiceImpl<ChartConfigSeriesDao, ChartConfigSeries> implements IService<ChartConfigSeries> {
    //根据图配置ID查询关联的Series配置
    List<ChartConfigSeries> getByChartConfigId(Serializable chartConfigId) {
        QueryWrapper wrapper = Wrappers.query().eq("chart_config_id", chartConfigId);
        List<ChartConfigSeries> serieses = list(wrapper);
        return serieses;
    }

    public boolean removeByChartConfigId(Integer chartConfigId) {
        QueryWrapper wrapper = Wrappers.query().eq("chart_config_id", chartConfigId);
        return remove(wrapper);
    }
}
