package com.icss.mvp.dao.report;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.mvp.entity.report.ReportConfig;
import com.icss.mvp.entity.report.ReportKpiConfigRef;

public interface IReportKpiConfigRefDao extends BaseMapper<ReportKpiConfigRef> {

    void saveOrUpdateKpiConfigRef(ReportConfig reportConfig);
}

