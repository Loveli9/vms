package com.icss.mvp.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.icss.mvp.entity.*;
import com.icss.mvp.entity.common.response.ListResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.service.MemberInforService;

@Controller
@RequestMapping("/MemberInforController")
public class MemberInforController {
    private final static Logger LOG = Logger.getLogger(MemberInforController.class);

    @Autowired
    private MemberInforService MemberInforService;

    @RequestMapping("/projectInformation")
    @ResponseBody
    public ProjectInformation projectInformation(String projectId) {
        ProjectInformation date = MemberInforService.projectInformation(projectId);
        return date;
    }

    @RequestMapping("/memberinformation")
    @ResponseBody
    public List<ProjectPostInfor> memberinformation(String projectId) {
        List<ProjectPostInfor> date = MemberInforService.memberinformation(projectId);
        return date;
    }

    @RequestMapping("/saveMembersInformation")
    @ResponseBody
    public BaseResponse saveMembersInformation(@RequestBody ProjectMembersInformation project) {
        BaseResponse result = new BaseResponse();
        try {
            MemberInforService.saveMembersInformation(project);
            result.setCode("success");
        } catch (Exception e) {
            LOG.error("编辑失败：", e);
            result.setCode("failure");
            result.setMessage("操作失败");
        }
        return result;
    }

    @RequestMapping("/queryMembersInformation")
    @ResponseBody
    public TableSplitResult queryMembersInformation(String projectId,
                                                    Integer limit, Integer offset) {
        TableSplitResult page = new TableSplitResult();
        page.setPage(offset);
        page.setRows(limit);
        page = MemberInforService.queryMembersInformation(projectId, limit, offset);
        return page;
    }

    @RequestMapping("/editDataPosts")
    @ResponseBody
    public ProjectMembersInformation editDataPosts(String id) {
        ProjectMembersInformation data = new ProjectMembersInformation();
        try {
            data = MemberInforService.editDataPosts(id);
        } catch (Exception e) {
            LOG.error("查找失败：", e);
        }
        return data;
    }

    @RequestMapping("/saveProjectInformation")
    @ResponseBody
    public BaseResponse saveProjectInformation(@RequestBody ProjectMembersInformation project) {
        BaseResponse result = new BaseResponse();
        try {
            MemberInforService.saveProjectInformation(project);
            result.setCode("success");
        } catch (Exception e) {
            LOG.error("编辑失败：", e);
            result.setCode("failure");
            result.setMessage("操作失败");
        }
        return result;
    }

    /**
     * excel导入成员
     *
     * @param file
     * @param proNo
     * @param request
     * @return
     */
    @RequestMapping("/importMembers")
    @ResponseBody
    public Map<String, Object> importMembers(@RequestParam(value = "file") MultipartFile file,
                                             @RequestParam(value = "proNo") String proNo,
                                             HttpServletRequest request) {
        Map<String, Object> result = MemberInforService.importMembers(file, proNo, request);
        return result;
    }

    /**
     * 成员导出
     *
     * @param response
     * @param request
     */
    @RequestMapping("/exportMembers")
    @ResponseBody
    public void exportMembers(HttpServletResponse response, HttpServletRequest request) {
        String proNo = StringUtils.isEmpty(request.getParameter("proNo1")) ? "" : request.getParameter("proNo1");

        try {
            byte[] fileContents = MemberInforService.exportMembers(proNo);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = "成员信息" + sf.format(new Date()).toString() + ".xlsx";
            //设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(fileName.getBytes(), "iso-8859-1"));

            response.getOutputStream().write(fileContents);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            LOG.error("导出失败", e);
        }
    }

    @RequestMapping(value = "/list_by_project_no")
    @ResponseBody
    public List<Map<String, Object>> listByProjectNo(String projectNo) {
        List<ProjectMembersInformation> list = MemberInforService.listByProjectNo(projectNo);
        if (list.isEmpty()) {
            return null;
        } else {
            List<Map<String, Object>> datas = new ArrayList<>(0);
            for (ProjectMembersInformation member : list) {
                datas.add(new HashMap() {{
                    put("name", member.getName());
                    put("zrAccount", member.getZrAccount());
                    put("no", member.getNo());
                }});
            }
            return datas;
        }
    }
}
