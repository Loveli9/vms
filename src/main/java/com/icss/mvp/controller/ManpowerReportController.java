package com.icss.mvp.controller;

import com.alibaba.fastjson.JSONObject;
import com.icss.mvp.entity.ManpowerReport;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.service.ManpowerReportService;
import com.icss.mvp.service.ProjectInfoService;
import com.icss.mvp.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 人力统计
 */
@RestController
@RequestMapping("/manpowerReport")
public class ManpowerReportController {
    private static Logger logger = Logger.getLogger(ManpowerReportController.class);

    @Autowired
    private ManpowerReportService manpowerReportService;
    @Autowired
    private ProjectInfoService projectInfoService;
    @Autowired
    private HttpServletRequest request;
    /**
     * 计算关键角色胜任度
     * @return
     */
    @RequestMapping(value = "/keyRole", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> keyRole(@ModelAttribute ProjectInfo projectInfo){
        Map<String, Object> map = new HashMap<>();
        try {
            String userName = CookieUtils.value(request, CookieUtils.USER_NAME);
            Map<String, Object> paraMap = new HashMap<>();
            projectInfoService.setParamNew(projectInfo, userName, paraMap);
            List<ManpowerReport> manpowerReports = manpowerReportService.keyRole(paraMap);
            int competencySumNum = 0;
            int workingSumNum = 0;
            for (ManpowerReport manpowerReport : manpowerReports) {
                workingSumNum += manpowerReport.getWorkingNum();
                competencySumNum += manpowerReport.getCompetenceNum();
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("competencySumNum", competencySumNum);
            jsonObject.put("workingSumNum", workingSumNum);
            jsonObject.put("detail", JSONObject.toJSON(manpowerReports));
            map.put("data", jsonObject);
            map.put("msg", "返回成功");
            map.put("status", "0");
        }catch (Exception e){
            logger.error("JSONObject.toJSON exception, error: "+e.getMessage());
            map.put("msg", "返回失败");
            map.put("status", "1");
        }
        return map;
    }

    /**
     * 计算关键角色和人员到位率
     * @return
     */
    @RequestMapping(value = "/positionRate", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> positionRate(@ModelAttribute ProjectInfo projectInfo){
        Map<String, Object> map = new HashMap<>();
        try {
            String userName = CookieUtils.value(request, CookieUtils.USER_NAME);
            Map<String, Object> paraMap = new HashMap<>();
            projectInfoService.setParamNew(projectInfo, userName, paraMap);
            List<Map<String, String>> list = manpowerReportService.positionRate(paraMap);
            Iterator<Map<String, String>> iterator = list.iterator();
            while (iterator.hasNext()){
                Map<String, String> rateMap = iterator.next();
                if ("100%".equals(rateMap.get("keyRoleRate")) && "100%".equals(rateMap.get("memberRate"))){
                    iterator.remove();
                }
            }
            map.put("data", list);
            map.put("msg", "返回成功");
            map.put("status", "0");
        }catch (Exception e){
            logger.error("manpowerReportService.positionRate exception, error: "+e.getMessage());
            map.put("msg", "返回失败");
            map.put("status", "1");
        }
        return map;
    }

}
