package com.icss.mvp.controller;

import com.icss.mvp.entity.Measure;
import com.icss.mvp.service.MeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/measure")
public class MeasureController {
    private static Logger logger = Logger.getLogger(MeasureController.class);

    @Autowired
    private MeasureService measureService;


    /**
     * <p>Title: getMeasureByWD</p>
     * <p>Description: 通过wd查询度量指标表记录</p>
     *
     * @param wd
     * @return
     * @author gaoyao
     */
    @RequestMapping("/getMeasureByWD")
    @ResponseBody
    public Map<String, Object> getMeasureByWD(String wd) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Measure> list = measureService.getMeasureByWD(wd);
            map.put("data", list);
            map.put("msg", "返回成功");
            map.put("status", "0");
        } catch (Exception e) {
            logger.error("measureService.getMeasureByWD exception, error: "+e.getMessage());
            map.put("msg", "返回失败");
            map.put("status", "1");
        }
        return map;
    }

    /**
     * <p>Title: getAllMeasure</p>
     * <p>Description: 查询全部Measure</p>
     *
     * @return
     * @author gaoyao
     */
    @RequestMapping("/getAllMeasure")
    @ResponseBody
    public Map<String, Object> getAllMeasure() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Measure> list = measureService.getAllMeasure();
            map.put("data", list);
            map.put("msg", "返回成功");
            map.put("status", "0");
        } catch (Exception e) {
            logger.error("measureService.getAllMeasure exception, error: "+e.getMessage());
            map.put("msg", "返回失败");
            map.put("status", "1");
        }
        return map;
    }


    /**
     * <p>Title: deleteMeasureById</p>
     * <p>Description: 删除一个Measure</p>
     *
     * @param id
     * @return
     * @author gaoyao
     */
    @RequestMapping("/deleteMeasureById")
    @ResponseBody
    public Map<String, Object> deleteMeasureById(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            int num = measureService.deleteMeasureById(id);
            if (num == 1) {
                map.put("msg", "删除成功");
                map.put("status", "0");
            } else {
                map.put("msg", "删除失败");
                map.put("status", "1");
            }

        } catch (Exception e) {
            logger.error("measureService.deleteMeasureById exception, error: "+e.getMessage());
            map.put("msg", "删除失败");
            map.put("status", "1");
        }
        return map;
    }
}
