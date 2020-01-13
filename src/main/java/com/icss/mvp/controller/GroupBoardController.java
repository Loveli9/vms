package com.icss.mvp.controller;

import com.icss.mvp.entity.GroupAcceptanceEntity;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectStateNumber;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.service.GroupBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * 组织看板 &项目看板
 *
 * @author limingming
 */
@Controller
@RequestMapping("/groupBoard")
@SuppressWarnings("all")
public class GroupBoardController {

    @Autowired
    private GroupBoardService groupBoardService;

    /**
     * 组织看板总览表
     *
     * @param projectInfo
     * @return
     */
    @RequestMapping("/groupOverview")
    @ResponseBody
    public TableSplitResult<List<GroupAcceptanceEntity>> queryGroupOverview(ProjectInfo projectInfo) {
        return groupBoardService.queryGroupOverview(projectInfo);
    }

    /**
     * 组织看板总览饼图
     *
     * @param projectInfo
     * @return
     */
    @RequestMapping("/groupOverviewPieChart")
    @ResponseBody
    public Map<String, Object> groupOverviewPieChart(ProjectInfo projectInfo) {
        return groupBoardService.groupOverviewPieChart(projectInfo);
    }

    /**
     * 组织看板总览柱图
     *
     * @param projectInfo
     * @param riskCategory
     * @return
     */
    @RequestMapping("/groupOverviewHistogram")
    @ResponseBody
    public Map<String, Object> groupOverviewHistogram(ProjectInfo projectInfo, String riskCategory) {
        return groupBoardService.groupOverviewHistogram(projectInfo, riskCategory);
    }

    /**
     * 组织看板验收表
     *
     * @param projectInfo
     * @return
     */
    @RequestMapping("/groupAcceptance")
    @ResponseBody
    public TableSplitResult<List<GroupAcceptanceEntity>> queryGroupAcceptance(ProjectInfo projectInfo) {
        return groupBoardService.queryGroupAcceptance(projectInfo);
    }

    /**
     * 项目看板执行表
     *
     * @param projectInfo
     * @param pageRequest
     * @return
     */
    @RequestMapping("/projectExecute")
    @ResponseBody
    public TableSplitResult<List<ProjectStateNumber>> queryProjectExecute(ProjectInfo projectInfo, PageRequest pageRequest) {
        return groupBoardService.queryProjectExecute(projectInfo, pageRequest);
    }

    /**
     * 项目看板验收表
     *
     * @param projectInfo
     * @param pageRequest
     * @return
     */
    @RequestMapping("/projectAcceptance")
    @ResponseBody
    public TableSplitResult<List<GroupAcceptanceEntity>> projectAcceptance(ProjectInfo projectInfo, PageRequest pageRequest) {
        return groupBoardService.queryProjectAcceptance(projectInfo, pageRequest);
    }

}
