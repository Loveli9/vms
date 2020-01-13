package com.icss.mvp.service.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.report.IReportRowDao;
import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.member.MemberEntity;
import com.icss.mvp.entity.report.*;
import com.icss.mvp.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class ReportRowService extends ServiceImpl<IReportRowDao, ReportRow> implements IService<ReportRow> {
    @Autowired
    private ReportKpiService reportKpiService;

    @Autowired
    private MemberService memberService;

    /**
     * 根据报表 行ID查询行数据，结果中包含所有指标
     *
     * @param id
     * @return
     */
    public ReportRow getFullById(Serializable id) {
        ReportRow reportRow = super.getById(id);
        List<ReportKpi> kpis = reportKpiService.getByReportRowId(id);
        reportRow.setKpis(kpis);
        return reportRow;
    }

    /**
     * 根据报表ID查询关联行数据集合，结果中包含所有指标
     *
     * @param reportId
     * @return
     */
    public List<ReportRow> listFullByReportId(Serializable reportId) {
        QueryWrapper wrapper = Wrappers.query().eq("report_id", reportId);
        List<ReportRow> rows = super.list(wrapper);
        for (ReportRow row : rows) {
            List<ReportKpi> kpis = reportKpiService.getByReportRowId(row.getId());
            row.setKpis(kpis);
        }
        return rows;
    }

    /**
     * 根据报表ID获取所有报表行
     *
     * @param reportId
     * @return
     */
    public List<ReportRow> getByReportId(Serializable reportId) {
        QueryWrapper condition = Wrappers.query().eq("report_id", reportId);
        return list(condition);
    }

    public List<ReportRow> merges(List<ReportRow> rows) {
        List<ReportRow> ps = new ArrayList<>();
        for (ReportRow reportRow : rows) {
            ps.add(merge(reportRow));
        }
        return ps;
    }

    public ReportRow merge(ReportRow reportRow) {
        if (reportRow.getId() == null) {
            getBaseMapper().insert(reportRow);
        } else {
            getBaseMapper().updateById(reportRow);
            List<ReportKpi> kpis = reportKpiService.getByReportRowId(reportRow.getId());
            reportKpiService.clearMetricsItem(kpis, reportRow.getKpis());
        }
        if (reportRow.getKpis() != null && !reportRow.getKpis().isEmpty()) {
            for (ReportKpi kpi : reportRow.getKpis()) {
                kpi.setReportRowId(reportRow.getId());
                reportKpiService.save(kpi);
            }
        }
        return reportRow;
    }

    public boolean deleteByReportId(Serializable reportId) {

        QueryWrapper condition = Wrappers.query().eq("report_id", reportId);
        List<ReportRow> rows = list(condition);
        rows.forEach(row -> {
            removeById(reportId);
        });
        return remove(condition);
    }

    @Override
    public boolean removeById(Serializable id) {
        super.removeById(id);
        reportKpiService.deleteByReportRowId(id);
        return true;
    }

    public ReportRow getFullOne(Integer reportId, String periodType, String periodId, String code, String name) {
        QueryWrapper conditions = null;
        if (ReportRow.PERIOD_TYPE_PROJECT.equals(periodType)) {
            conditions = Wrappers.query().eq("report_id", reportId)
                    .eq("period", periodType);//eq("period_id", periodId);
        } else if (ReportRow.PERIOD_TYPE_PERIOD.equals(periodType)) {
            conditions = Wrappers.query().eq("report_id", reportId)
                    .eq("period", periodType).eq("period_id", periodId);
        }
        if (StringUtils.isNotEmpty(code)) {
            conditions.eq("code", code);
        }
        if (StringUtils.isNotEmpty(name)) {
            conditions.eq("name", name);
        }
        ReportRow reportRow = getOne(conditions);
        if (reportRow != null) {
            List<ReportKpi> kpis = reportKpiService.getByReportRowId(reportRow.getId());
            reportRow.setKpis(kpis);
        }
        return reportRow;
    }


    /**
     * 获得报告，返回结果是map，key:newCreate表示是否新建的，true表示是，false表示不是，key:report表示返回报表对象
     *
     * @param project        项目
     * @param iterationCycle 迭代
     * @param reportConfig
     * @return
     */
    public List<ReportRow> getOrCreateReportRow(ProjectInfo project, IterationCycle iterationCycle, ReportConfig reportConfig, Report report) {
        String periodType = reportConfig.getPeriod().toLowerCase();
        List<ReportRow> rows = new ArrayList<>(0);
        if (Report.TYPE_PROJECT.equalsIgnoreCase(report.getType())) {
            ReportRow row = newReportRow(project, iterationCycle, reportConfig, report, periodType);
            rows.add(row);
        } else if (Report.TYPE_DEMAND.equalsIgnoreCase(report.getType())) {
        } else if (Report.TYPE_PERSIONNEL.equalsIgnoreCase(report.getType())) {
            ListResponse<MemberEntity> members = memberService.getProjectMembers(project.getNo(), new HashSet<>(0));
            if (members.getData() != null && !members.getData().isEmpty()) {
                for (MemberEntity member : members.getData()) {
                    ReportRow row = newReportRow(project, iterationCycle, reportConfig, report, periodType, member.getAccount(), member.getName());
                    row.setCode(member.getAccount());
                    row.setName(member.getName());
                    save(row);
                    rows.add(row);
                }
            }
        }
        merges(rows);
        return rows;
    }

    private ReportRow newReportRow(ProjectInfo project, IterationCycle iterationCycle, ReportConfig reportConfig, Report report, String periodType) {
        return newReportRow(project, iterationCycle, reportConfig, report, periodType, null, null);
    }

    private ReportRow newReportRow(ProjectInfo project, IterationCycle iterationCycle, ReportConfig reportConfig, Report report, String periodType, String account, String name) {
        String iterationCycleId;
        if (iterationCycle == null){
            iterationCycleId = null;
        }else {
            iterationCycleId = iterationCycle.getId();
        }
        ReportRow row = getFullOne(report.getId(), periodType, iterationCycleId, account, name);
        if (row == null) {
            row = new ReportRow();
            row.setPeriod(periodType);
            if (ReportRow.PERIOD_TYPE_PROJECT.equals(periodType)) {
                row.setPeriodId(project.getNo());
                row.setPeriodName(project.getName());
            } else if (ReportRow.PERIOD_TYPE_PERIOD.equals(periodType)) {
                row.setPeriodId(iterationCycle.getId());
                row.setPeriodName(iterationCycle.getIteName());
                row.setPeriodStartDate(iterationCycle.getStartDate());
                row.setPeriodEndDate(iterationCycle.getEndDate());
            }
            row.setReportId(report.getId());
            for (ReportKpiConfigRef ref : reportConfig.getKpiConfigRefs()) {
                ReportKpi kpi = new ReportKpi();
                kpi.setReportKpiConfigId(ref.getReportKpiConfigId());
                row.addKpi(kpi);
            }
        }
        return row;
    }

    public void removeAllByIds(List<Integer> ids) {
        QueryWrapper wrapper = Wrappers.query().in("id", ids);
        remove(wrapper);
        wrapper = Wrappers.query().in("report_row_id", ids);
        reportKpiService.remove(wrapper);
    }
}
