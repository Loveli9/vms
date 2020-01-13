package com.icss.mvp.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectInfoVo;
import com.icss.mvp.service.BuOverviewService;
import com.icss.mvp.service.ProjectInfoService;
import com.icss.mvp.service.ProjectScheduleService;
import com.icss.mvp.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 项目进度
 */
@RestController
@RequestMapping("/projectSchedule")
public class ProjectScheduleController {
    private static Logger logger = Logger.getLogger(ProjectScheduleController.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ProjectInfoService projectInfoService;
    @Autowired
    private ProjectScheduleService projectScheduleService;


    @RequestMapping(value = "/warning", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> warning(@ModelAttribute ProjectInfo projectInfo, @RequestParam int step){
        Map<String, Object> map = new HashMap<>();
        try {
            String userName = CookieUtils.value(request, CookieUtils.USER_NAME);
            Set<String> projNos = projectInfoService.projectNos(projectInfo, userName);
            if (CollectionUtils.isEmpty(projNos)){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("sum", 0);
                map.put("data", jsonObject);
                map.put("msg", "返回成功");
                map.put("status", "0");
                return map;
            }
            List<Map<String, Object>> list = projectScheduleService.projectScheduleList(projNos, step);
            int sum = 0;
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            Map<String, List<String>> warnings = new HashMap<>();
            for (Map<String, Object> warningMap : list) {
                String pdu = warningMap.get("PDU").toString();
                String no = warningMap.get("NO").toString();
                if (null != warnings.get(pdu)){
                    warnings.get(pdu).add(no);
                }else {
                    List<String> nos = new ArrayList<>();
                    nos.add(no);
                    warnings.put(pdu, nos);
                }
                sum++;
            }
            for (Map.Entry<String, List<String>> entry : warnings.entrySet()) {
                JSONObject warning = new JSONObject();
                warning.put("pdu", entry.getKey());
                warning.put("nos", entry.getValue());
                warning.put("num", entry.getValue().size());
                jsonArray.add(warning);
            }
            jsonObject.put("detail", jsonArray);
            jsonObject.put("sum", sum);
            map.put("data", jsonObject);
            map.put("msg", "返回成功");
            map.put("status", "0");
        }catch (Exception e){
            logger.error("projectScheduleService.projectScheduleList exception, error: "+e.getMessage());
            map.put("msg", "返回失败");
            map.put("status", "1");
        }
        return map;
    }

    @RequestMapping(value = "/warningList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> warningList(@RequestParam(name = "nos") String nos,
                                           @RequestParam(name = "name", required = false) String name){
        Map<String, Object> map = new HashMap<>();
        String[] noArray;
        if (null == nos){
            noArray = new String[]{""};
        }else {
            noArray = nos.split(",");
        }
        try {
            List<ProjectInfoVo> projectInfoVos = projectInfoService.projectScheduleList(noArray , name);
            map.put("data", projectInfoVos);
            map.put("msg", "返回成功");
            map.put("status", "0");
        }catch (Exception e){
            logger.error("projectInfoService.projectScheduleList exception, error: "+e.getMessage());
            map.put("msg", "返回失败");
            map.put("status", "1");
        }
        return map;
    }
}
