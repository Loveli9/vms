package com.icss.mvp.controller.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.controller.BaseController;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.report.ReportConfig;
import com.icss.mvp.entity.report.ReportKpiConfig;
import com.icss.mvp.entity.report.ReportKpiConfigRef;
import com.icss.mvp.service.report.ReportConfigService;
import com.icss.mvp.service.report.ReportKpiConfigRefService;
import com.icss.mvp.service.report.ReportKpiConfigService;
import com.icss.mvp.service.report.ReportService;
import com.icss.mvp.util.CollectionUtilsLocal;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 报表配置
 *
 * @author:xhy
 * @create:2019/12/17 9:20
 **/
@Controller
@SuppressWarnings("all")
@RequestMapping("/report/reportConfig")
public class ReportConfigController extends BaseController {

    private static Logger logger = Logger.getLogger(ReportConfigController.class);

    @Autowired
    private ReportConfigService reportConfigService;
    @Autowired
    private ReportKpiConfigService reportKpiConfigService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportKpiConfigRefService reportKpiConfigRefService;

    /**
     * 根据ID询全部的查询报表配置
     *
     * @param reportId
     * @return ReportConfig
     */
    @RequestMapping(value = "/queryReportConfigsById", consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public PlainResponse<ReportConfig> getReportConfigById(Integer reportConfigId) {
        return PlainResponse.ok(reportConfigService.getById(reportConfigId));
    }

    /**
     * 根据ID获取报表配置，结果中包含指标配置引用
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("get_by_id")
    public PlainResponse<ReportConfig> getById(Integer id) {
        return PlainResponse.ok(reportConfigService.getFullById(id));
    }

    /**
     * @param kpiConfigId
     * @param page
     * @Description:根据指标配置ID查询引用的报表配置
     * @Date: 2019/12/23
     * @return: com.icss.mvp.entity.common.response.ListResponse<com.icss.mvp.entity.report.ReportConfig>
     **/
    @ResponseBody
    @RequestMapping("/queryReportConfigByKpiConfigId")
    public ListResponse<ReportConfig> queryReportConfigByKpiConfigId(Integer kpiConfigId, Page page) {
        ListResponse<ReportConfig> response = PageResponse.ok(new ArrayList()).totalCount(0).pageNumber(0).pageSize(0);
        if (kpiConfigId != null) {
            List<ReportKpiConfigRef> refs = reportKpiConfigRefService.getByKpiConfigId(kpiConfigId);
            List<Integer> reportConfigIds = new ArrayList<>();
            refs.forEach(reportKpiConfigRef -> {
                        if (!reportConfigIds.contains(reportKpiConfigRef.getReportConfigId())) {
                            reportConfigIds.add(reportKpiConfigRef.getReportConfigId());
                        }
                    }
            );
            if (CollectionUtils.isNotEmpty(reportConfigIds)) {
                QueryWrapper<ReportConfig> queryWrapper = Wrappers.query();
                IPage result = reportConfigService.page(page, queryWrapper.in("id", reportConfigIds));
                response = PageResponse.ok(result.getRecords()).totalCount(result.getTotal()).pageNumber(result.getCurrent()).pageSize(result.getSize());
            }
        }
        return response;
    }

    /**
     * @description:查询排除传入ID以外的指标配置
     * @author:xhy
     * @create:2019/12/20 17:50
     **/
    @ResponseBody
    @RequestMapping("/queryExcluedKpiConfigsByIds")
    public ListResponse<ReportKpiConfig> queryExcluedKpiConfigsByIds(String kpiName, String kpiTypes, String period, String kpiConfigIds, Page page) {
        ListResponse<ReportKpiConfig> response = PageResponse.ok(new ArrayList()).totalCount(0).pageNumber(0).pageSize(0);
        QueryWrapper<ReportKpiConfig> queryWrapper = Wrappers.query();
        if (StringUtils.isEmpty(kpiTypes)) {
            return ListResponse.fail("指标类型不能为空！");
        }

        //查询的指标类型与报表类型相同，同时加上基础指标
        queryWrapper.in("kpi_type", kpiTypes.split("[,]"));
        if (StringUtils.isNotBlank(kpiName)) {
            queryWrapper.like("kpi_name", kpiName);
        }
        if (StringUtils.isNotBlank(kpiConfigIds)) {
            List<Integer> ids = CollectionUtilsLocal.splitToList(kpiConfigIds, Integer.class);
            queryWrapper.notIn("id", ids);
        }
        IPage result = reportKpiConfigService.page(page, queryWrapper);
        response = PageResponse.ok(result.getRecords()).totalCount(result.getTotal()).pageNumber(result.getCurrent()).pageSize(result.getSize());
        return response;
    }

    /**
     * 新增或修改报表配置
     **/
    @RequestMapping(value = "/saveReportConfig", consumes = "application/json")
    @ResponseBody
    public BaseResponse saveReportConfig(@RequestBody ReportConfig reportConfig) {
        BaseResponse result = new BaseResponse();
        try {
            if (CollectionUtils.isEmpty(reportConfig.getKpiConfigRefs())) {
                result.setCode(CommonResultCode.EXCEPTION.code);
                result.setMessage("前端数据缺失！");
            } else {
                reportConfigService.saveReportConfig(reportConfig);
                result.setCode(CommonResultCode.SUCCESS.code);
                result.setMessage("保存报表配置成功！");
            }
        } catch (Exception e) {
            logger.error("保存报表配置异常：", e);
            result.setCode(CommonResultCode.EXCEPTION.code);
            result.setMessage("保存报表配置失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 根据ID删除无报表引用的配置
     *
     * @param reportConfigIds
     * @param BaseResponse
     * @return
     */
    @RequestMapping("/delReportConfigs")
    @ResponseBody
    public BaseResponse delReportConfigs(String reportConfigIds) {
        BaseResponse result = new BaseResponse();
        result.setCode(CommonResultCode.EXCEPTION.code);
        result.setMessage("报表配置删除失败！");
        try {
            Boolean isDel = false;
            if (StringUtils.isNotBlank(reportConfigIds)) {
                List<Integer> ids = CollectionUtilsLocal.splitToList(reportConfigIds, Integer.class);
                if (!CollectionUtils.isEmpty(ids)) {
                    Boolean isRef = reportService.isExistRefByReportConfigIds(ids);
                    if (!isRef) {//没有报表引用指标就删除
                        isDel = reportConfigService.removeByIds(ids);
                    } else {
                        result.setMessage("报表配置存在引用，删除失败！");
                    }
                }
            }
            if (isDel) {
                result.setCode(CommonResultCode.SUCCESS.code);
                result.setMessage("报表配置删除成功！");
            }
        } catch (Exception e) {
            logger.error("报表配置删除失败：", e);
            result.setCode(CommonResultCode.EXCEPTION.code);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "query_sytem_report")
    public ListResponse<ReportConfig> querySytemReport(Page page, String queryStr) {
        QueryWrapper<ReportConfig> query = Wrappers.query();
        if (StringUtils.isNotEmpty(queryStr)) {
            query.like("name", queryStr)
                    .or().like("description", queryStr);
        }
        query.and(wrapper -> {
            wrapper.isNull("project_id");
        });
        IPage result = reportConfigService.page(page, query);
        ListResponse response = PageResponse.ok(result.getRecords()).totalCount(result.getTotal()).pageNumber(result.getCurrent()).pageSize(result.getSize());
        return response;
    }

    /**
     * 查询项目报表（含系统报表,项目ID为空时为系统报表，不为空则对应该项目的自定义报表）
     *
     * @param projectNo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "list_excluded_by_project_no")
    public BaseResponse listExcludedByProjectNo(String projectNo) {
        if (StringUtils.isEmpty(projectNo)) {
            return PlainResponse.fail("参数：“projectNo”不能为空！");
        }
        List<ReportConfig> result = reportConfigService.listExcludedByProjectNo(projectNo);
        ListResponse response = ListResponse.ok(result);
        return response;
    }

    /**
     * 查询项目报表（含系统报表,项目ID为空时为系统报表，不为空则对应该项目的自定义报表）
     *
     * @param projectNo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "list_full_by_project_no")
    public BaseResponse listFullByProjectNo(String projectNo) {
        if (StringUtils.isEmpty(projectNo)) {
            return PlainResponse.fail("参数：“projectNo”D不能为空！");
        }
        List<ReportConfig> result = reportConfigService.listFullByProjectNo(projectNo);
        ListResponse response = ListResponse.ok(result);
        return response;
    }

    /**
     * 根据ID获取报表配置，结果中包含指标配置引用(排除指定项目的裁剪项)
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("get_excluded_by_id_and_project_no")
    public PlainResponse<ReportConfig> getExcludedByIdAndProjectNo(Integer id, String projectNo) {
        ReportConfig reportConfig = reportConfigService.getExcludedByIdAndProjectNo(id, projectNo);
        return PlainResponse.ok(reportConfig);
    }

}
