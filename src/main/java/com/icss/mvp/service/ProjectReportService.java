package com.icss.mvp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.icss.mvp.dao.*;
import com.icss.mvp.entity.*;

import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.constant.Constants;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.service.io.ExportService;
import com.icss.mvp.util.CookieUtils;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.MeasureUtils;

@Service public class ProjectReportService {

    private final static Logger             LOG = Logger.getLogger(ProjectListService.class);
    @Autowired private   ProjectReportDao   projectReportDao;
    @Resource private    ProjectInfoService projectInfoService;
    @Autowired private   HttpServletRequest request;
    @Autowired private   ExportService      exportService;

    @Autowired private ManpowerBudgetService         manpowerBudgetService;
    @Resource private  IProjectMaturityAssessmentDAO projectMaturityAssessmentDao;
    @Resource private  IProjectKeyrolesDao           projectKeyrolesDao;
    @Resource IProjectManagersDao projectManagersDao;
    @Resource private  IProjectListDao   projectListDao;
    @Autowired private MeasureCommentDao measureCommentDao;

    @Autowired private ProjectLableDao projectLableDao;

    @Autowired private ProjectInfoVoDao projectInfoVoDao;

    public List<MeasureValue> getMeasureValues(String proNo) {
        return projectReportDao.getMeasureValues(proNo);
    }

    public List<QualityMonthlyReport> getQualityonthlyReportList(String proNo, String month) {
        List<QualityMonthlyReport> ret = projectReportDao.getQualityonthlyReportList(proNo, month);
        return ret;
    }

    public void editProjectAssess(QualityMonthlyReport qualityMonthlyReport) {
        projectReportDao.insertQualityMonthlyReport(qualityMonthlyReport);
    }

    public void exitProjectReport(String proNo, String month) {
        List<QualityMonthlyReport> ret = projectReportDao.getQualityonthlyReportList(proNo, month);
        QualityMonthlyReport qualityMonthlyReport;
        if (ret != null && ret.size() > 0) {
            qualityMonthlyReport = ret.get(0);
        } else {
            qualityMonthlyReport = new QualityMonthlyReport();
            qualityMonthlyReport.setNo(proNo);
            String lastmonth = DateUtils.getSystemFewMonth(-1);
            lastmonth = lastmonth + "01";
            List<QualityMonthlyReport> lastquaMonReports = projectReportDao.getQualityonthlyReportList(proNo,
                                                                                                       lastmonth);
            if (lastquaMonReports != null && lastquaMonReports.size() > 0) {
                QualityMonthlyReport lastquaMonReport = lastquaMonReports.get(0);
                qualityMonthlyReport.setDemandProgress(lastquaMonReport.getDemandProgress());
                qualityMonthlyReport.setDemandTotal(lastquaMonReport.getDemandTotal());
                qualityMonthlyReport.setDemandChangeNumber(lastquaMonReport.getDemandChangeNumber());
            }
        }

        /**
         * 人员点灯：团队成员在岗人数/SOW需求人数 关键角色匹配： 显示数值：关键角色人数/SOW关键角色需求人数 关键角色答辩通过率：
         * 数值显示：答辩通过人数/关键角色总人数
         */
        ManpowerBudget manpowerBudget = manpowerBudgetService.getManpowerBudgetByProNo(proNo);
        if (manpowerBudget != null) {
            if (manpowerBudget.getKeyRoleCount() == null) {
                manpowerBudget.setKeyRoleCount(0);
            }
            if (manpowerBudget.getHeadcount() == null) {
                manpowerBudget.setHeadcount(0);
            }
            List<ProjectKeyroles> keyroles = projectKeyrolesDao.queryProjectKeyrolesNo(proNo);
            SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
            String lastWeek = DateUtils.getLatestWeek(1, false, month).get(0);
            if (keyroles != null && keyroles.size() > 0) {
                Double all = Double.valueOf(keyroles.size());
                int num = 0;
                int onDuty = 0;
                int reserve = 0;
                for (ProjectKeyroles keyrole : keyroles) {
                    if ("通过".equals(keyrole.getReplyResults())) {
                        num++;
                    }
                    try {
                        if (StringUtils.isNotBlank(keyrole.getEndDate())) {
                            if ("在岗".equals(keyrole.getStatus()) && sdt.parse(lastWeek).before(
                                    sdt.parse(keyrole.getEndDate()))) {
                                onDuty++;
                            } else if ("后备".equals(keyrole.getStatus()) && sdt.parse(lastWeek).before(
                                    sdt.parse(keyrole.getEndDate()))) {
                                reserve++;
                            }
                        }
                    } catch (ParseException e) {
                        LOG.error("onDuty,reserve members fail to obtain:" + e.getMessage());
                    }
                }
                if (num == 0 || all == 0) {
                    qualityMonthlyReport.setKeyRolesPass("0");
                } else {
                    qualityMonthlyReport.setKeyRolesPass(StringUtilsLocal.keepTwoDecimals(num / all * 100).toString());
                }
                // 关键角色待答辩数
                int notPass = (int) (all - num);
                qualityMonthlyReport.setNotPassedNumber(notPass);
                // 关键角色匹配缺口
                int rolesDifferNumber = (int) (manpowerBudget.getKeyRoleCount() - onDuty);
                qualityMonthlyReport.setRolesDifferNumber(rolesDifferNumber);
                // 关键角色诉求人数
                qualityMonthlyReport.setKeyRolesTotal(manpowerBudget.getKeyRoleCount());
                // 关键角色通过数
                qualityMonthlyReport.setPassNumber(num);
                // 关键角色匹配度
                if (manpowerBudget.getKeyRoleCount() <= 0) {
                    qualityMonthlyReport.setKeyRoles("--");
                } else {
                    qualityMonthlyReport.setKeyRoles(StringUtilsLocal.keepTwoDecimals(
                            (onDuty + reserve) / manpowerBudget.getKeyRoleCount() * 100).toString());
                }
            } else {
                if (manpowerBudget.getKeyRoleCount() <= 0) {
                    qualityMonthlyReport.setKeyRoles("--");
                    qualityMonthlyReport.setKeyRolesTotal(manpowerBudget.getKeyRoleCount());
                    // 关键角色匹配缺口
                    qualityMonthlyReport.setRolesDifferNumber(0);
                } else {
                    qualityMonthlyReport.setKeyRoles("0");
                    qualityMonthlyReport.setKeyRolesTotal(manpowerBudget.getKeyRoleCount());
                    // 关键角色匹配缺口
                    qualityMonthlyReport.setRolesDifferNumber(manpowerBudget.getKeyRoleCount());
                }
            }
            Map<String, Object> count = projectManagersDao.queryOMPUserSelectedCount(proNo, lastWeek);
            int onDutyCount = StringUtils.isNotBlank(StringUtilsLocal.valueOf(count.get("on_duty"))) ? Integer.parseInt(
                    StringUtilsLocal.valueOf(count.get("on_duty"))) : 0;
            int reserveCount = StringUtils.isNotBlank(
                    StringUtilsLocal.valueOf(count.get("reserve"))) ? Integer.parseInt(
                    StringUtilsLocal.valueOf(count.get("reserve"))) : 0;
            if (manpowerBudget.getHeadcount() <= 0) {
                qualityMonthlyReport.setPeopleLamp("--");
                // 人力缺口数
                qualityMonthlyReport.setPeopleDifferNumber(0);
                // 总人力诉求数
                qualityMonthlyReport.setPeopleLampTotal(0);
            } else {
                qualityMonthlyReport.setPeopleLamp(StringUtilsLocal.keepTwoDecimals(
                        (onDutyCount + reserveCount) / Double.valueOf(manpowerBudget.getHeadcount()) * 100).toString());
                // 人力缺口数
                qualityMonthlyReport.setPeopleDifferNumber(
                        (int) (Double.valueOf(manpowerBudget.getHeadcount()) - onDutyCount));
                // 总人力诉求数
                qualityMonthlyReport.setPeopleLampTotal((int) (Double.valueOf(manpowerBudget.getHeadcount()) - 0));
            }
        }
        // 需求总数
        /*
         * Integer demandTotal = projectReportDao.queryTotalCount(proNo); //已完成需求总数
         * String status = "6"; Integer finishTotal =
         * projectReportDao.queryTotalCountByStatus(proNo, status); //需求进度 double
         * demandProgress =finishTotal/Double.valueOf(demandTotal)*100;
         * qualityMonthlyReport.setDemandProgress(StringUtilsLocal.keepTwoDecimals(
         * demandProgress).toString());
         */
        // 需求变更率

        String demandProgress;
        demandProgress = qualityMonthlyReport.getDemandProgress();
        if (StringUtilsLocal.isBlank(demandProgress)) {
            demandProgress = null;
        }

        // 开发进度
        ProjectInfo projectInfo = projectListDao.getProjInfoByNo(proNo);
        Date startDate = projectInfo.getStartDate();
        Date endDate = projectInfo.getEndDate();
        Date date = new Date();
        long usedTime = date.getTime() - startDate.getTime();
        long totalTime = endDate.getTime() - startDate.getTime();
        double developmentProgress = usedTime / Double.valueOf(totalTime) * 100;
        if (developmentProgress <= 0) {
            developmentProgress = 0;
        } else if (developmentProgress > 100) {
            developmentProgress = 100;
        }
        qualityMonthlyReport.setDevelopmentProgress(StringUtilsLocal.keepTwoDecimals(developmentProgress).toString());
        double deviation = StringUtilsLocal.parseDouble(demandProgress) - developmentProgress;
        // 进度偏差率
        if (deviation == 0 || developmentProgress == 0) {
            qualityMonthlyReport.setPlanLamp("0");
        } else {
            double planLamp = deviation / (developmentProgress) * 100;
            if (planLamp > 100) {
                planLamp = 100;
            } else if (planLamp < -100) {
                planLamp = -100;
            }
            qualityMonthlyReport.setPlanLamp(StringUtilsLocal.keepTwoDecimals(planLamp).toString());
        }

        /**
         * 质量目标点灯： 绿灯：质量点评 中所有指标 绿灯 黄灯：质量点评 中的指标中有一个黄灯 红灯：质量点评 中的指标中有一个红灯
         */
        int red = 0;
        int green = 0;
        int yellow = 0;
        //当前项目配置流程类目
        String isSelectCate = "";
        try {
            isSelectCate = projectLableDao.getIsSelectCategory(proNo);
        } catch (Exception e) {
            LOG.error("projectLableDao.getIsSelectCategory exception, error: " + e.getMessage());
        }
        if ("高斯".equals(isSelectCate)) {
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("proNo", proNo);
            List<MeasureComment> measureComments = measureCommentDao.queryCommentListByNo(parameter);
            for (MeasureComment measureComment : measureComments) {
                String light = MeasureUtils.light(measureComment);
                if (light == null || "".equals(light)) {
                    continue;
                }
                if ("green".equals(light)) {
                    green++;
                } else if ("red".equals(light)) {
                    red++;
                } else if ("yellow".equals(light)) {
                    yellow++;
                }
            }
            // 红黄灯占比
            Double lightAll = Double.valueOf(red + yellow + green);
            double qualityLamp = 0.0;
            if (!lightAll.equals(0.0)) {
                qualityLamp = (red + yellow) / lightAll * 100;
            }
            qualityMonthlyReport.setQualityLamp(StringUtilsLocal.keepTwoDecimals(qualityLamp).toString());
        } else {
            qualityMonthlyReport.setQualityLamp("-1");
        }
        qualityMonthlyReport.setRedLightNumber(red);
        qualityMonthlyReport.setGreenLightNumber(green);
        qualityMonthlyReport.setYellowLightNumber(yellow);

        qualityMonthlyReport.setCreationDate(month);
        editProjectAssess(qualityMonthlyReport);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" }) public TableSplitResult getProjectAssessPage(ProjectInfo projectInfo,
                                                                                                PageRequest pageRequest,
                                                                                                String username) {
        TableSplitResult ret = new TableSplitResult();
        Set<String> nos = projectInfoService.projectNos(projectInfo, username);
        List<String> proNos = new ArrayList<>(nos);
        int number = projectInfo.getNumber();
        if (proNos.size() <= 0) {
            ret.setErr(new ArrayList<>(), pageRequest.getPageNumber());
            return ret;
        }
        Integer count = projectReportDao.getProjectAssessCount("(" + StringUtilsLocal.listToSqlIn(proNos) + ")",
                                                               number);
        if (count <= 0) {
            return ret;
        }
        List<QualityMonthlyReport> qualityMonthlyReports = projectReportDao.getProjectAssessByNos(
                "(" + StringUtilsLocal.listToSqlIn(proNos) + ")", pageRequest, number);
        for (QualityMonthlyReport qualityMonthlyReport : qualityMonthlyReports) {
            Integer closed361 = qualityMonthlyReport.getClosed361();
            Integer sum361 = qualityMonthlyReport.getSum361();
            Integer closedAAR = qualityMonthlyReport.getClosedAAR();
            Integer sumAAR = qualityMonthlyReport.getSumAAR();
            Integer closedAudit = qualityMonthlyReport.getClosedAudit();
            Integer sumAudit = qualityMonthlyReport.getSumAudit();
            Integer closedQuality = qualityMonthlyReport.getClosedQuality();
            Integer sumQuality = qualityMonthlyReport.getSumQuality();
            double degree361 = 0;
            double degreeAAR = 0;
            double degreeAudit = 0;
            double degreeQuality = 0;
            qualityMonthlyReport.setDegree361(sum361, closed361, degree361);
            qualityMonthlyReport.setDegreeAAR(sumAAR, closedAAR, degreeAAR);
            qualityMonthlyReport.setDegreeAudit(sumAudit, closedAudit, degreeAudit);
            qualityMonthlyReport.setDegreeQuality(sumQuality, closedQuality, degreeQuality);
        }
        ret.setRows(qualityMonthlyReports);
        ret.setPage(pageRequest.getPageNumber());
        ret.setTotal(count == null ? 0 : count);
        return ret;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" }) public TableSplitResult getProjectAssessPageQ(
            ProjectInfo projectInfo, PageRequest pageRequest, String username) {
        TableSplitResult ret = new TableSplitResult();
        Set<String> nos = projectInfoService.projectNos(projectInfo, username);
        List<String> proNos = new ArrayList<>(nos);
        int number = projectInfo.getNumber();
        if (proNos.size() <= 0) {
            ret.setErr(new ArrayList<>(), pageRequest.getPageNumber());
            return ret;
        }

        List<QualityMonthlyReport> qualityMonthlyReports = projectReportDao.getProjectAssessByNosQ(
                "(" + StringUtilsLocal.listToSqlIn(proNos) + ")", pageRequest, number);
        Integer count = projectReportDao.getProjectAssessCountQ("(" + StringUtilsLocal.listToSqlIn(proNos) + ")",
                                                                number);

        ret.setRows(qualityMonthlyReports);
        ret.setPage(pageRequest.getPageNumber());
        ret.setTotal(count == null ? 0 : count);
        return ret;
    }

    private String nullOrNot(String str) {
        return str == null ? "-" : str;
    }

    @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" }) public byte[] exportAssess(ProjectInfo projectInfo) {
        String username = CookieUtils.value(request, CookieUtils.USER_NAME);
        PageRequest pageRequest = new PageRequest();
        pageRequest.setOffset(null);
        TableSplitResult ret = getProjectAssessPage(projectInfo, pageRequest, username);
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("项目评估");
        CellStyle headerStyle = exportService.getHeadStyle(wb);
        CellStyle cellStyle = exportService.getCellStyle(wb);
        CellStyle cellStyle1 = exportService.getCellStyle(wb);
        cellStyle1.setAlignment(CellStyle.ALIGN_LEFT);
        /************************ 生成表格头 ***************************/
        Row row = sheet.createRow(0);
        row.setHeightInPoints((float) 30.0);
        exportService.setCellValue(0, "所属交付部", row, headerStyle);
        exportService.setCellValue(1, "PDU/SDU名称", row, headerStyle);
        exportService.setCellValue(2, "所属地域", row, headerStyle);
        exportService.setCellValue(3, "项目名称", row, headerStyle);
        exportService.setCellValue(4, "启动时间", row, headerStyle);
        exportService.setCellValue(5, "结束时间", row, headerStyle);

        exportService.setCellValue(6, "人员点灯", row, headerStyle);
        exportService.setCellValue(7, "计划进度点灯", row, headerStyle);
        exportService.setCellValue(8, "需求范围点灯", row, headerStyle);
        exportService.setCellValue(9, "质量目标点灯", row, headerStyle);
        exportService.setCellValue(10, "关键角色匹配", row, headerStyle);
        exportService.setCellValue(11, "关键角色答辩通过率", row, headerStyle);
        for (int i = 0; i <= 11; i++) {
            sheet.autoSizeColumn(i);
            if (i == 0 || i == 3) {
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 3);
            }
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 2);
        }
        /************************ 项目评估信息 **************************/
        List<QualityMonthlyReport> qualityMonthlyReports = (List<QualityMonthlyReport>) ret.getRows();
        if (qualityMonthlyReports == null || qualityMonthlyReports.size() == 0) {
            return exportService.returnWb2Byte(wb);
        }
        for (int i = 1; i <= qualityMonthlyReports.size(); i++) {
            QualityMonthlyReport qualityMonthlyReport = qualityMonthlyReports.get(i - 1);
            row = sheet.createRow(i);
            row.setHeightInPoints((float) 100.0);
            exportService.setCellValue(0, qualityMonthlyReport.getDu(), row, cellStyle);
            exportService.setCellValue(1, qualityMonthlyReport.getPduSpdt(), row, cellStyle);
            exportService.setCellValue(2, qualityMonthlyReport.getArea(), row, cellStyle);
            exportService.setCellValue(3, qualityMonthlyReport.getName(), row, cellStyle);
            exportService.setCellValue(4, DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL,
                                                               qualityMonthlyReport.getStartDate()), row, cellStyle);
            exportService.setCellValue(5, DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL,
                                                               qualityMonthlyReport.getEndDate()), row, cellStyle);

            exportService.setCellValue(6, nullOrNot(qualityMonthlyReport.getPeopleLamp()), row, cellStyle);
            exportService.setCellValue(7, nullOrNot(qualityMonthlyReport.getPlanLamp()), row, cellStyle);
            exportService.setCellValue(8, nullOrNot(qualityMonthlyReport.getScopeLamp()), row, cellStyle);
            exportService.setCellValue(9, nullOrNot(qualityMonthlyReport.getQualityLamp()), row, cellStyle);
            exportService.setCellValue(10, nullOrNot(qualityMonthlyReport.getKeyRoles()), row, cellStyle);
            exportService.setCellValue(11, nullOrNot(qualityMonthlyReport.getKeyRolesPass()), row, cellStyle);
        }
        return exportService.returnWb2Byte(wb);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" }) public TableSplitResult getProjectDscPage(ProjectInfo projectInfo,
                                                                                             PageRequest pageRequest,
                                                                                             String username) {
        TableSplitResult ret = new TableSplitResult();
        Set<String> nos = projectInfoService.projectNos(projectInfo, username);
        List<String> proNos = new ArrayList<>(nos);
        if (proNos.size() <= 0) {
            ret.setErr(new ArrayList<>(), pageRequest.getPageNumber());
            return ret;
        }
        List<ProjectCommentsInfo> ProjectCommentsInfos = projectReportDao.getProjectDscByNos(
                "(" + StringUtilsLocal.listToSqlIn(proNos) + ")", pageRequest);
        Integer count = projectReportDao.getProjectDscCount("(" + StringUtilsLocal.listToSqlIn(proNos) + ")");

        ret.setRows(ProjectCommentsInfos);
        ret.setPage(pageRequest.getPageNumber());
        ret.setTotal(count == null ? 0 : count);
        return ret;
    }

    @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" }) public byte[] exportProblem(ProjectInfo projectInfo) {
        String username = CookieUtils.value(request, CookieUtils.USER_NAME);
        PageRequest pageRequest = new PageRequest();
        pageRequest.setOffset(null);
        TableSplitResult ret = getProjectDscPage(projectInfo, pageRequest, username);
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("问题描述");
        CellStyle headerStyle = exportService.getHeadStyle(wb);
        CellStyle cellStyle = exportService.getCellStyle(wb);
        CellStyle cellStyle1 = exportService.getCellStyle(wb);
        cellStyle1.setAlignment(CellStyle.ALIGN_LEFT);
        /************************ 生成表格头 ***************************/
        Row row = sheet.createRow(0);
        row.setHeightInPoints((float) 30.0);
        exportService.setCellValue(0, "所属交付部", row, headerStyle);
        exportService.setCellValue(1, "PDU/SDU名称", row, headerStyle);
        exportService.setCellValue(2, "所属地域", row, headerStyle);
        exportService.setCellValue(3, "项目名称", row, headerStyle);
        exportService.setCellValue(4, "启动时间", row, headerStyle);
        exportService.setCellValue(5, "结束时间", row, headerStyle);
        exportService.setCellValue(6, "问题", row, headerStyle);
        exportService.setCellValue(7, "改进措施", row, headerStyle);
        exportService.setCellValue(8, "进展&效果描述", row, headerStyle);
        exportService.setCellValue(9, "计划完成时间", row, headerStyle);
        exportService.setCellValue(10, "实际完成时间", row, headerStyle);
        exportService.setCellValue(11, "类型", row, headerStyle);
        exportService.setCellValue(12, "责任人", row, headerStyle);
        exportService.setCellValue(13, "状态", row, headerStyle);
        for (int i = 0; i <= 13; i++) {
            sheet.autoSizeColumn(i);
            if (i == 6 || i == 7 || i == 8) {
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 5);
            }
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 2);
        }
        /************************ 项目评估信息 **************************/
        List<ProjectAssessInfo> projectAssessInfos = (List<ProjectAssessInfo>) ret.getRows();
        if (projectAssessInfos == null || projectAssessInfos.size() == 0) {
            return exportService.returnWb2Byte(wb);
        }
        for (int i = 1; i <= projectAssessInfos.size(); i++) {
            ProjectAssessInfo projectAssessInfo = projectAssessInfos.get(i - 1);
            row = sheet.createRow(i);
            row.setHeightInPoints((float) 100.0);
            exportService.setCellValue(0, projectAssessInfo.getDu() == null ? "-" : projectAssessInfo.getDu(), row,
                                       cellStyle);
            exportService.setCellValue(1, projectAssessInfo.getPduSpdt() == null ? "-" : projectAssessInfo.getPduSpdt(),
                                       row, cellStyle);
            exportService.setCellValue(2, projectAssessInfo.getArea() == null ? "-" : projectAssessInfo.getArea(), row,
                                       cellStyle);
            exportService.setCellValue(3, projectAssessInfo.getName() == null ? "-" : projectAssessInfo.getName(), row,
                                       cellStyle);
            if (projectAssessInfo.getStartDate() != null) {
                exportService.setCellValue(4, DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL,
                                                                   projectAssessInfo.getStartDate()), row, cellStyle);
            } else {
                exportService.setCellValue(4, "-", row, cellStyle);
            }
            if (projectAssessInfo.getEndDate() != null) {
                exportService.setCellValue(5, DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL,
                                                                   projectAssessInfo.getEndDate()), row, cellStyle);
            } else {
                exportService.setCellValue(5, "-", row, cellStyle);
            }
            exportService.setCellValue(6, nullOrNot(projectAssessInfo.getQuestion()), row, cellStyle1);
            exportService.setCellValue(7, nullOrNot(projectAssessInfo.getImprMeasure()), row, cellStyle1);
            exportService.setCellValue(8, nullOrNot(projectAssessInfo.getProgressDesc()), row, cellStyle1);
            if (projectAssessInfo.getFinishTime() != null) {
                exportService.setCellValue(9, DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL,
                                                                   projectAssessInfo.getFinishTime()), row, cellStyle);
            } else {
                exportService.setCellValue(9, "-", row, cellStyle);
            }
            if (projectAssessInfo.getActualFinishTime() != null) {
                exportService.setCellValue(10, DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL,
                                                                    projectAssessInfo.getActualFinishTime()), row,
                                           cellStyle);
            } else {
                exportService.setCellValue(10, "-", row, cellStyle);
            }
            String type = null;
            if (projectAssessInfo.getIs361() == 1) {
                type = "361评估问题";
            } else if (projectAssessInfo.getIs361() == 2) {
                type = "AAR";
            } else if (projectAssessInfo.getIs361() == 3) {
                type = "开工会审计";
            } else if (projectAssessInfo.getIs361() == 4) {
                type = "质量回溯";
            } else {
                type = "-";
            }
            exportService.setCellValue(11, nullOrNot(type), row, cellStyle);
            exportService.setCellValue(12, nullOrNot(projectAssessInfo.getPersonLiable()), row, cellStyle);
            exportService.setCellValue(13, nullOrNot(projectAssessInfo.getState()), row, cellStyle);
        }
        return exportService.returnWb2Byte(wb);
    }

	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	public TableSplitResult getProjectDscPageByNo(ProjectAssessInfo projectAssessInfo, PageRequest pageRequest) {
		TableSplitResult ret = new TableSplitResult();
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("proNo", projectAssessInfo.getNo());
		if (null != projectAssessInfo.getFlag()) {
			parameter.put("flag", projectAssessInfo.getFlag());
		} else {
			parameter.put("is361", projectAssessInfo.getIs361());
			parameter.put("offset", pageRequest.getOffset());
			parameter.put("limit", pageRequest.getPageSize());
		}
		Integer count = projectReportDao.getProjectDscByNoCount(parameter);
		List<ProjectAssessInfo> projectAssessInfos = new ArrayList<>();
		if (count > 0) {
			projectAssessInfos = projectReportDao.getProjectDscByNo(parameter);
		}
		ret.setRows(projectAssessInfos);
		ret.setTotal(count == null ? 0 : count);
		return ret;
	}*/

    /**
     * @Description: 分页查询问题风险信息
     * 返回类型 @throws
     */
    @SuppressWarnings({ "rawtypes", "unchecked" }) public TableSplitResult getProjectDscPageByNo(
            ProjectAssessInfo projectAssessInfo, TableSplitResult page, String proNo) {
        TableSplitResult ret = new TableSplitResult();
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("proNo", proNo);
        if (null != projectAssessInfo.getFlag()) {
            parameter.put("flag", projectAssessInfo.getFlag());
        } else {
            parameter.put("is361", projectAssessInfo.getIs361());
            parameter.put("limit", page.getRows());
            parameter.put("offset", page.getPage());
            parameter.put("id", page.getQueryMap().get("id"));
        }
        // 查询总记录条数
        Integer count = 0;
        try {
            count = projectReportDao.getProjectDscByNoCount(parameter);
        } catch (Exception e) {
            LOG.error("projectReportDao.getProjectDscByNoCount exception, error: " + e.getMessage());
        }
        List<ProjectAssessInfo> projectAssessInfos = new ArrayList<>();
        if (count > 0) {
            projectAssessInfos = projectReportDao.getProjectDscByNo(parameter);
        }
        ret.setTotal(count);
        ret.setRows(projectAssessInfos);
        return ret;
    }

    @SuppressWarnings("unused") public void deleteProjectDsc(String[] ids) {
        for (String id : ids) {
            int a = projectReportDao.deleteProjectDsc(id);
        }
    }

    public void addProjectDsc(ProjectCommentsListInfo projectCommentsListInfo) {
        // 设置创建、修改时间
        Date date = new Date();
        projectCommentsListInfo.setCreateTime(date);
        projectCommentsListInfo.setModifyTime(date);
        projectCommentsListInfo.setIsDeleted(0);
        projectMaturityAssessmentDao.replaceCommentsList(projectCommentsListInfo);
    }

    public void editProjectDsc(ProjectCommentsListInfo projectCommentsListInfo) {
        Date date = new Date();
        projectCommentsListInfo.setModifyTime(date);
        projectReportDao.editProjectDsc(projectCommentsListInfo);
    }

    /**
     * 保存问题列表仪表盘定制
     *
     * @param list
     */
    public void editProjectDscConfig(List<Map<String, Integer>> list) {
        for (Map<String, Integer> map : list) {
            projectReportDao.editProjectDscConfig(map.get("id"), map.get("flag"));
        }
    }

    public void editQualityMonthlyReport(QualityMonthlyReport qualityMonthlyReport) throws Exception {
        projectReportDao.editQualityMonthlyReport(qualityMonthlyReport);
    }

    /**
     * 修改需求完成率
     */
    public void editDemandProgress(QualityMonthlyReport qualityMonthlyReport) {
        projectReportDao.editDemandProgress(qualityMonthlyReport);
    }

    public Map<String, Object> projectAssessList(ProjectInfo projectInfo, String username) {
        String month = null;
        Map<String, Object> map = new HashMap<>();
        List<String> months = new ArrayList<>();
        Date displayMonth = DateUtils.parseDate(DateUtils.SHORT_FORMAT_GENERAL, month);
        displayMonth = displayMonth == null ? new Date() : displayMonth;
        Date oneYearBefore = DateUtils.getFirstDayOfAmountMonth(-11, displayMonth);
        Map<String, Integer> indexes = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            Date firstDay = DateUtils.getFirstDayOfAmountMonth(i, oneYearBefore);
            String year_month = DateUtils.formatDate(DateUtils.YEAR_PERIOD_MONTH, firstDay);
            months.add(year_month);
            indexes.put(year_month, i);
        }
        map.put("months", months);
        Set<String> nos = projectInfoService.projectNos(projectInfo, username);
        List<String> proNos = new ArrayList<>(nos);
        int num = projectInfo.getNumber();
        Nunber assessmentNunber = projectReportDao.getAssessmentNunber("(" + StringUtilsLocal.listToSqlIn(proNos) + ")",
                                                                       num);
        Nunber AARNunber = projectReportDao.getAARNunber("(" + StringUtilsLocal.listToSqlIn(proNos) + ")", num);
        Nunber auditNunber = projectReportDao.getAuditNunber("(" + StringUtilsLocal.listToSqlIn(proNos) + ")", num);
        Nunber qualityNunber = projectReportDao.getQualityNunber("(" + StringUtilsLocal.listToSqlIn(proNos) + ")", num);
        map.put("assess", assessmentNunber);
        map.put("AAR", AARNunber);
        map.put("audit", auditNunber);
        map.put("quality", qualityNunber);
        return map;
    }

    // 查询项目开始时间
    public String projectStartTime(String proNo) {
        String startTime = projectReportDao.projectStartTime(proNo);
        return startTime;
    }

    /**
     * 消费者业务线：质量-项目指标统计
     *
     * @param projectInfo
     * @param pageRequest
     * @param username
     * @return getCousumerQualityPDUIndex
     */
    @SuppressWarnings({ "rawtypes", "unchecked" }) public TableSplitResult getCousumerQualityIndex(
            ProjectInfo projectInfo, PageRequest pageRequest, String username) {
        TableSplitResult ret = new TableSplitResult();
        //分页查询每个项目信息
        List<ProjectInfoVo> projectList = projectInfoService.queryProjectInfos(projectInfo, username, pageRequest);
        List<Integer> idsList = new ArrayList<Integer>();
        idsList.add(Constants.INVALID_VERSIONS);
        idsList.add(Constants.SINGULAR_REGRESSION_FAILURES);
        idsList.add(Constants.QUALITY_ACCIDENTS);
        idsList.add(Constants.QUALITY_EARLY_WARNING);
        idsList.add(Constants.SERIOUS_ONLINE_PROBLEMS);
        idsList.add(Constants.PRODUCTION_BATCH_PROBLEM);
        idsList.add(Constants.MISSING_TESTING_AFTER_DELIVERY);
        idsList.add(Constants.LOW_UALITY_AND_SERIOUS_PROGRESS_DEVIATION);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (ProjectInfoVo projectInfoVo : projectList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("no", projectInfoVo.getNo());
            map.put("pm", projectInfoVo.getPm());
            map.put("area", projectInfoVo.getArea());
            map.put("hwpdu", projectInfoVo.getHwpdu());
            map.put("pdu_spdt", projectInfoVo.getPduSpdt());
            map.put("end_date", projectInfoVo.getEndDate());
            map.put("hwzpdu", projectInfoVo.getHwzpdu());
            map.put("name", projectInfoVo.getName());
            map.put("start_date", projectInfoVo.getStartDate());
            List<CousumerQuality> list = projectReportDao.getCousumerQualityIndex(projectInfoVo.getNo(), idsList);
            for (CousumerQuality cousumerQuality : list) {
                map.put("id" + cousumerQuality.getMeasureId(), cousumerQuality);

            }
            data.add(map);
        }
        //统计项目总数
        Integer total = projectInfoService.queryProjectInfosCount(projectInfo, username);
        ret.setRows(data);
        ret.setPage(pageRequest.getPageNumber());
        ret.setTotal(total == null ? 0 : total);
        return ret;
    }

    /**
     * 消费者业务线：质量-PDU指标统计
     *
     * @param projectInfo
     * @param pageRequest
     * @param username
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" }) public TableSplitResult getCousumerQualityPDUIndex(
            ProjectInfo projectInfo, PageRequest pageRequest, String username) {
        TableSplitResult ret = new TableSplitResult();
        List<Integer> idsList = new ArrayList<Integer>();
        idsList.add(Constants.INVALID_VERSIONS);
        idsList.add(Constants.SINGULAR_REGRESSION_FAILURES);
        idsList.add(Constants.QUALITY_ACCIDENTS);
        idsList.add(Constants.QUALITY_EARLY_WARNING);
        idsList.add(Constants.SERIOUS_ONLINE_PROBLEMS);
        idsList.add(Constants.PRODUCTION_BATCH_PROBLEM);
        idsList.add(Constants.MISSING_TESTING_AFTER_DELIVERY);
        idsList.add(Constants.LOW_UALITY_AND_SERIOUS_PROGRESS_DEVIATION);
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        //分页查询每个项目信息的部门名称
        List<String> pduList = projectInfoService.queryProjectPDUInfos(projectInfo, username, pageRequest);
        for (String pdu : pduList) {
            Map<String, Object> PDUmap = new HashMap<String, Object>();
            //部门查询
            List<CousumerQuality> queryProjectPDUInfos = projectInfoVoDao.getCousumerQualityPDUIndex(pdu, idsList);
            PDUmap.put("pdu_spdt", pdu);
            for (CousumerQuality cousumerQuality : queryProjectPDUInfos) {
                PDUmap.put("id" + cousumerQuality.getMeasureId(), cousumerQuality.getMeasureValue());
            }
            dataList.add(PDUmap);
        }
        //统计项目总数
        Integer total = projectInfoService.queryPDUInfosCount(projectInfo, username);
        ret.setRows(dataList);
        ret.setPage(pageRequest.getPageNumber());
        ret.setTotal(total == null ? 0 : total);
        return ret;

    }

    /**
     * 消费者业务线：效率-项目指标统计
     *
     * @param projectInfo
     * @param pageRequest
     * @param username
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" }) public TableSplitResult getCousumerQualityProjectIndex(
            ProjectInfo projectInfo, PageRequest pageRequest, String username) {
        TableSplitResult ret = new TableSplitResult();
        //分页查询每个项目信息
        List<ProjectInfoVo> projectList = projectInfoService.queryProjectInfos(projectInfo, username, pageRequest);
        List<Integer> idsList = new ArrayList<Integer>();
        idsList.add(Constants.CODE_DEVELOPMENT_EFFICIENCY);
        idsList.add(Constants.E2E_CODE_DEVELOPMENT_EFFICIENCY);
        idsList.add(Constants.PROBLEM_SOLVING_EFFICIENCY);
        idsList.add(Constants.VERSION_DELIVERY_EFFICIENCY);
        idsList.add(Constants.VERSION_REQUIREMENTS_DEVELOPMENT_EFFICIENCY);
        idsList.add(Constants.MANUAL_TEST_EXECUTION_EFFICIENCY);
        idsList.add(Constants.TEST_CASE_DESIGN_EFFICIENCY);
        idsList.add(Constants.TEST_AUTOMATION_SCRIPT_DEVELOPMENT_EFFICIENCY);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (ProjectInfoVo projectInfoVo : projectList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("no", projectInfoVo.getNo());
            map.put("pm", projectInfoVo.getPm());
            map.put("area", projectInfoVo.getArea());
            map.put("hwpdu", projectInfoVo.getHwpdu());
            map.put("pdu_spdt", projectInfoVo.getPduSpdt());
            map.put("end_date", projectInfoVo.getEndDate());
            map.put("hwzpdu", projectInfoVo.getHwzpdu());
            map.put("name", projectInfoVo.getName());
            map.put("start_date", projectInfoVo.getStartDate());
            List<CousumerQuality> list = projectReportDao.getCousumerQualityIndex(projectInfoVo.getNo(), idsList);
            for (CousumerQuality cousumerQuality : list) {
                map.put("id" + cousumerQuality.getMeasureId(), cousumerQuality);

            }
            data.add(map);
        }
        //统计项目总数
        Integer total = projectInfoService.queryProjectInfosCount(projectInfo, username);
        ret.setRows(data);
        ret.setPage(pageRequest.getPageNumber());
        ret.setTotal(total == null ? 0 : total);
        return ret;
    }

    /**
     * 消费者业务线：效率-PDU指标统计
     *
     * @param projectInfo
     * @param pageRequest
     * @param username
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" }) public TableSplitResult getCousumerEfficiencyPDUIndex(
            ProjectInfo projectInfo, PageRequest pageRequest, String username) {
        TableSplitResult ret = new TableSplitResult();
        List<Integer> idsList = new ArrayList<Integer>();
        idsList.add(Constants.CODE_DEVELOPMENT_EFFICIENCY);
        idsList.add(Constants.E2E_CODE_DEVELOPMENT_EFFICIENCY);
        idsList.add(Constants.PROBLEM_SOLVING_EFFICIENCY);
        idsList.add(Constants.VERSION_DELIVERY_EFFICIENCY);
        idsList.add(Constants.VERSION_REQUIREMENTS_DEVELOPMENT_EFFICIENCY);
        idsList.add(Constants.MANUAL_TEST_EXECUTION_EFFICIENCY);
        idsList.add(Constants.TEST_CASE_DESIGN_EFFICIENCY);
        idsList.add(Constants.TEST_AUTOMATION_SCRIPT_DEVELOPMENT_EFFICIENCY);
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        //分页查询每个项目信息的部门名称
        List<String> pduList = projectInfoService.queryProjectPDUInfos(projectInfo, username, pageRequest);
        for (String pdu : pduList) {
            Map<String, Object> PDUmap = new HashMap<String, Object>();
            //部门查询
            List<CousumerQuality> queryProjectPDUInfos = projectInfoVoDao.getCousumerQualityPDUIndex(pdu, idsList);
            Double everyPDUProjectNum = projectInfoVoDao.getCousumerQualityPDUProjectNum(pdu);
            DecimalFormat df = new DecimalFormat("0.00");
            PDUmap.put("pdu_spdt", pdu);
            for (CousumerQuality cousumerQuality : queryProjectPDUInfos) {
                String PDUEfficiencyIndex = null;
                if (everyPDUProjectNum == 0) {
                    PDUEfficiencyIndex = String.valueOf(0.00);
                } else {
                    PDUEfficiencyIndex = df.format(
                            Double.valueOf(cousumerQuality.getMeasureValue()) / everyPDUProjectNum);
                }
                PDUmap.put("id" + cousumerQuality.getMeasureId(), PDUEfficiencyIndex);
            }
            dataList.add(PDUmap);
        }
        //统计项目总数
        Integer total = projectInfoService.queryPDUInfosCount(projectInfo, username);
        ret.setRows(dataList);
        ret.setPage(pageRequest.getPageNumber());
        ret.setTotal(total == null ? 0 : total);
        return ret;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" }) public TableSplitResult getProjectAssessPageAll(
            ProjectInfo projectInfo, PageRequest pageRequest, String username) {
        TableSplitResult ret = new TableSplitResult();
        Set<String> nos = projectInfoService.projectNos(projectInfo, username);
        List<String> proNos = new ArrayList<>(nos);
        if (proNos.size() <= 0) {
            ret.setErr(new ArrayList<>(), pageRequest.getPageNumber());
            return ret;
        }

        List<QualityMonthlyReport> qualityMonthlyReports = projectReportDao.getProjectAssessPageAll(
                "(" + StringUtilsLocal.listToSqlIn(proNos) + ")", pageRequest);
        Integer count = projectReportDao.getProjectAssessCountAll("(" + StringUtilsLocal.listToSqlIn(proNos) + ")");

        ret.setRows(qualityMonthlyReports);
        ret.setPage(pageRequest.getPageNumber());
        ret.setTotal(count == null ? 0 : count);
        return ret;
    }

    public Map<String, Object> querydemandchangerate(String proNo) {
        Map<String, Object> data = new HashMap<String, Object>();
        String month = DateUtils.getSystemMonth();
        month = month + "01";
        List<QualityMonthlyReport> qualityonthlyReportList = projectReportDao.getQualityonthlyReportList(proNo, month);
        QualityMonthlyReport qualityMonthlyReport;
        if (qualityonthlyReportList != null && qualityonthlyReportList.size() > 0) {
            qualityMonthlyReport = qualityonthlyReportList.get(0);
            data.put("demandTotal", qualityMonthlyReport.getDemandTotal());
            data.put("demandChangeNumber", qualityMonthlyReport.getDemandChangeNumber());
            Double demandTotal = Double.valueOf(qualityMonthlyReport.getDemandTotal());
            int demandChangeNumber = qualityMonthlyReport.getDemandChangeNumber();
            qualityMonthlyReport.setScopeLamp(
                    StringUtilsLocal.keepTwoDecimals(demandChangeNumber / demandTotal * 100).toString());
            qualityMonthlyReport.setCreationDate(month);
            data.put("qualityMonthlyReport", qualityMonthlyReport);
        }
        return data;
    }

    public TableSplitResult getProjectEdit(String id) {
        TableSplitResult ret = new TableSplitResult();
        ProjectAssessInfo projectAssessInfos = projectReportDao.getProjectEdit(id);
        ret.setRows(projectAssessInfos);
        return ret;
    }
}
