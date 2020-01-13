package com.icss.mvp.controller.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.icss.mvp.controller.BaseController;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.report.ReportConfigExcluded;
import com.icss.mvp.service.report.ReportConfigExcludedService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Ray on 2018/6/6.
 */
@Controller
@RequestMapping("report/report_config_excluded")
public class ReportConfigExcludedController extends BaseController {
    private static Logger logger = Logger.getLogger(ReportConfigExcludedController.class);
    @Autowired
    private ReportConfigExcludedService service;

    @ResponseBody
    @RequestMapping("list_by_project_no")
    public ListResponse<ReportConfigExcluded> listByProjectNo(String projectNo) {
        return ListResponse.ok(service.listByProjectNo(projectNo));
    }


    @ResponseBody
    @RequestMapping(value = "save")
    public PlainResponse save(@RequestBody SaveRequest saveRequest) {
        return service.saveOrUpdateBatch(saveRequest.getProjectNo(), saveRequest.getExcludeds());
    }

    public static class SaveRequest {
        private String projectNo;

        private List<ReportConfigExcluded> excludeds;

        public String getProjectNo() {
            return projectNo;
        }

        public void setProjectNo(String projectNo) {
            this.projectNo = projectNo;
        }

        public List<ReportConfigExcluded> getExcludeds() {
            return excludeds;
        }

        public void setExcludeds(List<ReportConfigExcluded> excludeds) {
            this.excludeds = excludeds;
        }
    }
}
