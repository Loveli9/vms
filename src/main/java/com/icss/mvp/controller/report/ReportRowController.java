package com.icss.mvp.controller.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.icss.mvp.entity.common.response.Result;
import com.icss.mvp.service.report.ReportKpiService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.controller.BaseController;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.report.ReportRow;
import com.icss.mvp.service.report.ReportRowService;
import com.icss.mvp.service.report.calculate.CalculateService;

import java.util.List;

/**
 * Created by Ray on 2018/6/6.
 */
@Controller
@SuppressWarnings("all")
@RequestMapping("/report/report_row")
public class ReportRowController extends BaseController {

    private static Logger logger = Logger.getLogger(ReportRowController.class);

    @Autowired
    private ReportRowService service;

    @Autowired
    private CalculateService calculateService;

    @ResponseBody
    @RequestMapping("save")
    public PlainResponse save(@RequestBody ReportRow reportRow) {
        return PlainResponse.ok(service.merge(reportRow));
    }

    @ResponseBody
    @RequestMapping("get_full_by_id")
    public PlainResponse<ReportRow> getFullById(Integer id) {
        ReportRow datas = service.getFullById(id);
        return PlainResponse.ok(datas);
    }

    @ResponseBody
    @RequestMapping("get_by_id")
    public PlainResponse<ReportRow> getById(Integer id) {
        ReportRow reportRow = service.getById(id);
        return PlainResponse.ok(reportRow);
    }

    @ResponseBody
    @RequestMapping("delete")
    public PlainResponse<Boolean> deleteByIds(@RequestBody DeleteReuqest reuqest) {
        service.removeAllByIds(reuqest.getIds());
        return PlainResponse.ok(true);
    }

    /**
     * 重新计算
     *
     * @param reportId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "recalculate")
    public Result recalculate(String id) {
        Result result = null;
        try {
            result = calculateService.recalculateReport(id);
        } catch (Exception e) {
            result = new Result();
            result.setResult("重新计算发生异常！");
        }
        List<String> errors = result.getErrors();
        if (errors.size() > 0){
            result.setSuccess(false);
            result.setErrorsList(String.join("；</br>", errors));
        }
        return result;
    }

    public static class DeleteReuqest {
        private List<Integer> ids;

        public List<Integer> getIds() {
            return ids;
        }

        public void setIds(List<Integer> ids) {
            this.ids = ids;
        }
    }
}
