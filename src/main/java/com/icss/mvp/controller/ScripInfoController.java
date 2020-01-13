package com.icss.mvp.controller;

import java.util.List;
import java.util.Map;

import com.icss.mvp.constant.CommonResultCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.service.ScripInfoService;

@Controller
@RequestMapping("/getErrorMessage")
public class ScripInfoController {
	private static Logger logger = Logger.getLogger(ScripInfoController.class);
	@Autowired
	private ScripInfoService service;
	
	/**
	* @Description: 获取未失效的错误信息 
	* @author Administrator  
	* @date 2018年5月20日  
	 */
	@RequestMapping("/getMessage")
	@ResponseBody
	public ListResponse<Map<String, Object>> getMessage(String proNo,String token){
		ListResponse<Map<String, Object>> result = new ListResponse<Map<String, Object>>();
		try {
			List<Map<String, Object>> list =  service.getMessage(proNo,token);
			Map<String, Object> mapSf = service.getCompleteCount(proNo,token);
			for (Map<String, Object> map : list) {
				//将已读取的消息日志is_delted 置为1
				service.updateErrorMessage(map.get("ID").toString()); 
			}
			result.setData(list);
			result.setMessage(mapSf.get("total").toString());
			result.setCode(mapSf.get("suc").toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return result;
	}
}
