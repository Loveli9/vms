package com.icss.mvp.controller.report;

import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.controller.BaseController;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.report.MetricsItemConfig;
import com.icss.mvp.entity.report.ReportKpiConfig;
import com.icss.mvp.service.report.MetricsItemConfigService;
import com.icss.mvp.service.report.ReportKpiConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 报表指标管理
 * Created by Ray on 2018/6/6.
 */
@Controller
@SuppressWarnings("all")
@RequestMapping("/report/reportKpiConfig")
public class ReportKpiConfigController extends BaseController {

    private static Logger logger = Logger.getLogger(ReportKpiConfigController.class);

    @Autowired
    private ReportKpiConfigService service;
    @Autowired
    private MetricsItemConfigService metricsItemConfigService;

    /**
     * 指标配置新增或更新
     **/
    @RequestMapping(value = "/save", consumes = "application/json")
    @ResponseBody
    public BaseResponse save(@RequestBody ReportKpiConfig reportKpiConfig) {
        BaseResponse result = new BaseResponse();
        if (reportKpiConfig.getId() != null && reportKpiConfig.getId() <= 0) {
            result.setMessage("基础指标禁止修改!");
            result.setCode(CommonResultCode.INVALID_PARAMETER.code);
            return result;
        }
        try {
            service.saveOrUpdate(reportKpiConfig);
            result.setCode(CommonResultCode.SUCCESS.code);
            result.setMessage("保存报表指标配置成功！");
        } catch (Exception e) {
            logger.error("保存报表指标配置异常：", e);
            result.setCode(CommonResultCode.EXCEPTION.code);
            result.setMessage("保存报表指标配置失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 根据id查询指标配置
     *
     * @param kpiConfigId
     * @return
     */
    @RequestMapping(value = "/get_full_by_id")
    @ResponseBody
    public PlainResponse<ReportKpiConfig> getFullById(Integer id) {
        ReportKpiConfig reportKpiConfig = service.getFullById(id);
        return PlainResponse.ok(reportKpiConfig);
    }

    /**
     * 根据度量项配置id查询关联的指标配置列表
     *
     * @param mictricsItemConfigId
     * @return
     */
    @RequestMapping(value = "/getKpiConfListByMictricsItemConfigId", consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public PlainResponse<ReportKpiConfig> getByMictricsItemConfigId(Integer mictricsItemConfigId) {
        return PlainResponse.ok(service.getByMictricsItemConfigId(mictricsItemConfigId));
    }

    /**
     * 根据指标名称模糊查询指标列表，没有指标名称查全部
     *
     * @param kpiName
     * @param pageRequest
     * @return
     */
    @RequestMapping(value = "/query", consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public TableSplitResult<List<ReportKpiConfig>> query(String kpiName, PageRequest pageRequest) {
        return service.queryByName(kpiName, pageRequest);
    }


    /**
     * 删除指标配置及关联度量项
     *
     * @param reportKpiConfigIds
     * @return BaseResponse
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public BaseResponse delete(@RequestBody List<String> ids) {
        BaseResponse result = new BaseResponse();
        try {
            boolean count = service.removeByIds(ids);
            if (!count) {
                result.setCode(CommonResultCode.RESOURCE_ALREADY_EXIST.code);
                result.setMessage("指标配置被引用或查询不到数据,删除失败！");
            } else {
                result.setCode(CommonResultCode.SUCCESS.code);
                result.setMessage("指标配置及度量项关联数据" + count + "条被成功删除！");
            }
        } catch (Exception e) {
            logger.error("删除指标配置失败：", e);
            result.setCode(CommonResultCode.EXCEPTION.code);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 查询不含传入ID以外的度量项
     *
     * @param mictricsItemConfigId(,分隔)
     * @return
     */
    @RequestMapping("/queryExcluedMictricsItemsByIds")
    @ResponseBody
    public TableSplitResult<List<MetricsItemConfig>> queryExcluedMictricsItemsByIds(String mictricsItemConfigIds, PageRequest pageRequest) {
        return service.queryExcluedMictricsItemsByIds(mictricsItemConfigIds, pageRequest);
    }

}
