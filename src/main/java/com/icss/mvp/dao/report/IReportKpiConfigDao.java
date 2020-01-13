package com.icss.mvp.dao.report;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.mvp.entity.report.MetricsItemConfig;
import com.icss.mvp.entity.report.ReportKpiConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IReportKpiConfigDao extends BaseMapper<ReportKpiConfig> {

    ReportKpiConfig getKpiConfigsById(@Param("kpiConfigId") Integer kpiConfigId);

    Integer updateMestricItemConfigs(ReportKpiConfig reportKpiConfig);


    /**
     * 根据报表配置ID查询该报表关联的所有KPI配置
     *
     * @param reportConfigId
     * @return
     */
    @ResultType(ReportKpiConfig.class)
    @Select("select rkc.* from report_kpi_config rkc left join report_kpi_config_reference rkcr on rkcr.report_kpi_config_id=rkc.id where rkcr.report_config_id=#{reportConfigId}")
    List<ReportKpiConfig> getByReportConfigId(@Param("reportConfigId") Integer reportConfigId);

    /**
     * 根据KPI引用ID查询关联KPI配置
     *
     * @param reportKpiConfigRefId
     * @return
     */
    @ResultType(ReportKpiConfig.class)
    @Select("select rkc.* from report_kpi_config rkc left join report_kpi_config_reference rkcr on rkcr.report_kpi_config_id=rkc.id where rkcr.id=#{reportKpiConfigRefId}")
    ReportKpiConfig getByReportKpiConfigRefId(Integer reportKpiConfigRefId);

    /**
     * 根据度量项配置ID查询关联KPI配置
     *
     * @param mictricsItemConfigId
     * @return
     */
    @ResultType(ReportKpiConfig.class)
    @Select("select rkc.* from report_kpi_config_ref_mictrics_item_config rkcrmic right join report_kpi_config rkc on rkcrmic.report_kpi_config_id = rkc.id where rkcrmic.mictrics_item_config_id = #{mictricsItemConfigId}")
    List<ReportKpiConfig> getByMictricsItemConfigId(@Param("mictricsItemConfigId") Integer mictricsItemConfigId);

    /**
     * 根据指标配置名称模糊查询指标配置列表
     *
     * @param name 允许为空
     * @return
     */
    List<ReportKpiConfig> queryKpiConfigList(@Param("name") String name);

    /**
     * 根据指标配置ID查询指标配置详情
     *
     * @param id
     * @return
     */
    ReportKpiConfig getKpiConfigById(@Param("id") Integer id);

    /**
     * 根据指标配置ID查询关联度量项
     *
     * @param kpiConfigId
     * @return
     */
    List<MetricsItemConfig> queryKpiRefMetricsItemConfigs(@Param("kpiConfigId") Integer kpiConfigId);

    @ResultType(Integer.class)
    @Select("select count(rkc.id) from report_kpi_config_ref_mictrics_item_config rkcrmic right join report_kpi_config rkc on rkcrmic.report_kpi_config_id = rkc.id where rkcrmic.mictrics_item_config_id = #{mictricsItemConfigId}")
    Integer countByMictricsItemConfigId(@Param("mictricsItemConfigId") Integer mictricsItemConfigId);
}

