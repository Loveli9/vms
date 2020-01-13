package com.icss.mvp.controller.report;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.controller.BaseController;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.report.ReportCheck;
import com.icss.mvp.service.report.ReportCheckService;
import com.icss.mvp.util.CollectionUtilsLocal;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.icss.mvp.entity.common.response.PlainResponse;

import java.util.List;

/**
 * @Description:
 * @Author:xhy
 * @Date2019/12/25
 **/
@Controller
@SuppressWarnings("all")
@RequestMapping("/report/report_check")
public class ReportCheckController extends BaseController {
    private static Logger logger = Logger.getLogger(ReportCheckController.class);
    @Autowired
    private ReportCheckService service;


    @RequestMapping(value = "/submit")
    @ResponseBody
    public BaseResponse submit(@RequestBody ReportCheck reportCheck) {
        BaseResponse result = new BaseResponse();
        try {
            service.submit(reportCheck);
            result.setMessage("提交审核成功！");
        } catch (Exception e) {
            logger.error("提交审核出现异常：", e);
            result.setError(CommonResultCode.EXCEPTION, "提交审核出现异常");
        }
        return result;
    }


    @RequestMapping(value = "/cancel")
    @ResponseBody
    public BaseResponse cancel(Integer id) {
        BaseResponse result = new BaseResponse();
        try {
            boolean success = service.cancel(id);
            result.setMessage("撤回审核操作成功！");
        } catch (Exception e) {
            logger.error("撤回审核操作出现异常.", e);
            result.setError(CommonResultCode.EXCEPTION, "撤回审核操作.");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "get_full_by_id")
    public PlainResponse<ReportCheck> getFullById(Integer id) {
        ReportCheck check = service.getFullById(id);
        return PlainResponse.ok(check);
    }

    @ResponseBody
    @RequestMapping(value = "page_by_qa")
    public BaseResponse pageByQa(Page page, String qa) {
        if (StringUtils.isEmpty(qa)) {
            return PlainResponse.fail("参数：“projectId”不能为空！");
        }
        IPage pageResult = service.pageFullByQa(page, qa);
        ListResponse response = PageResponse.ok(pageResult.getRecords()).pageNumber(pageResult.getCurrent()).pageSize(pageResult.getSize()).totalCount(pageResult.getTotal());
        return response;
    }

    @RequestMapping(value = "/pass")
    @ResponseBody
    public BaseResponse pass(Integer id) {
        BaseResponse result = new BaseResponse();
        try {
            service.pass(id);
            result.setMessage("提交审核成功！");
        } catch (Exception e) {
            logger.error("提交审核出现异常：", e);
            result.setError(CommonResultCode.EXCEPTION, "提交审核出现异常");
        }
        return result;
    }

    @RequestMapping(value = "/failed")
    @ResponseBody
    public BaseResponse failed(Integer id, String description) {
        BaseResponse result = new BaseResponse();
        try {
            if (StringUtils.isEmpty(description)) {
                result.setError(CommonResultCode.INVALID_PARAMETER, "请输入审核不通过的原因！");
                return result;
            }
            service.failed(id, description);
            result.setMessage("提交审核成功！");
        } catch (Exception e) {
            logger.error("提交审核出现异常：", e);
            result.setError(CommonResultCode.EXCEPTION, "提交审核出现异常");
        }
        return result;
    }
}
