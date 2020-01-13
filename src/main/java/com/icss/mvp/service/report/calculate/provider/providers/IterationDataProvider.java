package com.icss.mvp.service.report.calculate.provider.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.icss.mvp.entity.report.Report;
import com.icss.mvp.entity.report.ReportRow;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.common.response.Result;
import com.icss.mvp.entity.report.MetricsItemConfig;
import com.icss.mvp.entity.report.MetricsTableConfig;
import com.icss.mvp.service.report.MetricsTableConfigService;
import com.icss.mvp.service.report.MetricsTableService;
import com.icss.mvp.service.report.calculate.provider.AbstractRealTableDataProvider;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

@Service("iteration_data_provider")
@Transactional
public class IterationDataProvider extends AbstractRealTableDataProvider {

    // private static final Log LOG = LogFactory.getLog(IterationDataProvider.class);
    private static Logger LOG = Logger.getLogger(IterationDataProvider.class);
    // @Autowired
    // private IterationCycleService iterationCycleService;
    @Autowired
    private MetricsTableConfigService metricsTableConfigService;
    @Autowired
    private MetricsTableService metricsTableService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Result getData(Report report, ReportRow row, ProjectInfo project, IterationCycle iterationCycle, MetricsItemConfig metricsItemConfig) {
        Result result = new Result<>(true);
        //TODO 这里需要一个根据度量项配置ID获取关联度量表配置的方法
        MetricsTableConfig metricsTableConfig = metricsTableConfigService.getById(
                metricsItemConfig.getMetricsTableConfigId());

        String tableName = metricsTableConfig.getTableName();
        String fieldName = metricsItemConfig.getFieldName();

        StringBuilder sql = new StringBuilder("select ");
        sql.append(fieldName);
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" where id = ").append(iterationCycle.getId());

        List<Map<String, Object>> datasMap = metricsTableService.executeSelect(sql.toString());

        List<Object> datas = new ArrayList<>(0);
        result.setData(datas);
        datas.add(datasMap);
        return result;
    }

}
