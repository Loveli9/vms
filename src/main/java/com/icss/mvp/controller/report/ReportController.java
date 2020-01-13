package com.icss.mvp.controller.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.mvp.controller.BaseController;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.common.response.Result;
import com.icss.mvp.entity.report.Report;
import com.icss.mvp.service.report.ReportService;
import com.icss.mvp.service.report.calculate.CalculateService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Ray on 2018/6/6.
 */
@Controller("ReportController")
@SuppressWarnings("all")
@RequestMapping("/report/table")
public class ReportController extends BaseController {

    private static Logger logger = Logger.getLogger(ReportController.class);

    @Autowired
    private ReportService service;
    @Autowired
    private CalculateService calculateService;


    @ResponseBody
    @RequestMapping("repair")
    public Result repair(String projectId, String reportConfigId) {
        Result result = service.repair(projectId, reportConfigId);
        return result;
    }

    @ResponseBody
    @RequestMapping("get_full_by_id")
    public PlainResponse<Report> getFullById(Integer id) {
        Report data = service.getFullById(id);
        return PlainResponse.ok(data);
    }

    @ResponseBody
    @RequestMapping("get_full_by_project_id_and_report_config_id")
    public PlainResponse<Report> getFullById(String projectId, String reportConfigId) {
        Report data = service.getOrCreateByProjectIdAndReportConfigId(projectId, reportConfigId);
        return PlainResponse.ok(data);
    }

    @ResponseBody
    @RequestMapping("get_full_and_passed_by_id")
    public PlainResponse<Report> getFullAndPassedById(Integer id) {
        Report data = service.getFullAndPassedById(id);
        return PlainResponse.ok(data);
    }

    @ResponseBody
    @RequestMapping("get_by_id")
    public PlainResponse<Report> getById(Integer id) {
        Report data = service.getById(id);
        return PlainResponse.ok(data);
    }

    @ResponseBody
    @RequestMapping("delete")
    public PlainResponse<Boolean> deleteById(Integer id) {
        return PlainResponse.ok(service.removeById(id));
    }

    @ResponseBody
    @RequestMapping(value = "query", consumes = "application/x-www-form-urlencoded")
    public ListResponse<Report> query(Page page, String queryStr, String projectNo) {
        QueryWrapper query = Wrappers.query();
        query.eq("project_id", projectNo);
        IPage result = service.page(page, query);
        ListResponse response = PageResponse.ok(result.getRecords()).totalCount(result.getTotal()).pageNumber(result.getCurrent()).pageSize(result.getSize());
        return response;
    }
}
