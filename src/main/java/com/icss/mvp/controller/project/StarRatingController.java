package com.icss.mvp.controller.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import org.apache.http.auth.AuthenticationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.icss.mvp.controller.BaseController;
import com.icss.mvp.entity.PlainRequest;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.service.project.ProjectService;
import com.icss.mvp.service.project.StarRatingService;

import javax.security.sasl.AuthenticationException;

@RestController
@RequestMapping("/starRating")
public class StarRatingController extends BaseController {

    private static Logger     logger = Logger.getLogger(StarRatingController.class);

    @Autowired
    private StarRatingService service;

    @Autowired
    private ProjectService    projectService;

    @RequestMapping("/summarize")
    @ResponseBody
    public PlainRequest<Map> summarizeStarRatings(ProjectInfo projectInfo, String type) {
        PlainRequest<Map> result = new PlainRequest<>();

        try {
            // 1:中软 2:华为 3:地域
            // get user authentication info
            authentication(projectInfo, type);

            result = service.summarizeRating(projectInfo, type);
        } catch (AuthenticationException e) {
            logger.error("summarizeStarRatings occurs AuthenticationException");
            result.setData(new HashMap<>());
        } catch (Exception e) {
            logger.error("summarizeStarRatings exception, error:" + e.getMessage());
            result.setData(new HashMap<>());
        }

        return result;
    }

    /**
     * 根据星级查询项目列表
     * 
     * @param project
     * @return
     */
    @RequestMapping("/page")
    @ResponseBody
    public ListResponse<ProjectInfo> describeStarRatings(ProjectInfo project, String type) {
        ListResponse<ProjectInfo> result = new ListResponse<>();

        try {
            // 1:中软 2:华为 3:地域
            // get user authentication info
            authentication(project, null);

            result = projectService.describeProject(project, type, null, null);
        } catch (AuthenticationException e) {
            logger.error("describeStarRatings occurs AuthenticationException");
            result.setData(new ArrayList<>());
        } catch (Exception e) {
            logger.error("describeStarRatings exception, error:" + e.getMessage());
            result.setData(new ArrayList<>());
        }

        return result;
    }
}
