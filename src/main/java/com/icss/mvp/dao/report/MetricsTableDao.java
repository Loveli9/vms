package com.icss.mvp.dao.report;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.mvp.entity.report.MetricsTable;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MetricsTableDao extends BaseMapper<MetricsTable> {
    @ResultType(HashMap.class)
    @Select("${sql}")
    List<Map<String, Object>> executeSelect(@Param("sql") String sql);

    @Update("update metrics_table set table_name=#{tableName} where metrics_table_config_id=#{metricsTableConfigId}")
    void updateTableNameByMetricsTableConfigId(@Param("metricsTableConfigId") Integer metricsTableConfigId, @Param("tableName") String tableName);
}
