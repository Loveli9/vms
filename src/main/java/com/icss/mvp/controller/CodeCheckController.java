package com.icss.mvp.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.CodeCheckService;
import com.icss.mvp.util.StringUtilsLocal;

@Controller
@RequestMapping("/codeCheck")
public class CodeCheckController {
	// private final static Logger LOG =
	// Logger.getLogger(CodeCheckController.class);

	@Autowired
	private CodeCheckService codeCheckService;

	/**
	 * 采集代码检视意见
	 * 
	 * @param
	 */
	@SuppressWarnings("all")
	@RequestMapping("/insertCodeCheck")
	@ResponseBody
	public PlainResponse insertCodeCheck(String no,String token,String id) {
		PlainResponse result = new PlainResponse();
		Map<String, Object> map = new HashMap<String, Object>();
		long startTime = System.currentTimeMillis();// 采集开始时间
		int a = codeCheckService.insertCodeCheck(no,token,id);
		long endTime = System.currentTimeMillis();// 采集结束时间
		String mesType = "采集代码检视意见数据......";
		String zxResult = "采集成功";
		result.setCode("success");
		if (a == 0) {
			zxResult = "采集异常";
			result.setCode("fail");
		}else if (a == 2) {
			result.setCode("no");
		}
		map.put("mesType", mesType);
		map.put("zxResult", zxResult);
		map.put("workTime", StringUtilsLocal.formatTime(endTime - startTime));
		result.setData(map);
		return result;
	}

}
