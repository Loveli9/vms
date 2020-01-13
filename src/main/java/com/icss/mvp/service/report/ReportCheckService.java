package com.icss.mvp.service.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.dao.report.IReportCheckDao;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectInfoVo;
import com.icss.mvp.entity.report.Report;
import com.icss.mvp.entity.report.ReportCheck;
import com.icss.mvp.entity.report.ReportCheckKpi;
import com.icss.mvp.entity.report.ReportRow;
import com.icss.mvp.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional
public class ReportCheckService extends ServiceImpl<IReportCheckDao, ReportCheck> implements IService<ReportCheck> {

    @Autowired
    private ReportCheckKpiService checkKpiService;
    @Autowired
    private IProjectListDao projectListDao;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportRowService reportRowService;


    /**
     * 根据ID获取报表配置，结果中包含指标引用及指标配置
     *
     * @param id
     * @return
     */
    public ReportCheck getFullById(Integer id) {
        ReportCheck check = super.getById(id);
        List<ReportCheckKpi> kpis = checkKpiService.getByCheckId(id);
        check.setKpis(kpis);
        loadFullData(check);
        return check;
    }

    public boolean submit(ReportCheck reportCheck) {
        if (reportCheck.getId() != null) {
            QueryWrapper wrapper = Wrappers.query().eq("report_check_id", reportCheck.getId());
            checkKpiService.remove(wrapper);
        } else {
            reportCheck.setSubmitDate(new Date());
        }
        ProjectInfo projectInfo = projectListDao.getProjInfoByNoQ(reportCheck.getProjectId());
        reportCheck.setQa(projectInfo.getQa());
        saveOrUpdate(reportCheck);
        if (reportCheck.getKpis() != null && !reportCheck.getKpis().isEmpty()) {
            reportCheck.getKpis().forEach(reportCheckKpi -> {
                reportCheckKpi.setReportCheckId(reportCheck.getId());
            });
            checkKpiService.saveOrUpdateBatch(reportCheck.getKpis());
        }
        return true;
    }

    public boolean pass(Integer id) {
        ReportCheck reportCheck = getById(id);
        reportCheck.setCheckStatus(ReportCheck.CHECK_STATUS_CHECK_PASS);
        reportCheck.setCheckDescription("审核通过");
        reportCheck.setChecked(true);
        return updateById(reportCheck);
    }

    public boolean failed(Integer id, String description) {
        ReportCheck reportCheck = getById(id);
        reportCheck.setCheckStatus(ReportCheck.CHECK_STATUS_CHECK_FAILED);
        reportCheck.setCheckDescription(description);
        reportCheck.setChecked(true);
        return updateById(reportCheck);
    }

    //取消审核
    public boolean cancel(Integer reportId, Integer reportRowId) {
        QueryWrapper wrapper = Wrappers.query().eq("report_id", reportId)
                .eq("report_row_id", reportRowId)
                .eq("check_status", ReportCheck.CHECK_STATUS_WAIT_CHECK);
        return super.remove(wrapper);
    }

    public IPage pageFullByQa(Page page, String qa) {
        QueryWrapper wrapper = Wrappers.query().eq("qa", qa).eq("check_status", ReportCheck.CHECK_STATUS_WAIT_CHECK);
        IPage pageResult = super.page(page, wrapper);
        if (!pageResult.getRecords().isEmpty()) {
            for (int i = 0; i < pageResult.getRecords().size(); i++) {
                ReportCheck check = (ReportCheck) pageResult.getRecords().get(0);
                loadFullData(check);
            }
        }
        return pageResult;
    }

    private void loadFullData(ReportCheck check) {
        ProjectInfo pi = projectListDao.getProjInfoByNo(check.getProjectId());
        check.setProject(pi);

        Report report = reportService.getById(check.getReportId());
        List<Integer> ids = check.getReportRowIdList();
        List<ReportRow> rows = new ArrayList<>(ids.size());
        for (Integer id : ids) {
            ReportRow row = reportRowService.getFullById(id);
            rows.add(row);
        }
        report.setRows(rows);
        initCheckinfo(report);
        check.setReport(report);
    }

    public List<ReportCheck> listPassedByProjectIdAndReportId(String projectId, Integer reportId) {
        QueryWrapper wrapper = Wrappers.query().
                eq("project_id", projectId).
                eq("report_id", reportId).
                eq("check_status", ReportCheck.CHECK_STATUS_CHECK_PASS);
        return super.list(wrapper);
    }

    public void initCheckinfo(Report report) {
        QueryWrapper wrapper = Wrappers.query().eq("project_id", report.getProjectId()).eq("report_id", report.getId()).eq("report_id", report.getId()).orderByAsc("id");
        List<ReportCheck> checks = list(wrapper);
        if (checks != null) {
            Map<Integer, ReportCheck> map = new HashMap<Integer, ReportCheck>() {{
                checks.forEach(item -> {
                    item.getReportRowIdList().forEach(rowId -> {
                        put(rowId, item);
                    });
                });
            }};
            report.getRows().forEach(item -> {
                if (map.containsKey(item.getId())) {
                    item.setReportCheckId(map.get(item.getId()).getId());
                    item.setCheckStatus(map.get(item.getId()).getCheckStatus());
                    item.setCheckDescription(map.get(item.getId()).getCheckDescription());
                } else {
                    item.setCheckStatus(ReportCheck.CHECK_STATUS_WAIT_SUBMIT_CHECK);
                }
            });
        } else {
            report.getRows().forEach(item -> {
                item.setCheckStatus(ReportCheck.CHECK_STATUS_WAIT_SUBMIT_CHECK);
            });
        }
    }

    public void clearUnpassedRows(Report report) {
        if (report.getRows() != null && !report.getRows().isEmpty()) {
            Iterator<ReportRow> it = report.getRows().iterator();
            while (it.hasNext()) {
                ReportRow row = it.next();
                if (!ReportCheck.CHECK_STATUS_CHECK_PASS.equals(row.getCheckStatus())) {
                    it.remove();
                }
            }
        }
    }


    public boolean cancel(Integer id) {
        ReportCheck check = getFullById(id);
        List<ReportCheckKpi> list = check.getKpis();
        for (ReportCheckKpi rck : list) {
            checkKpiService.removeById(rck.getId());
        }
        return removeById(id);
    }
}
