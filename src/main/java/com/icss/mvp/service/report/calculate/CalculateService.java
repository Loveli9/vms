package com.icss.mvp.service.report.calculate;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.Result;
import com.icss.mvp.entity.report.*;
import com.icss.mvp.service.BuOverviewService;
import com.icss.mvp.service.IterationCycleService;
import com.icss.mvp.service.ProjectInfoService;
import com.icss.mvp.service.ProjectListService;
import com.icss.mvp.service.report.*;
import com.icss.mvp.service.report.calculate.provider.AbstractProvider;
import com.icss.mvp.utils.ApplicationContextUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CalculateService {

    // private static final Log LOG = LogFactory.getLog(CalculateService.class);
    private static Logger LOG = Logger.getLogger(CalculateService.class);

    @Autowired
    private ProjectListService projectListService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportKpiService reportKpiService;
    @Autowired
    private ExpressionCalculator expressionCalculateService;
    @Autowired
    private IterationCycleService iterationCycleService;
    @Autowired
    private ReportConfigService reportConfigService;
    @Autowired
    private MetricsTableConfigService metricsTableConfigService;
    @Autowired
    private ReportRowService reportRowService;
    @Autowired
    private ReportKpiConfigService reportKpiConfigService;

    //生成所有项目当前迭代的所有报表
    //    public void generateCurrentIterationReport() {
    //        List<ProjectInfo> projects = projectInfoService.all();
    //
    //        for (ProjectInfo projectInfo : projects) {
    //            List<ReportKpiConfigRef> reportKpiConfigRefs = projectInfo.getIterationReportConfigurationReferences();
    //            for (Project.IterationReportConfigurationReference reportConfigurationReference : reportConfigurationReferences) {
    //                ReportConfiguration reportConfiguration = reportConfigurationReference.getReportConfiguration();
    //                Result<Integer> result = repairReport(project.getId(), reportConfiguration.getId());
    //                LOG.info(result.getResult() + String.format("(项目=%s,报表=%s)", project.getName(), reportConfiguration.getName()));
    //            }
    //        }
    //    }

    //根据报表类型补齐项目所有迭代或项目的指定报表（报表type为project时，补齐项目报表，报表type为iteration时补齐迭代报表）
    public Result<Integer> repairReport(String proNo, Integer reportId) {
        Result result = new Result(true);
        ProjectInfo projectInfo = projectListService.getByProNo(proNo);
        if (projectInfo == null) {
            return new Result(false, "报表补齐失败，指定的项目不存在！");
        }
        Report report = reportService.getFullById(reportId);
        ReportConfig reportConfig = reportConfigService.getFullById(report.getReportConfigId());

        if (report == null) {
            return new Result(false, "报表补齐失败，指定的报表不存在！");
        }
        int count = 0;
        if (reportConfig.getPeriod().equals(ReportRow.PERIOD_TYPE_PERIOD)) {
            List<IterationCycle> iterationCycles = iterationCycleService.getMessage(proNo);
            for (IterationCycle iterationCycle : iterationCycles) {
                if (iterationCycle.getStartDate() != null && System.currentTimeMillis() >= iterationCycle.getStartDate().getTime() && iterationCycle.getEndDate() != null && System.currentTimeMillis() <= iterationCycle.getEndDate().getTime()) {
                    QueryWrapper condition = Wrappers.query().eq("period_id", iterationCycle.getId()).eq("report_Id", report.getId());
                    List<ReportRow> reportRows = reportRowService.list(condition);
                    if (reportRows.isEmpty()) {
                        Result<Integer> generateResult = generateAndCalculateReportRow(projectInfo, iterationCycle, reportConfig, report, false);
                        if (generateResult.isSuccess() && generateResult.getData() != null) {
                            count++;
                        }
                    }
                }
            }
        } else if (reportConfig.getPeriod().equals(ReportRow.PERIOD_TYPE_PROJECT)) {
            Result<Integer> generateResult = generateAndCalculateReportRow(projectInfo, null, reportConfig, report, false);
            if (generateResult.isSuccess() && generateResult.getData() != null) {
                count++;
            }
        } else {
            result.setSuccess(false);
            result.setResult(String.format("报表补齐失败，无法识别的报表类型个：报表=“%s”", reportConfig.getName()));
        }
        if (count > 0) {
            result.setResult("完成报表补齐，本次共补齐报表" + count + "个。");
        } else {
            result.setResult("当前没有需要补齐的报表。");
        }
        return result;
    }


    private Result generateAndCalculateReportRow(ProjectInfo project, IterationCycle iterationCycle, ReportConfig reportConfig, Report report, boolean recalculate) {
        Result result = new Result(true);
        try {
            //获取指定项目或迭代报表行，如果没有则生成一个新的(新报表行ID为null)
            List<ReportRow> reportRows = reportRowService.getOrCreateReportRow(project, iterationCycle, reportConfig, report);
            for (ReportRow reportRow : reportRows) {
                Result calculateResult = calculateKpis(report, project, iterationCycle, reportConfig, reportRow);
                result.setData(calculateResult.getData());
                if (calculateResult.isSuccess()) {
                    //是否是新数据，如果是则将返回结果中的data设置为1（表示一条新数据）
                    result.addErrors(calculateResult.getErrors());
                }
            }
            reportService.saveOrUpdate(report);
            return result;
        } catch (Exception e) {
            StringBuilder error = new StringBuilder("报表计算出现异常：报表=" + reportConfig.getName());
            if (iterationCycle != null) {
                String.format("，迭代=" + iterationCycle.getIteName());
            }
            result.setResult(error.toString());
            result.setException(e);
            result.setSuccess(false);
            LOG.error(error.toString(), e);
        }
        return result;
    }

    public Result recalculateReport(String reportRowId) throws Exception {
        Result result = new Result();
        ReportRow reportRow = reportRowService.getFullById(reportRowId);
        Report report = reportService.getFullById(reportRow.getReportId());
        ReportConfig reportConfig = reportConfigService.getFullById(report.getReportConfigId());
        if (reportRow == null) {
            result.setSuccess(false);
            result.addError(String.format("报表重算失败，指定的报表行（报表ID=%s）不存在！", reportRowId));
            return result;
        }
        if (report == null) {
            result.setSuccess(false);
            result.addError(String.format("报表重算失败，指定的报表（报表ID=%s）不存在！", reportRow.getReportId()));
            return result;
        }
        ProjectInfo project = null;
        IterationCycle iterationCycle = null;
        if (reportRow.getPeriod().equalsIgnoreCase("1")) {
            project = projectListService.getByProNo(report.getProjectId());
        } else if (reportRow.getPeriod().equalsIgnoreCase("2")) {
            iterationCycle = iterationCycleService.queryIteInfoById(reportRow.getPeriodId());
            project = projectListService.getByProNo(iterationCycle.getProNo());
        } else {
            result.setSuccess(false);
            result.addError(String.format("报表重算失败，报表类型（报表类型=%s）无法识别！", reportConfig.getType()));
            return result;
        }
        result = calculateKpis(report, project, iterationCycle, reportConfig, reportRow);
//        if (result.getErrors() != null && !result.getErrors().isEmpty()) {
//            baseResponse.setErrorMessage("error", String.join(";", result.getErrors()));
//        }
//        if (result.isSuccess()) {
//            //reportService.save(report);
//            result.setResult(String.format("报表重算完成（报表=%s）！", reportConfig.getName()));
//        }
        result.setResult(String.format("报表重算完成（报表=%s）！", reportConfig.getName()));
        return result;
    }


    /**
     * 根据项目、迭代、报表配置计算kpi指标的值
     *
     * @param project
     * @param iterationCycle
     * @param reportConfig
     * @param reportRow      报表行
     */
    public Result calculateKpis(Report report, ProjectInfo project, IterationCycle iterationCycle, ReportConfig reportConfig, ReportRow reportRow) {
        Result result = new Result();
        List<ReportKpi> calculateKpis = new ArrayList<>();
        List<ReportKpi> reportKpis = reportRow.getKpis();
        List<ReportKpiConfigRef> reportKpiConfigRefs = reportConfig.getKpiConfigRefs();

        try {
            //循环计算KPI的值
            for (ReportKpiConfigRef reportKpiConfigRef : reportKpiConfigRefs) {
                ReportKpiConfig reportKpiConfig = reportKpiConfigRef.getReportKpiConfig();
                ReportKpi reportKpi = findReportKpiByReportKpiConfig(reportKpis, reportKpiConfig);
                //报表配置reportConfig可能会修改，新增KPI指标配置
                //没有找到对应的KPI则创建
                if (reportKpi == null) {
                    reportKpi = new ReportKpi();
                    reportKpi.setReportKpiConfigId(reportKpiConfig.getId());
                    reportKpi.setReportRowId(reportRow.getId());
                    reportRow.addKpi(reportKpi);
                }
                if (reportKpiConfig.getKpiType().equals(ReportKpiConfig.TYPE_PROJECT_BASIC)
                        || reportKpiConfig.getKpiType().equals(ReportKpiConfig.TYPE_ITERATION_BASIC) ||
                        reportKpiConfig.getKpiType().equals(ReportKpiConfig.TYPE_PERSONNEL_BASIC) ||
                        reportKpiConfig.getKpiType().equals(ReportKpiConfig.TYPE_DEMAND_BASIC)) {
                    //设置基础KPI的值：如迭代名称，项目名称，迭代开始时间，迭代结束时间，项目开始时间，项目结束时间
                    setBasicKpiValue(reportRow, reportKpi);
                    reportKpi.setStatus(true);
                    calculateKpis.add(reportKpi);
                    continue;
                }
                //1.如果KPI中未配置计算表达式，则路过计算
                if (reportKpiConfig.getExpression() == null || StringUtils.isEmpty(reportKpiConfig.getExpression().trim())) {
                    continue;
                }

                //计算KPI的值
                Result calculateResult = calculateKpi(report, project, iterationCycle, reportKpiConfig, reportRow);
                result.setData(calculateResult.getData());
                if (calculateResult.getErrors() != null && !calculateResult.getErrors().isEmpty()) {
                    result.addErrors(calculateResult.getErrors());
                    LOG.error(calculateResult.getResult());
                }
                //如果计算成功则将结果保存到kpi中
                //如果KPI配置数据类型为整形，则将计算结果转换为整形
                Object data = calculateResult.getData();
                if (reportKpiConfig.getDataType() != null && reportKpiConfig.getDataType().equals("int")
                        && data != null && !data.equals("-") && !data.equals("")) {
                    Double val = Double.parseDouble(data.toString());
                    reportKpi.setValue(val.intValue() + "");
                } else {
                    reportKpi.setValue(calculateResult.getData().toString());
                }
                //处理点灯状态
                lightingAnalysis(reportKpiConfigRef, reportKpiConfig, reportKpi);
                if (reportKpi.getId() == null) {
                    calculateKpis.add(reportKpi);
                } else {
                    reportKpiService.updateById(reportKpi);
                }
            }
        } catch (Exception e) {
            StringBuilder error = new StringBuilder("计算报表KPI异常（项目=" + project.getName() + "，报表=" + reportConfig.getName());
            if (iterationCycle != null) {
                error.append("，迭代=" + iterationCycle.getIteName());
            }
            error.append("）");
            result.setResult(error.toString());
            result.addError(error.toString());
            result.setException(e);
            result.setSuccess(false);
            LOG.error(error.toString());
        }

        try {
            //清理KPI Ref
            Iterator<ReportKpi> iterator = reportKpis.iterator();
            while (iterator.hasNext()) {
                ReportKpi reportKpi = iterator.next();
                ReportKpiConfigRef reportKpiConfigRef = findReportKpiConfigRefByReportKpi(reportKpiConfigRefs, reportKpi);
                if (reportKpiConfigRef == null) {
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            StringBuilder error = new StringBuilder("清理报表KPI出现异常：项目=" + project.getName() + "，报表=" + reportConfig.getName());
            if (iterationCycle != null) {
                error.append("，迭代=" + iterationCycle.getProNo());
            }
            error.append("）");
            result.setResult(error.toString());
            result.addError(error.toString());
            result.setException(e);
            result.setSuccess(false);
            LOG.error(error.toString());
        }
        if (!calculateKpis.isEmpty()) {
            reportKpiService.saveOrUpdateBatch(calculateKpis);
        }
        return result;
    }

    private void lightingAnalysis(ReportKpiConfigRef reportKpiConfigRef, ReportKpiConfig reportKpiConfig, ReportKpi reportKpi) {
        reportKpi.setStatus(true);
        String lightUpRule = reportKpiConfig.getLightUpRule();
        Double maxValue = reportKpiConfig.getMaxValue();
        Double minValue = reportKpiConfig.getMinValue();
        Double targetValue = reportKpiConfig.getTargetValue();
        if (StringUtils.isNotEmpty(reportKpiConfigRef.getLightUpRule())) {
            lightUpRule = reportKpiConfigRef.getLightUpRule();
        }
        if (StringUtils.isNotEmpty(lightUpRule)) {
            if (StringUtils.isEmpty(reportKpi.getValue()) || "-".equals(reportKpi.getValue())) {
                reportKpi.setStatus(false);
            }
        }
        if (reportKpiConfigRef.getMaxValue() != null) {
            maxValue = reportKpiConfigRef.getMaxValue();
        }
        if (reportKpiConfigRef.getMinValue() != null) {
            minValue = reportKpiConfigRef.getMinValue();
        }
        if (reportKpiConfigRef.getTargetValue() != null) {
            targetValue = reportKpiConfigRef.getTargetValue();
        }
        String kpiValue = reportKpi.getValue();
        if (kpiValue == null || kpiValue.equals("") || kpiValue.equals("-")) {
            return;
        }
        double value = Double.parseDouble(kpiValue);
        if (lightUpRule.equalsIgnoreCase("min") && minValue != null) {
            if (value < minValue.doubleValue()) {
                reportKpi.setStatus(false);
            }
        } else if (lightUpRule.equalsIgnoreCase("max") && maxValue != null) {
            if (value > maxValue) {
                reportKpi.setStatus(false);
            }
        } else if (lightUpRule.equalsIgnoreCase("target") && targetValue != null) {
            if (value < targetValue) {
                reportKpi.setStatus(false);
            }
        }
    }

    //设置基础KPI的值：如迭代名称，项目名称，迭代开始时间，迭代结束时间，项目开始时间，项目结束时间
    private void setBasicKpiValue(ReportRow reportRow, ReportKpi reportKpi) {
        switch (reportKpi.getReportKpiConfigId()) {
            case -1010:
            case -910:
                reportKpi.setValue(reportRow.getPeriodName());
                break;
            case -1009:
            case -1008:
            case -909:
            case -908:
                reportKpi.setValue(DateFormatUtils.format(reportRow.getPeriodEndDate(), "yyyy/MM/dd"));
                break;
            case -710:
            case -810:
                reportKpi.setValue(reportRow.getCode());
                break;
            case -709:
            case -809:
                reportKpi.setValue(reportRow.getName());
                break;
        }
    }

    private Result calculateKpi(Report report, ProjectInfo project, IterationCycle iterationCycle, ReportKpiConfig reportKpiConfig, ReportRow reportRow) {

        Result result = null;
        try {
            Result<DataManager> dataManagerResult = loadDatas(report, project, iterationCycle, reportKpiConfig, reportRow);
            //数据源加载出错时直接返回错误信息，不计算此kpi
            if (!dataManagerResult.isSuccess()) {
                String error = "指标计算失败，" + dataManagerResult.getResult();
                result.setSuccess(false);
                result.addError(error);
                LOG.error(error);
                return result;
            }
            //如果提供的数据不完整，则将kpi的值设置为"-"
            result = expressionCalculateService.calculate(reportKpiConfig, dataManagerResult.getData());
        } catch (Exception e) {
            String error = "指标计算出现异常：" + reportKpiConfig.getKpiName();
            result.setSuccess(false);
            result.addError(error);
            LOG.error(error);
            return result;
        }
        return result;
    }

    //加载数据源，用于计算 指标，加载过程中有任何一个数据源加载失败，则返回错误
    private Result<DataManager> loadDatas(Report report, ProjectInfo project, IterationCycle iterationCycle, ReportKpiConfig reportKpiConfig, ReportRow reportRow) {
        DataManager dataManager = new DataManager();
        Result<DataManager> result = new Result<DataManager>(true, null, dataManager);
        MetricsItemConfig metricsItemConfig = null;
        MetricsTableConfig metricsTableConfig = null;
        try {
            //取得KPI配置中所有的数据源字段
            //TODO 这里需要一个获取KPI配置下所有度量项配置的方法
            reportKpiConfig = reportKpiConfigService.getFullById(reportKpiConfig.getId());
            List<MetricsItemConfig> metricsItemConfigs = reportKpiConfig.getMetricsItemConfigs();
            //List<MetricsItemConfig> metricsItemConfigs = (List<MetricsItemConfig>) metricsItemConfigService.listByIds(reportKpiConfig.getMetricsItemConfigIds());

            for (int i = 0; i < metricsItemConfigs.size(); i++) {
                metricsItemConfig = metricsItemConfigs.get(i);
                //TODO 这里需要一个根据度量表配置ID获取关联度量表配置的方法
                metricsTableConfig = metricsTableConfigService.getById(metricsItemConfig.getMetricsTableConfigId());

                //创建数据提供器
                AbstractProvider provider = null;
                if (metricsTableConfig.getVirtualTable()) {
                    if (Report.TYPE_PERSIONNEL.equalsIgnoreCase(report.getType())) {
                        //虚拟人员表提供器
                        provider = (AbstractProvider) ApplicationContextUtils.getBean("virtual_personnel_table_data_provider");
                    } else if (Report.TYPE_DEMAND.equalsIgnoreCase(report.getType())) {
                        //虚拟需求表提供器
                        provider = (AbstractProvider) ApplicationContextUtils.getBean("virtual_demand_table_data_provider");
                    } else {
                        //虚拟项目表提供器
                        provider = (AbstractProvider) ApplicationContextUtils.getBean("virtual_table_data_provider");
                    }
                } else {
                    provider = (AbstractProvider) ApplicationContextUtils.getBean(metricsTableConfig.getTableName().toLowerCase() + "_data_provider");
                }
                Result<List> dataResult = provider.getData(report, reportRow, project, iterationCycle, metricsItemConfig);
                if (dataResult == null || dataResult.getData() == null) {
                    String error = String.format("数据提供器未正确返回数据（指标=%s，表=%s，字段=%s）", reportKpiConfig.getKpiName(), metricsTableConfig.getTableName(), metricsTableConfig.getAlias());
                    result.setSuccess(false);
                    result.setResult(error);
                    //result.addError(error);
                    LOG.error(error);
                    return result;
                }
                dataManager.put(metricsItemConfig.getFieldName(), dataResult.getData());
            }
        } catch (Exception e) {
            String error = String.format("迭代数据获取异常，请检查数据表配置是否确（指标=%s，源数据表=%s，源字段名称=%s）", reportKpiConfig.getKpiName(), metricsTableConfig.getTableName(), metricsTableConfig.getAlias());
            result.setSuccess(false);
            result.setResult(error);
            //result.addError(error);
            LOG.error(error, e);
        }
        return result;
    }


    /**
     * 在KPI列表中找到包含指定指标名称的KPI
     *
     * @param reportKpis
     * @param reportKpiConfig
     * @return
     */
    private ReportKpi findReportKpiByReportKpiConfig(List<ReportKpi> reportKpis, ReportKpiConfig reportKpiConfig) {
        for (ReportKpi item : reportKpis) {
            if (item.getReportKpiConfigId().equals(reportKpiConfig.getId())) {
                return item;
            }
        }
        return null;
    }

    /**
     * 在KPI配置列表中找到包含指定指标名称的KPI配置
     *
     * @param reportKpiConfigRefs
     * @param reportKpi
     * @return
     */
    private ReportKpiConfigRef findReportKpiConfigRefByReportKpi(List<ReportKpiConfigRef> reportKpiConfigRefs, ReportKpi reportKpi) {
        for (ReportKpiConfigRef item : reportKpiConfigRefs) {
            if (item.getReportKpiConfigId().equals(reportKpi.getReportKpiConfigId())) {
                return item;
            }
        }
        return null;
    }

}
