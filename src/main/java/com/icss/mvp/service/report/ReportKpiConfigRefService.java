package com.icss.mvp.service.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.report.IReportKpiConfigRefDao;
import com.icss.mvp.entity.report.ReportKpiConfigRef;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class ReportKpiConfigRefService extends ServiceImpl<IReportKpiConfigRefDao, ReportKpiConfigRef> implements IService<ReportKpiConfigRef> {

    /**
     * 根据报表配置ID查询所有KPI指标引用
     *
     * @param reportConfigId
     * @return
     */
    public List<ReportKpiConfigRef> getByReportConfigId(Serializable reportConfigId) {
        QueryWrapper conditions = Wrappers.query().eq("report_config_id", reportConfigId).orderByAsc("idx");
        return list(conditions);
    }

    /**
     * @param kpiConfigId
     * @Description:根据指标配置ID查询所有引用的报表配置
     * @Date: 2019/12/23
     * @return: java.util.List<com.icss.mvp.entity.report.ReportKpiConfigRef>
     **/
    public List<ReportKpiConfigRef> getByKpiConfigId(Serializable kpiConfigId) {
        QueryWrapper conditions = Wrappers.query().eq("report_kpi_config_id", kpiConfigId);
        return list(conditions);
    }

    /**
     * @description:根据指标配置ID判断是否存在引用关系
     * @author:xhy
     * @create:2019/12/19 9:05
     **/
    public Boolean existKpiConfigRef(Collection reportKpiConfigIds) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("report_kpi_config_id", reportKpiConfigIds);
        return getBaseMapper().selectCount(wrapper) > 0 ? true : false;
    }

    public void removeReportConfigRefById(Integer reportConfigId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("report_config_id", reportConfigId);
        getBaseMapper().delete(wrapper);
    }

}
