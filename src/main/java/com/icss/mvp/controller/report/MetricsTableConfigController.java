package com.icss.mvp.controller.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.report.MetricsTableConfig;
import com.icss.mvp.entity.report.ReportConfig;
import com.icss.mvp.service.report.MetricsTableConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("report/metrics_table_config")
public class MetricsTableConfigController {

    @Autowired
    private MetricsTableConfigService service;

    @ResponseBody
    @RequestMapping(value = "save", consumes = "application/json")
    public PlainResponse save(@RequestBody MetricsTableConfig metricsTableConfig) {
        PlainResponse outResponse = service.merge(metricsTableConfig);
        return outResponse;
    }

    @ResponseBody
    @RequestMapping("get_full_by_id")
    public PlainResponse<MetricsTableConfig> getFullById(Integer id) {
        MetricsTableConfig metricsTableConfig = service.getFullById(id);
        return PlainResponse.ok(metricsTableConfig);
    }

    @ResponseBody
    @RequestMapping("get_by_id")
    public PlainResponse<MetricsTableConfig> getById(Integer id) {
        MetricsTableConfig metricsTableConfig = service.getById(id);
        return PlainResponse.ok(metricsTableConfig);
    }

    @ResponseBody
    @RequestMapping("delete")
    public PlainResponse<Boolean> deleteById(Integer id) {
        boolean val = service.removeById(id);
        return PlainResponse.ok(val);
    }

    @ResponseBody
    @RequestMapping(value = "query", consumes = "application/x-www-form-urlencoded")
    public ListResponse<ReportConfig> query(Page page, String queryStr) {
        QueryWrapper<MetricsTableConfig> query = Wrappers.query();
        if (StringUtils.isNotEmpty(queryStr)) {
            query.like("table_name", queryStr)
                    .or().like("alias", queryStr)
                    .or().like("description", queryStr)
                    .or().eq("type", queryStr)
                    .or().eq("period", queryStr);
        }
        IPage result = service.page(page, query);
        ListResponse response = PageResponse.ok(result.getRecords()).totalCount(result.getTotal()).pageNumber(result.getCurrent()).pageSize(result.getSize());
        return response;
    }
}
