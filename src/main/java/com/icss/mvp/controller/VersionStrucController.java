package com.icss.mvp.controller;

import java.util.HashMap;
import java.util.Map;

import com.icss.mvp.util.StringUtilsLocal;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.VersionStrucService;

/**
 * 6+1 ICP-CI 指标 计算(版本级构建时长)
 * 
 * @author Administrator
 */
@Controller
@SuppressWarnings("all")
@RequestMapping("/icp-ci")
public class VersionStrucController {

    @Autowired
    private VersionStrucService service;
    private static Logger logger = Logger.getLogger(VersionStrucController.class);

    /**
     * @Description: 获取版本级构建时长指标项目级 (图标)
     * @author Administrator
     * @date 2018年6月20日
     * @version V1.0
     */
    @RequestMapping("/query")
    @ResponseBody
    public Map<String, Object> versionStrucMinute(String no, String yearNow) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = service.queryVersionStrucInfo(no, yearNow);
            map.put("code", 200);
        } catch (Exception e) {
            map.put("code", 400);
            logger.error("service.queryVersionStrucInfo exception, error: "+e.getMessage());
        }
        return map;
    }

    /**
     * @Description: 获取最新指标值项目级(气泡)
     * @author Administrator
     * @date 2018年6月20日
     * @version V1.0
     */
    @RequestMapping("/queryNew")
    @ResponseBody
    public Map<String, Object> versionStrucMinute(String no) {
        Map<String, Object> map = new HashMap<String, Object>();
        double value = 0;
        try {
            value = service.queryVersionStrucInfoNew(no);
            map.put("code", 200);
            map.put("result", value);
        } catch (Exception e) {
            map.put("code", 400);
            logger.error("service.queryVersionStrucInfoNew exception, error: "+e.getMessage());
        }
        return map;
    }

    /**
     * @Description:查询部门级别6+1指标值(气泡)
     * @author Administrator
     * @date 2018年7月3日
     */
    @RequestMapping("/queryIndex")
    @ResponseBody
    public Map<String, Object> queryIndexs(ProjectInfo proj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map = service.queryIndexs(proj);
        return map;
    }

    /**
     * @Description:获取部门级别版本构建时长
     * @author Administrator
     * @date 2018年7月4日
     * @version V1.0
     */
    @RequestMapping("/queryBbsc")
    @ResponseBody
    public Map<String, Object> queryBbscByNos(ProjectInfo proj) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = service.queryBbscByNos(proj);
            map.put("code", 200);
        } catch (Exception e) {
            map.put("code", 400);
            logger.error("service.queryBbscByNos exception, error: "+e.getMessage());
        }
        return map;
    }

    /**
     * @Description:从icp-ci服务采集版本构建及工程能力数据
     * @author Administrator
     * @date 2018年7月9日
     */
    @RequestMapping("/collectData")
    @ResponseBody
    public PlainResponse icpCiCollect(String no, String token, String id) {
        // 开始采集时间
        PlainResponse result = new PlainResponse();
        Map<String, Object> map = new HashMap<String, Object>();
        long startTime = System.currentTimeMillis();
        int a = service.icpCiCollect(no, token, id);
        long endTime = System.currentTimeMillis();// 采集结束时间
        String mesType = "更新ICPCI数据......";
        String zxResult = "更新成功";
        result.setCode("success");
        if (a == 1) {
            zxResult = "更新异常";
            result.setCode("fail");
        }
        map.put("mesType", mesType);
        map.put("zxResult", zxResult);
        map.put("workTime", StringUtilsLocal.formatTime(endTime - startTime));
        result.setData(map);
        return result;
    }

}
