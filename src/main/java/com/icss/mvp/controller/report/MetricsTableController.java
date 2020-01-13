package com.icss.mvp.controller.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.report.MetricsTable;
import com.icss.mvp.service.report.MetricsTableConfigService;
import com.icss.mvp.service.report.MetricsTableService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("report/metrics_table")
public class MetricsTableController {

    @Autowired
    private MetricsTableService service;
    @Autowired
    private MetricsTableConfigService metricsTableConfigService;

    @ResponseBody
    @RequestMapping(value = "repair")
    public PlainResponse repair(String projectNo, Integer metricsTableConfigId) {
        if (StringUtils.isEmpty(projectNo) || metricsTableConfigId == null) {
            return PlainResponse.fail("补齐数据错误，缺少必要参数！", CommonResultCode.INVALID_PARAMETER.code);
        }
        PlainResponse response = service.repair(projectNo, metricsTableConfigId);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "save", consumes = "application/json")
    public PlainResponse save(@RequestBody MetricsTable metricsTable) {
        if (StringUtils.isEmpty(metricsTable.getProjectId())) {
            return PlainResponse.fail("项目编号不能为空!");
        }
        return PlainResponse.ok(service.saveOrUpdate(metricsTable));
    }

    @ResponseBody
    @RequestMapping("get_full_by_id")
    public PlainResponse<MetricsTable> getFullById(Integer id) {
        MetricsTable MetricsTable = service.getFullById(id);
        return PlainResponse.ok(MetricsTable);
    }

    @ResponseBody
    @RequestMapping("get_by_project_id_and_metrics_table_config_id")
    public PlainResponse<MetricsTable> getByProjectIdAndMetricsTableConfigId(String projectId, Integer metricsTableConfigId) {
        MetricsTable MetricsTable = service.getByProjectIdAndMetricsTableConfigId(projectId, metricsTableConfigId);
        return PlainResponse.ok(MetricsTable);
    }

    @ResponseBody
    @RequestMapping("get_by_id")
    public PlainResponse<MetricsTable> getById(Integer id) {
        MetricsTable MetricsTable = service.getById(id);
        return PlainResponse.ok(MetricsTable);
    }

    @ResponseBody
    @RequestMapping("delete")
    public PlainResponse<Boolean> deleteById(Integer id) {
        boolean val = service.removeById(id);
        return PlainResponse.ok(val);
    }

    @ResponseBody
    @RequestMapping(value = "query", consumes = "application/x-www-form-urlencoded")
    public ListResponse query(Page page, String queryStr, String projectNo) {
        if (StringUtils.isEmpty(projectNo)) {
            return ListResponse.fail("项目编号不能为空!");
        }
        QueryWrapper<MetricsTable> query = Wrappers.query();
        if (StringUtils.isNotEmpty(queryStr)) {
            query.like("table_name", queryStr);
        }
        query.eq("project_id", projectNo);
        IPage result = service.page(page, query);
        ListResponse response = PageResponse.ok(result.getRecords()).totalCount(result.getTotal()).pageNumber(result.getCurrent()).pageSize(result.getSize());
        return response;
    }
}
