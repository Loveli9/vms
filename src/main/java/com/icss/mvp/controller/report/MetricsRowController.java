package com.icss.mvp.controller.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.report.MetricsRow;
import com.icss.mvp.service.report.MetricsRowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("report/metrics_row")
public class MetricsRowController {

    @Autowired
    private MetricsRowService service;


    @ResponseBody
    @RequestMapping("save")
    public PlainResponse save(@RequestBody MetricsRow metricsRow) {
        return PlainResponse.ok(service.merge(metricsRow));
    }

    @ResponseBody
    @RequestMapping("get_full_by_id")
    public PlainResponse<MetricsRow> getFullById(Integer id) {
        MetricsRow metricsRow = service.getFullById(id);
        return PlainResponse.ok(metricsRow);
    }

    /**
     * 度量表全表数据分页查询，暂时不使用该接口
     */
    @ResponseBody
    @RequestMapping("get_full_page_by_id")
    public ListResponse<MetricsRow> getFullPageById(Page page, Integer id) {
        return service.getFullPageById(page, id);
    }

    @ResponseBody
    @RequestMapping("get_by_id")
    public PlainResponse<MetricsRow> getById(Integer id) {
        MetricsRow metricsRow = service.getById(id);
        return PlainResponse.ok(metricsRow);
    }

    @ResponseBody
    @RequestMapping("delete")
    public PlainResponse<Boolean> deleteById(Integer id) {
        boolean val = service.removeById(id);
        return PlainResponse.ok(val);
    }

    @ResponseBody
    @RequestMapping("data_collection")
    public PlainResponse<Boolean> dataCollection(Integer id) {
        boolean val = service.dataCollection(id);
        PlainResponse response = PlainResponse.ok(val);
        if (val) {
            response.setMessage("采集请求已经提交！");
        } else {
            response.setMessage("采集请求提交失败！");
        }
        return response;
    }
}
