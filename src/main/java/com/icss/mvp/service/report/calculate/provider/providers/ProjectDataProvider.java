package com.icss.mvp.service.report.calculate.provider.providers;

import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.common.response.Result;
import com.icss.mvp.entity.report.MetricsItemConfig;
import com.icss.mvp.entity.report.MetricsTableConfig;
import com.icss.mvp.entity.report.Report;
import com.icss.mvp.entity.report.ReportRow;
import com.icss.mvp.service.ProjectInfoService;
import com.icss.mvp.service.report.MetricsItemConfigService;
import com.icss.mvp.service.report.MetricsTableConfigService;
import com.icss.mvp.service.report.MetricsTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("project_data_provider")
@Transactional
public class ProjectDataProvider extends IterationDataProvider {
    @Autowired
    private ProjectInfoService projectService;
    @Autowired
    private MetricsItemConfigService metricsItemConfigService;
    @Autowired
    private MetricsTableService metricsTableService;
    @Autowired
    private MetricsTableConfigService metricsTableConfigService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Result getData(Report report, ReportRow row, ProjectInfo project, IterationCycle iterationCycle, MetricsItemConfig metricsItemConfig) {
        Result result = new Result<>(true);

        MetricsTableConfig metricsTableConfig = metricsTableConfigService.getById(metricsItemConfig.getMetricsTableConfigId());

        String tableName = metricsTableConfig.getTableName();
        String fieldName = metricsItemConfig.getFieldName();

        StringBuilder sql = new StringBuilder("select ");
        sql.append(fieldName);
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" where id = ").append(iterationCycle.getId());

        List<Map<String, Object>> datasMap = metricsTableService.executeSelect(sql.toString());

        List<Object> datas = new ArrayList<>(0);
        datas.add(datasMap);
        result.setData(datas);
        return result;
    }
}
