package com.icss.mvp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.icss.mvp.entity.LabelMeasureConfig;
import com.icss.mvp.service.LabelMeasureConfigService;

/**
 * 
* <p>Title: LabelMeasureConfigController</p>  
* <p>Description: </p>  
* @author gaoyao  
* @date 2018年5月10日下午3:41:08
 */
@RestController
@RequestMapping("/labelMeasureConfig")
public class LabelMeasureConfigController {
	private static Logger logger = Logger.getLogger(LabelMeasureConfigController.class);
	
	@Resource
	private HttpServletRequest request;

	@Resource
	private LabelMeasureConfigService labelMeasureConfigService;
	
	
	/**
	 * <p>Title: getAllLabelMeasureConfig</p>
	 * <p>Description: 查询所有的 指标配置表 信息</p>
	 * @author gaoyao
	 * @param pId
	 * @return
	 */
	@RequestMapping("/getAllLabelMeasureConfig")
	@ResponseBody
	public Map<String, Object> getAllLabelMeasureConfig(String pId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<LabelMeasureConfig> list = labelMeasureConfigService.getAllLabelMeasureConfig();
			map.put("data", list);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("labelMeasureConfigService.getAllLabelMeasureConfig exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}
}
