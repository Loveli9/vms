package com.icss.mvp.service.report.calculate.provider;


import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.common.response.Result;
import com.icss.mvp.entity.report.MetricsItemConfig;
import com.icss.mvp.entity.report.Report;
import com.icss.mvp.entity.report.ReportRow;

public abstract class AbstractProvider {

    public abstract Result getData(Report report, ReportRow row, ProjectInfo project, IterationCycle iterationCycle, MetricsItemConfig metricsItemConfig);

}
