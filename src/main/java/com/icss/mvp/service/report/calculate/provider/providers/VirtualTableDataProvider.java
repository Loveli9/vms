package com.icss.mvp.service.report.calculate.provider.providers;

import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.common.response.Result;
import com.icss.mvp.entity.report.MetricsItemConfig;
import com.icss.mvp.entity.report.MetricsTableConfig;
import com.icss.mvp.entity.report.Report;
import com.icss.mvp.entity.report.ReportRow;
import com.icss.mvp.service.report.MetricsTableConfigService;
import com.icss.mvp.service.report.MetricsTableService;
import com.icss.mvp.service.report.calculate.provider.AbstractProvider;
import com.icss.mvp.utils.NumericUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("virtual_table_data_provider")
@Transactional
public class VirtualTableDataProvider extends AbstractProvider {
    @Autowired
    private MetricsTableService metricsTableService;
    @Autowired
    private MetricsTableConfigService metricsTableConfigService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Result<List> getData(Report report, ReportRow row, ProjectInfo project, IterationCycle iteration, MetricsItemConfig metricsItemConfig) {

        Result<List> result = new Result<>(true);
        MetricsTableConfig metricsTableConfig = metricsTableConfigService.getFullById(metricsItemConfig.getMetricsTableConfigId());
        String tableName = metricsTableConfig.getTableName();
        String tableAlias = metricsTableConfig.getAlias();
        String dataType = metricsItemConfig.getDataType();
        String fieldName = metricsItemConfig.getFieldName();

        StringBuilder sql = createSql(iteration,project,tableName,tableAlias, dataType,fieldName);

        List<Map<String, Object>> datasMap = metricsTableService.executeSelect(sql.toString());

        List<Object> datas = new ArrayList<>(0);
        for (Map item : datasMap) {
            if (item == null){
                continue;
            }
            if ("int".equals(dataType) || "float".equals(dataType)) {
                String v1 = item.get("value1") == null ? "" : item.get("value1").toString();
                v1 = "-".equals(v1) ? "" : v1;
                String v2 = item.get("value2") == null ? "" : item.get("value2").toString();
                v2 = "-".equals(v2) ? "" : v2;
                if (StringUtils.isBlank(v1) && StringUtils.isBlank(v2)) {
                    continue;
                }

                Double value = 0d;

                if (StringUtils.isNotBlank(v1)) {
                    Double d = NumericUtils.parseDoubleMute(v1);
                    if (d == null) {
                        result.setSuccess(false);
                        result.setResult(String.format("源数据用户录入值类型错误（数据名称=%s，表=%s，字段=%s，数据类型=%s）！", tableAlias,
                                                       tableName, fieldName, "整型或浮点"));
                        return result;
                    }

                    value += d;
                }

                if (StringUtils.isNotBlank(v2)) {
                    Double d = NumericUtils.parseDoubleMute(v2);
                    if (d == null) {
                        result.setSuccess(false);
                        result.setResult(String.format("源数据用户录入值类型错误（数据名称=%s，表=%s，字段=%s，数据类型=%s）！", tableAlias,
                                                       tableName, fieldName, "整型或浮点"));
                        return result;
                    }

                    value += d;
                }

                datas.add(value);
            } else if ("string".equals(dataType)) {
                Object obj = item.get("value");
                datas.add(item.get("value"));
            } else if ("date".equals(dataType)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Object obj = item.get("value");
                    if (obj != null) {
                        Date date = sdf.parse((String) item.get("value").toString());
                        datas.add(date);
                    } else {
                        continue;
                    }
                } catch (ParseException e1) {
                    result.setSuccess(false);
                    result.setResult(String.format("源数据自动采集值类型错误，不是有效的日期类型（数据名称=%s，表=%s，字段=%s，日期格式=%s）！", tableAlias, tableName, fieldName, "yyyy-MM-dd"));
                    return result;
                }
            }
        }
        result.setData(datas);
        return result;
    }

    private StringBuilder createSql(IterationCycle iteration, ProjectInfo project, String tableName, String tableAlias, String dataType, String fieldName) {
        StringBuilder sql = new StringBuilder("select ");
        if ("int".equals(dataType) || "float".equals(dataType)) {
            sql.append("mi.input_value as value1, mi.collected_value as value2 ");
        } else {
            sql.append("mi.input_value as value ");
        }
        sql.append("from metrics_item mi ");
        sql.append("LEFT JOIN metrics_row mr on mi.metrics_row_id = mr.id ");
        sql.append("LEFT JOIN metrics_item_config mic on mic.id = mi.metrics_item_config_id ");
        sql.append("LEFT JOIN metrics_table mt on mt.id = mr.metrics_table_id ");
        //sql.append("LEFT JOIN metrics_table_config mtc on mtc.id = mt.metrics_table_config_id ");
        sql.append(" where ");
        sql.append("mt.project_id = '").append(project.getNo()).append("' and ");
        if (iteration != null) {
            sql.append("mr.period_id='").append(iteration.getId()).append("' and ");
        }
        //sql.append("mtc.table_name = '").append(tableName).append("' and ");
        sql.append("mic.field_name = '").append(fieldName).append("'");
        return sql;
    }
}
