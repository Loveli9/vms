package com.icss.mvp.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.icss.mvp.entity.*;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.service.ProjectReportService;
import com.icss.mvp.util.CookieUtils;
import com.icss.mvp.util.LocalDateUtils;

/**
 * 项目报表
 */
@Controller @RequestMapping("/projectReport") public class ProjectReportController {

    private static     Logger               logger = Logger.getLogger(ProjectReportController.class);
    @Resource private  ProjectReportService projectReportService;
    @Autowired private HttpServletRequest   request;

    /**
     * 报表列表
     *
     * @return
     */
    @RequestMapping("/list") @ResponseBody public Map<String, List<ProjectReport>> getPRs(
            @RequestParam("proNo") String proNo) {
        List<MeasureValue> measureValues = projectReportService.getMeasureValues(proNo);

        Map<String, ProjectReport> reportMap = new HashMap<>();
        for (MeasureValue measureValue : measureValues) {
            String month = LocalDateUtils.getAbbreviatedMonth(measureValue.getMonth()).toUpperCase();
            if (StringUtils.isBlank(month)) {
                continue;
            }

            String key = measureValue.getName();
            ProjectReport report = reportMap.get(key);
            if (report == null) {
                report = new ProjectReport(measureValue.getTitle(), key);
            }

            report.getReportMap().put(month, measureValue.getValue());
            reportMap.put(key, report);
        }

        Map<String, List<ProjectReport>> result = new HashMap<>();
        for (Map.Entry<String, ProjectReport> entry : reportMap.entrySet()) {
            String key = entry.getValue().getTitle();

            List<ProjectReport> reports = result.get(key);
            if (reports == null || reports.isEmpty()) {
                reports = new ArrayList<>();
            }
            reports.add(entry.getValue());

            result.put(key, reports);
        }

        return result;
    }

    @SuppressWarnings({ "unchecked",
                        "rawtypes" }) @RequestMapping("/getQualityonthlyReportList") @ResponseBody public TableSplitResult getQualityonthlyReportList(
            String proNo, String month) {
        TableSplitResult ret = new TableSplitResult();
        List<QualityMonthlyReport> rows = null;
        try {
            rows = projectReportService.getQualityonthlyReportList(proNo, month);
            ret.setRows(rows);
        } catch (Exception e) {
            logger.error("查询数据异常", e);
            ret.setRows(new ArrayList<>());
        }
        ret.setTotal(1);
        ret.setPage(1);
        return ret;
    }

    @RequestMapping("/editProjectAssess") @ResponseBody public BaseResponse editProjectAssess(
            @RequestBody QualityMonthlyReport qualityMonthlyReport) {
        BaseResponse ret = new BaseResponse();
        try {
            projectReportService.editProjectAssess(qualityMonthlyReport);
        } catch (Exception e) {
            logger.error("保存数据异常", e);
            ret.setErrorMessage("-100", "保存数据异常");
        }
        return ret;
    }

    @RequestMapping("/projectStartTime") @ResponseBody public String projectStartTime(String proNo) {
        return projectReportService.projectStartTime(proNo);
    }

    @RequestMapping("/demandchangerate") @ResponseBody public Map<String, Object> demandchangerate(String proNo) {
        BaseResponse result = new BaseResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        data = projectReportService.querydemandchangerate(proNo);
        QualityMonthlyReport qualityMonthlyReport = (QualityMonthlyReport) data.get("qualityMonthlyReport");
        try {
            projectReportService.editQualityMonthlyReport(qualityMonthlyReport);
            result.setCode("success");
        } catch (Exception e) {
            logger.error("编辑失败", e);
            result.setCode("failure");
            result.setMessage("操作失败");
        }
        return data;
    }

    /**
     * 刷新数据，单项目
     *
     * @param proNo
     * @return
     */
    @RequestMapping("/refreshData") @ResponseBody public BaseResponse refreshData(String proNo, String month) {
        BaseResponse ret = new BaseResponse();
        try {
            projectReportService.exitProjectReport(proNo, month);
        } catch (Exception e) {
            logger.error("保存数据异常", e);
            ret.setErrorMessage("-100", "保存数据异常");
        }
        return ret;
    }

    @SuppressWarnings("rawtypes") @RequestMapping("/getProjectAssessPage") @ResponseBody public TableSplitResult getProjectAssessPage(
            ProjectInfo projectInfo, PageRequest pageRequest) {
        TableSplitResult ret = null;
        try {
            String username = CookieUtils.value(request, CookieUtils.USER_NAME);
            ret = projectReportService.getProjectAssessPage(projectInfo, pageRequest, username);
        } catch (Exception e) {
            logger.error("查询数据异常", e);
        }
        return ret;
    }

    @SuppressWarnings("rawtypes") @RequestMapping("/getProjectAssessPageQ") @ResponseBody public TableSplitResult getProjectAssessPageQ(
            ProjectInfo projectInfo, PageRequest pageRequest) {
        TableSplitResult ret = null;
        try {
            String username = CookieUtils.value(request, CookieUtils.USER_NAME);
            ret = projectReportService.getProjectAssessPageQ(projectInfo, pageRequest, username);
        } catch (Exception e) {
            logger.error("查询数据异常", e);
        }
        return ret;
    }

    /**
     * 导出项目评估
     */
    @RequestMapping("/exportAssess") @ResponseBody public void exportAssess(HttpServletResponse response,
                                                                            ProjectInfo projectInfo) {
        try {
            byte[] fileContents = projectReportService.exportAssess(projectInfo);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = "项目评估" + sf.format(new Date()).toString() + ".xlsx";
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                               "attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));
            response.getOutputStream().write(fileContents);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            logger.error("交易失败", e);
        }
    }

    /**
     * 查询问题描述信息，以部门为单位
     *
     * @param projectInfo
     * @param pageRequest
     * @return
     */
    @SuppressWarnings("rawtypes") @RequestMapping("/getProjectDscPage") @ResponseBody public TableSplitResult getProjectDscPage(
            ProjectInfo projectInfo, PageRequest pageRequest) {
        TableSplitResult ret = null;
        try {
            String username = CookieUtils.value(request, CookieUtils.USER_NAME);
            ret = projectReportService.getProjectDscPage(projectInfo, pageRequest, username);
        } catch (Exception e) {
            logger.error("查询数据异常", e);
        }
        return ret;
    }

    /**
     * 导出问题描述
     */
    @RequestMapping("/exportProblem") @ResponseBody public void exportProblem(HttpServletResponse response,
                                                                              ProjectInfo projectInfo) {
        try {
            byte[] fileContents = projectReportService.exportProblem(projectInfo);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = "问题描述" + sf.format(new Date()).toString() + ".xlsx";
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                               "attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));
            response.getOutputStream().write(fileContents);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            logger.error("交易失败", e);
        }
    }

    /**
     * @throws
     * @Description: 分页查询问题风险信息
     */
    @SuppressWarnings({ "rawtypes",
                        "unchecked" }) @RequestMapping(value = "/getProjectDscPageByNo", method = RequestMethod.POST, consumes = "application/json") @ResponseBody public TableSplitResult getProjectDscPageByNo(
            @RequestBody ProjectAssessInfo projectAssessInfo) {
        Integer is361 = null;
        String proNo = projectAssessInfo.getNo();
        if (StringUtils.isNotBlank(projectAssessInfo.getIs361str())) {
            is361 = Integer.parseInt(projectAssessInfo.getIs361str());
            projectAssessInfo.setIs361(is361);
        }
        int limit = projectAssessInfo.getLimit();
        int offset = projectAssessInfo.getOffset();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("is361", is361);
        map.put("id", projectAssessInfo.getId());
        TableSplitResult page = new TableSplitResult();
        page.setPage(offset);
        page.setRows(limit);
        page.setQueryMap(map);
        page = projectReportService.getProjectDscPageByNo(projectAssessInfo, page, proNo);
        return page;
    }

    /**
     * @Description: 删除需求管理信息 @param @return 参数 @return BaseResponse 返回类型 @throws
     */
    @RequestMapping("/deleteProjectDsc") @ResponseBody public BaseResponse deleteProjectDsc(@RequestBody String[] ids) {
        BaseResponse result = new BaseResponse();
        try {
            projectReportService.deleteProjectDsc(ids);
            result.setCode("success");
        } catch (Exception e) {
            logger.error("删除失败：", e);
            result.setCode("failure");
            result.setMessage("操作失败");
        }
        return result;
    }

    /**
     * @Description: 新增需求管理信息 @param @return 参数 @return BaseResponse 返回类型 @throws
     */
    @RequestMapping("/addProjectDsc") @ResponseBody public BaseResponse addProjectDsc(
            @RequestBody ProjectCommentsListInfo projectCommentsListInfo) {
        BaseResponse result = new BaseResponse();
        if ("".equals(projectCommentsListInfo.getSpeed())) {
            projectCommentsListInfo.setSpeed("0");
        }
        try {
            projectReportService.addProjectDsc(projectCommentsListInfo);
            result.setCode("success");
        } catch (Exception e) {
            logger.error("新增失败：", e);
            result.setCode("failure");
            result.setMessage("操作失败");
        }
        return result;
    }

    /**
     * @Description: 修改需求管理信息 @param @return 参数 @return BaseResponse 返回类型 @throws
     */
    @RequestMapping("/editProjectDsc") @ResponseBody public BaseResponse editProjectDsc(
            @RequestBody ProjectCommentsListInfo projectCommentsListInfo) {
        BaseResponse result = new BaseResponse();
        try {
            projectReportService.editProjectDsc(projectCommentsListInfo);
            result.setCode("success");
        } catch (Exception e) {
            logger.error("修改失败：", e);
            result.setCode("failure");
            result.setMessage("操作失败");
        }
        return result;
    }

    /**
     * 保存问题列表仪表盘定制
     *
     * @param list
     * @return
     */
    @RequestMapping(value = "/editProjectDscConfig") @ResponseBody public BaseResponse editProjectDscConfig(
            @RequestBody List<Map<String, Integer>> list) {
        BaseResponse result = new BaseResponse();
        try {
            projectReportService.editProjectDscConfig(list);
            result.setCode("success");
        } catch (Exception e) {
            logger.error("配置失败：", e);
            result.setCode("failure");
            result.setMessage("配置失败");
        }
        return result;
    }

    /*
     * 修改需求变更率
     *
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST) @ResponseBody public BaseResponse editScopeLamp(
            QualityMonthlyReport qualityMonthlyReport) {
        BaseResponse result = new BaseResponse();
        try {
            projectReportService.editQualityMonthlyReport(qualityMonthlyReport);
            result.setCode("success");
        } catch (Exception e) {
            logger.error("编辑失败：", e);
            result.setCode("failure");
            result.setMessage("操作失败");
        }
        return result;
    }

    /**
     * 修改需求完成率
     */
    @RequestMapping(value = "/editDemandProgress", method = RequestMethod.POST) @ResponseBody public BaseResponse editDemandProgress(
            QualityMonthlyReport qualityMonthlyReport) {
        BaseResponse result = new BaseResponse();
        try {
            projectReportService.editDemandProgress(qualityMonthlyReport);
            result.setCode("success");
        } catch (Exception e) {
            logger.error("编辑失败：", e);
            result.setCode("failure");
            result.setMessage("操作失败");
        }
        return result;
    }

    /* 查询每月的数据 */
    @RequestMapping("/projectAssessList") @ResponseBody public Map<String, Object> projectAssessList(
            ProjectInfo projectInfo) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = CookieUtils.value(request, CookieUtils.USER_NAME);
            map = projectReportService.projectAssessList(projectInfo, username);

        } catch (Exception e) {
            logger.error("查询数据异常", e);
        }
        return map;
    }

    /**
     * 消费者业务线：质量-项目指标统计
     *
     * @param projectInfo
     * @param pageRequest
     * @return
     */
    @SuppressWarnings("rawtypes") @RequestMapping("/getCousumerQualityIndex") @ResponseBody public TableSplitResult getCousumerQualityIndex(
            ProjectInfo projectInfo, PageRequest pageRequest) {
        TableSplitResult ret = null;
        try {
            String username = CookieUtils.value(request, CookieUtils.USER_NAME);
            ret = projectReportService.getCousumerQualityIndex(projectInfo, pageRequest, username);

        } catch (Exception e) {
            logger.error("查询数据异常", e);
        }
        return ret;
    }

    /**
     * 消费者业务线：质量-PDU指标统计
     *
     * @param projectInfo
     * @param pageRequest
     * @return
     */
    @SuppressWarnings("rawtypes") @RequestMapping("/getCousumerPDUIndex") @ResponseBody public TableSplitResult getCousumerPDUIndex(
            ProjectInfo projectInfo, PageRequest pageRequest) {
        TableSplitResult ret = null;
        try {
            String username = CookieUtils.value(request, CookieUtils.USER_NAME);
            ret = projectReportService.getCousumerQualityPDUIndex(projectInfo, pageRequest, username);

        } catch (Exception e) {
            logger.error("查询数据异常", e);
        }
        return ret;
    }

    /**
     * 消费者业务线：效率-项目指标统计
     *
     * @param projectInfo
     * @param pageRequest
     * @return
     */
    @SuppressWarnings("rawtypes") @RequestMapping("/getCousumerQualityProjectIndex") @ResponseBody public TableSplitResult getCousumerQualityProjectIndex(
            ProjectInfo projectInfo, PageRequest pageRequest) {
        TableSplitResult ret = null;
        try {
            String username = CookieUtils.value(request, CookieUtils.USER_NAME);
            ret = projectReportService.getCousumerQualityProjectIndex(projectInfo, pageRequest, username);

        } catch (Exception e) {
            logger.error("查询数据异常", e);
        }
        return ret;
    }

    /**
     * 消费者业务线：效率-PDU指标统计
     *
     * @param projectInfo
     * @param pageRequest
     * @return
     */
    @SuppressWarnings("rawtypes") @RequestMapping("/getCousumerEfficiencyPDUIndex") @ResponseBody public TableSplitResult getCousumerEfficiencyPDUIndex(
            ProjectInfo projectInfo, PageRequest pageRequest) {
        TableSplitResult ret = null;
        try {
            String username = CookieUtils.value(request, CookieUtils.USER_NAME);
            ret = projectReportService.getCousumerEfficiencyPDUIndex(projectInfo, pageRequest, username);

        } catch (Exception e) {
            logger.error("查询数据异常", e);
        }
        return ret;
    }

    @SuppressWarnings("rawtypes") @RequestMapping("/getProjectAssessPageAll") @ResponseBody public TableSplitResult getProjectAssessPageAll(
            ProjectInfo projectInfo, PageRequest pageRequest) {
        TableSplitResult ret = null;
        try {
            String username = CookieUtils.value(request, CookieUtils.USER_NAME);
            ret = projectReportService.getProjectAssessPageAll(projectInfo, pageRequest, username);
        } catch (Exception e) {
            logger.error("查询数据异常", e);
        }
        return ret;
    }

    @RequestMapping(value = "/getProjectEdit") @ResponseBody public TableSplitResult getProjectEdit(String id) {
        TableSplitResult page = new TableSplitResult();
        page = projectReportService.getProjectEdit(id);
        return page;
    }
}
