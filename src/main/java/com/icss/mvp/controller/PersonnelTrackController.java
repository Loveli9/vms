package com.icss.mvp.controller;

import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.service.PersonnelTrackService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author zhanghucheng
 * @date 2019/12/13
 */
@Controller
@RequestMapping("/personnelTrack")
public class PersonnelTrackController extends BaseController {

    @Autowired
    private PersonnelTrackService personnelTrackService;

    private static Logger logger = Logger.getLogger(PersonnelTrackService.class);

    /**
     * 整体评估
     *
     * @param projectId
     * @param duty
     * @return
     */
    @RequestMapping(value = "/overallAssessment")
    @ResponseBody
    public TableSplitResult<List<Map<String, String>>> metricRespective(String projectId, String duty) {
        TableSplitResult<List<Map<String, String>>> result = new TableSplitResult<>();
        try {
            result = personnelTrackService.metricRespective(request, projectId, duty);
        } catch (Exception e) {
            logger.error("overallAssessment exception, error:", e);
        }
        return result;
    }

    /**
     * 质量问题
     *
     * @param pageRequest
     * @return
     */
    @RequestMapping("/queryQualityProblem")
    @ResponseBody
    public TableSplitResult<List<Map<String, String>>> queryQualityProblem(PageRequest pageRequest) {
        TableSplitResult<List<Map<String, String>>> result = new TableSplitResult<>();
        try {
            result = personnelTrackService.queryQualityProblem(pageRequest);
        } catch (Exception e) {
            logger.error("queryQualityProblem exception, error:", e);
        }
        return result;
    }

    /**
     * 项目群人员跟踪
     *
     * @param pageRequest
     * @return
     */
    @RequestMapping("/groupPersonTrack")
    @ResponseBody
    public TableSplitResult<List<Map<String, String>>> groupPersonTrack(PageRequest pageRequest) {
        return personnelTrackService.queryGroupPersonTrack(pageRequest);
    }
}
