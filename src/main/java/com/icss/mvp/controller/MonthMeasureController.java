package com.icss.mvp.controller;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.MonthMeasure;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.service.MonthMeasureService;


/**
 * 月指标计算
 * @author chengchenhui
 *
 */
@Controller
@RequestMapping("/monthMeasure")
public class MonthMeasureController {
	private static Logger logger = Logger.getLogger(MonthMeasureController.class);
	
	@Autowired
	private MonthMeasureService monthMeasureService;
	
	/**
	* @Description:加载月指标实际值
	* @author Administrator  
	* @date 2018年5月25日  
	 */
	public ListResponse<MonthMeasure> geMonthtMeasureValue(String proNo) {
		ListResponse<MonthMeasure> result = new ListResponse<MonthMeasure>();
		try {
			List<MonthMeasure> list = monthMeasureService.geMonthtMeasureValue(proNo);
			result.setMessage("返回成功");
			result.setData(list);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("monthMeasureService.geMonthtMeasureValue exception, error: "+e.getMessage());
			result.setMessage("返回失败");
			result.setCode("failure");
		}
		return result;
	}
	
	
	/**
	* @Description: 计算月指标实际值 
	* @author Administrator  
	* @date 2018年5月25日  
	 */
	@RequestMapping("/calculate")
	@ResponseBody
	public ListResponse<MonthMeasure> calculateMeasure(String proNo) {
		ListResponse<MonthMeasure> result = new ListResponse<MonthMeasure>();
		try {
			List<MonthMeasure> list = monthMeasureService.calculateMeasure(proNo);
			result.setData(list);
			result.setCode("success");
			result.setMessage("返回成功");
		} catch (Exception e) {
			logger.error("monthMeasureService.calculateMeasure exception, error: "+e.getMessage());
			result.setMessage("返回失败");
			result.setCode("failure");
		}
		return result;
	}
	
}
