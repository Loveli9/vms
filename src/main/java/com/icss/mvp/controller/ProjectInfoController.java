package com.icss.mvp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icss.mvp.entity.PageInfo;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectInfoVo;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.service.ProjectInfoService;
import com.icss.mvp.util.CookieUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projectInfo")
public class ProjectInfoController {
    private static Logger logger = Logger.getLogger(ProjectInfoController.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ProjectInfoService projectInfoService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> list(@ModelAttribute ProjectInfo projectInfo, @ModelAttribute PageInfo pageInfo){
        Map<String, Object> map = new HashMap<>();
        try {
            String username = CookieUtils.value(request, CookieUtils.USER_NAME);
            projectInfo.setName(URLDecoder.decode(projectInfo.getName(), "UTF-8"));
            projectInfo.setPm(URLDecoder.decode(projectInfo.getPm(), "UTF-8"));
            projectInfo.setProjectState(URLDecoder.decode(projectInfo.getProjectState(), "UTF-8"));
            List<ProjectInfoVo> projectInfoVos = projectInfoService.projectInfos(projectInfo, username, pageInfo);
//            map.put("pageInfo", jsonObject.get("pageInfo"));
//            map.put("data", jsonObject.get("data"));
//            map.put("msg", "返回成功");
//            map.put("status", "0");
            String title = "[{\"field\":\"项目名称\",\"title\":\"项目名称\"},{\"field\":\"项目经理\",\"title\":\"项目经理\"},{\"field\":\"地域\",\"title\":\"地域\"},{\"field\":\"华为产品线\",\"title\":\"华为产品线\"},{\"field\":\"子产品线\",\"title\":\"子产品线\"},{\"field\":\"PDU/SPDT\",\"title\":\"PDU/SPDT\"},{\"field\":\"计费类型\",\"title\":\"计费类型\"},{\"field\":\"项目状态\",\"title\":\"项目状态\"}]";
            Object gridTitles = JSON.parse(title);
            map.put("gridTitles", gridTitles);
            JSONObject json = new JSONObject();
//            json.put("total", pageInfo.getTotalRecord());
            json.put("total", projectInfoVos.size());
            JSONArray jsonArray = new JSONArray();
            for (ProjectInfoVo projectInfoVo : projectInfoVos) {
                JSONObject projectInfoObject = new JSONObject();
                projectInfoObject.put("项目名称", projectInfoVo.getName());
                projectInfoObject.put("项目经理", projectInfoVo.getPm());
                projectInfoObject.put("子产品线", projectInfoVo.getHwzpdu());
                projectInfoObject.put("华为产品线", projectInfoVo.getHwpdu());
                projectInfoObject.put("计费类型", projectInfoVo.getType());
                projectInfoObject.put("地域", projectInfoVo.getArea());
                projectInfoObject.put("PDU/SPDT", projectInfoVo.getPduSpdt());
                projectInfoObject.put("项目状态", projectInfoVo.getProjectState());
                projectInfoObject.put("项目编码", projectInfoVo.getNo());
                jsonArray.add(projectInfoObject);
            }
            json.put("rows", jsonArray);
            map.put("gridDatas", json);
        }catch (Exception e){
            logger.error("projectInfoService.projectInfos exception, error: "+e.getMessage());
            map.put("msg", "返回失败");
            map.put("status", "1");
        }
        return map;
    }
}
