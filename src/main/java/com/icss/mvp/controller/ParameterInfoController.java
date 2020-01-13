package com.icss.mvp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.service.ProjectParameterService;

/**
 * @author fwx548114
 */
@Controller
@RequestMapping("/paraInfo")
public class ParameterInfoController {

    @Autowired
    private ProjectParameterService projectService;

    // @RequestMapping("/query")
    // @ResponseBody
    // public Map<String,Object> queryParameterInfo(String projNo,Page page)
    // {
    // return projectService.queryProjectParameter(projNo,page);
    // }

    // //查询数据源
    // @RequestMapping("/queryprojSV")
    // @ResponseBody
    // public Map<String,Object> queryDataSource()
    // {
    // return projectService.queryDataSource();
    // }

    // @RequestMapping("/updateParam")
    // @ResponseBody
    // public String updateProjectParameter(ProjectParameter projParam){
    // String message=projectService.updateProjectParameter(projParam);
    // return message;
    // }
    /*
     * @RequestMapping("/addParam")
     * @ResponseBody public String addProjectParameter(ParameterInfo paramInfo,String projNo,int isDisplay) { String
     * message=projectService.addProjectParameter(projNo,paramInfo,isDisplay); return message; }
     * @RequestMapping("/delParam")
     * @ResponseBody public String delProjectParameter(String projNo,ParameterInfo paramInfo){ String
     * message=projectService.delProjectParameter(projNo,paramInfo); return message; }
     */

    /**
     * 工程能力折线图
     * 
     * @param projectId
     * @param paraName
     * @return
     */
    @RequestMapping("/processCapability")
    @ResponseBody
    public List<Map<String, Object>> processCapability(String projectId, String paraName) {
        return projectService.processCapability(projectId, paraName);
    }

    /**
     * 工程能力值获取
     * 
     * @param projectId
     * @param parameters
     * @return
     */
    @RequestMapping("/getProcessCapability")
    @ResponseBody
    public List<Map<String, Object>> getProcessCapability(String projectId, String parameters) {
        return projectService.getProcessCapability(projectId, parameters);
    }

}
