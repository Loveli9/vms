package com.icss.mvp.controller;

import com.icss.mvp.entity.DeliverResult;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.service.ProjectInspectService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projectInspect")
public class ProjectInspectController {

    private static Logger logger = Logger.getLogger(ProjectOverviewController.class);

    @Resource
    private ProjectInspectService projectInspectService;

    /**
     * 需求完成情况
     *
     * @param projectInfo
     * @param pageRequest
     * @param date
     * @return
     */
    @RequestMapping("/queryDemandFinish")
    @ResponseBody
    public TableSplitResult<List< Map<String,String>>> queryDemandFinish(ProjectInfo projectInfo, PageRequest pageRequest, String date) {
        TableSplitResult<List< Map<String,String>>> result = new TableSplitResult<>();
        try {
            result = projectInspectService.queryDemandFinish(projectInfo, pageRequest, date);
        } catch (Exception e) {
            logger.error("queryResourceReport exception, error:", e);
        }
        return result;
    }

    /**
     * 遗留DI
     *
     * @param projectInfo
     * @param pageRequest
     * @param date
     * @return
     */
    @RequestMapping("/queryLeftOver")
    @ResponseBody
    public TableSplitResult<List< Map<String,String>>> queryLeftOver(ProjectInfo projectInfo, PageRequest pageRequest, String date) {
        TableSplitResult<List< Map<String,String>>> result = new TableSplitResult<>();
        try {
            result = projectInspectService.queryLeftOver(projectInfo, pageRequest, date);
        } catch (Exception e) {
            logger.error("queryResourceReport exception, error:", e);
        }
        return result;
    }

    /**
     * 验收评价
     *
     * @param projectInfo
     * @param pageRequest
     * @param date
     * @return
     */
    @RequestMapping("/queryEvaluation")
    @ResponseBody
    public TableSplitResult<List< Map<String,String>>> queryEvaluation(ProjectInfo projectInfo, PageRequest pageRequest, String date) {
        TableSplitResult<List< Map<String,String>>> result = new TableSplitResult<>();
        try {
            result = projectInspectService.queryEvaluation(projectInfo, pageRequest, date);
        } catch (Exception e) {
            logger.error("queryResourceReport exception, error:", e);
        }
        return result;
    }
}
