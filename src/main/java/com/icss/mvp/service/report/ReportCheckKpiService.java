package com.icss.mvp.service.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.report.IReportCheckKpiDao;
import com.icss.mvp.entity.report.ReportCheckKpi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@SuppressWarnings("All")
@Transactional
public class ReportCheckKpiService extends ServiceImpl<IReportCheckKpiDao, ReportCheckKpi> implements IService<ReportCheckKpi> {

    /**
     * 根据check_ID获取报表检查项列表
     *
     * @param id
     * @return
     */
    public List<ReportCheckKpi> getByCheckId(Integer checkId) {
        QueryWrapper<ReportCheckKpi> wrapper = Wrappers.query();
        wrapper.eq("report_check_id",checkId);
        return getBaseMapper().selectList(wrapper);
    }

    public void deleteByCheckIds(List<Integer> checkIds){
        QueryWrapper<ReportCheckKpi> wrapper = Wrappers.query();
        wrapper.in("report_check_id",checkIds);
        getBaseMapper().delete(wrapper);
    }
}
