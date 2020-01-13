package com.icss.mvp.controller.report;

import com.icss.mvp.controller.BaseController;
import com.icss.mvp.service.report.ReportKpiService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Ray on 2018/6/6.
 */
@Controller
@SuppressWarnings("all")
@RequestMapping("/report/report_kpi")
public class ReportKpiController extends BaseController {

    private static Logger logger = Logger.getLogger(ReportKpiController.class);

    @Autowired
    private ReportKpiService service;

}
