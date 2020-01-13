package com.icss.mvp.controller.report;

import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.report.MetricsItemConfig;
import com.icss.mvp.entity.report.MetricsTableConfig;
import com.icss.mvp.service.report.MetricsItemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("report/metrics_item_config")
public class MetricsItemConfigController {

    @Autowired
    private MetricsItemConfigService service;

    @ResponseBody
    @RequestMapping("get_by_metrics_table_config_id")
    public ListResponse<MetricsTableConfig> getByMetricsTableConfigId(Integer metricsTableConfigId) {
        List<MetricsItemConfig> metricsTableConfigs = service.getByMetricesTableConfigId(metricsTableConfigId);
        return ListResponse.ok(metricsTableConfigs);
    }

    @ResponseBody
    @RequestMapping("delete")
    public PlainResponse<Boolean> deleteById(Integer id) {
        Integer kpiConfigIdCount = service.countByMictricsItemConfigId(id);
        if(kpiConfigIdCount > 0) {
            return PlainResponse.fail("有项目的指标引用了该度量项配置，不能删除");
        }
        boolean val = service.removeById(id);
        return PlainResponse.ok(val);
    }

}
