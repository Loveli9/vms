package com.icss.mvp.dao.report;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.mvp.entity.report.MetricsItemConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MetricsItemConfigDao extends BaseMapper<MetricsItemConfig> {

    /**
     * 查询与指标关联的数据项配置个数
     *
     * @param reportKpiConfigId
     * @return
     */
    @Select("select count(1) from report_kpi_config_ref_mictrics_item_config where report_kpi_config_id=#{reportKpiConfigId}")
    Integer countByReportKpiConfigId(@Param("reportKpiConfigId") Integer reportKpiConfigId);

    /**
     * 查询与度量项配置关联的指标配置个数
     * @param mictricsItemConfigId
     * @return
     */
    @Select("select count(1) from report_kpi_config_ref_mictrics_item_config where mictrics_item_config_id = #{mictricsItemConfigId}")
    Integer countByMictricsItemConfigId(@Param("mictricsItemConfigId") Integer mictricsItemConfigId);

    @Select("select * from metrics_item_config mic join report_kpi_config_ref_mictrics_item_config ref on mic.id=ref.mictrics_item_config_id where ref.report_kpi_config_id=#{reportKpiConfigId}")
    List<MetricsItemConfig> getByReportKpiConfigId(@Param("reportKpiConfigId") Integer reportKpiConfigId);

}
