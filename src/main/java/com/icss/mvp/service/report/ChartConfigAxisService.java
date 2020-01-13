package com.icss.mvp.service.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.report.ChartConfigAxisDao;
import com.icss.mvp.dao.report.ChartConfigDao;
import com.icss.mvp.entity.report.ChartConfig;
import com.icss.mvp.entity.report.ChartConfigAxis;
import com.icss.mvp.entity.report.ChartConfigSeries;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public class ChartConfigAxisService extends ServiceImpl<ChartConfigAxisDao, ChartConfigAxis> implements IService<ChartConfigAxis> {
    //根据图配置ID查询关联的Axis配置
    List<ChartConfigAxis> getByChartConfigId(Serializable chartConfigId) {
        QueryWrapper wrapper = Wrappers.query().eq("chart_config_id", chartConfigId);
        List<ChartConfigAxis> axeses = list(wrapper);
        return axeses;
    }

    public boolean removeByChartConfigId(Integer chartConfigId) {
        QueryWrapper wrapper = Wrappers.query().in("chart_config_id", chartConfigId);
        return remove(wrapper);
    }
}
