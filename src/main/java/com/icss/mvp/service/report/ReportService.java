package com.icss.mvp.service.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.report.IReportDao;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.common.response.Result;
import com.icss.mvp.entity.report.Report;
import com.icss.mvp.entity.report.ReportConfig;
import com.icss.mvp.entity.report.ReportKpi;
import com.icss.mvp.entity.report.ReportRow;
import com.icss.mvp.service.ProjectListService;
import com.icss.mvp.service.report.calculate.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public class ReportService extends ServiceImpl<IReportDao, Report> implements IService<Report> {
    @Autowired
    private ReportRowService reportRowService;
    @Autowired
    private ReportKpiService reportKpiService;
    @Autowired
    private ReportCheckService reportCheckService;
    @Autowired
    private ProjectListService projectListService;
    @Autowired
    private ReportConfigService reportConfigService;
    @Autowired
    private CalculateService calculateService;


    /**
     * 根据报表ID查询报表，结果中包含所有指标
     *
     * @param id
     * @return
     */
    public Report getFullById(Serializable id) {
        Report report = super.getById(id);
        List<ReportRow> rows = reportRowService.getByReportId(id);
        report.setRows(rows);
        reportCheckService.initCheckinfo(report);
        for (ReportRow row : rows) {
            List<ReportKpi> kpis = reportKpiService.getByReportRowId(row.getId());
            row.setKpis(kpis);
        }
        return report;
    }

    /**
     * @description:根据报表配置ID检查是否有报表引用配置
     **/
    public Boolean isExistRefByReportConfigIds(List<Integer> ids) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("report_config_id", ids);
        return getBaseMapper().selectCount(wrapper) > 0 ? true : false;
    }


    public Report getFullAndPassedById(Integer id) {
        Report report = super.getById(id);
        QueryWrapper wrapper = Wrappers.query().eq("report_id", id);
        List<ReportRow> rows = reportRowService.list(wrapper);
        report.setRows(rows);
        reportCheckService.initCheckinfo(report);
        reportCheckService.clearUnpassedRows(report);
        rows.forEach(row -> {
            List<ReportKpi> kpis = reportKpiService.getByReportRowId(row.getId());
            row.setKpis(kpis);
        });
        return report;
    }

    public boolean updateReportName(Integer reportConfigId, String name) {
        QueryWrapper wrapper = Wrappers.query().eq("report_config_id", reportConfigId);
        List<Report> reports = list(wrapper);
        for (Report report : reports) {
            report.setReportName(name);
        }
        if (reports.isEmpty()) {
            return true;
        }
        return updateBatchById(reports);
    }

    public Result repair(String projectId, String reportConfigId) {
        Report report = getOrCreateByProjectIdAndReportConfigId(projectId, reportConfigId);
        Result result = calculateService.repairReport(projectId, report.getId());
        return result;
    }

    public Report getOrCreateByProjectIdAndReportConfigId(String projectId, String reportConfigId) {
        QueryWrapper wrapper = Wrappers.query().eq("report_config_id", reportConfigId).eq("project_id", projectId);
        Report report = getOne(wrapper);
        if (report == null) {
            report = new Report();

            ProjectInfo projectInfo = projectListService.getByProNo(projectId);
            report.setProjectId(projectId);
            report.setProjectName(projectInfo.getName());

            ReportConfig reportConfig = reportConfigService.getById(reportConfigId);
            report.setReportName(reportConfig.getName());
            report.setReportConfigId(reportConfigId);
            report.setType(reportConfig.getType());
            save(report);
        } else {
            report = getFullById(report.getId());
        }
        return report;
    }

    public Report saveAll(Report report) {
        super.save(report);
        if (report.getRows() != null) {
            for (ReportRow row : report.getRows()) {
                reportRowService.merge(row);
            }
        }
        return report;
    }
}
