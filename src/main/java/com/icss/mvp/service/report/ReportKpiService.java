package com.icss.mvp.service.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.report.IReportKpiDao;
import com.icss.mvp.entity.report.ReportKpi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public class ReportKpiService extends ServiceImpl<IReportKpiDao, ReportKpi> implements IService<ReportKpi> {

    /**
     * 根据报表ID查询所有KPI指标
     *
     * @param reportRowId
     * @return
     */
    List<ReportKpi> getByReportRowId(Serializable reportRowId) {
        QueryWrapper conditions = Wrappers.query().eq("report_row_id", reportRowId);
        return getBaseMapper().selectList(conditions);
    }

    public void clearMetricsItem(List<ReportKpi> olds, List<ReportKpi> news) {
        for (ReportKpi reportKpi : olds) {
            boolean exists = exists(reportKpi.getId(), news);
            if (!exists) {
                removeById(reportKpi.getId());
            }
        }
    }

    private boolean exists(Integer id, List<ReportKpi> news) {
        for (ReportKpi item : news) {
            if (item.getId() != null && item.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean deleteByReportRowId(Serializable reportRowId) {
        QueryWrapper condition = Wrappers.query();
        condition.eq("report_row_id", reportRowId);
        return remove(condition);
    }
}
