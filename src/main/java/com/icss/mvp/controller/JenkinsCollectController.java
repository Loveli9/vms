package com.icss.mvp.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.icss.mvp.util.StringUtilsLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.ScripInfo;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.JenkinsCollectService;
import com.icss.mvp.service.ScripInfoService;

@RequestMapping("/jenkins")
@Controller
public class JenkinsCollectController {

	@Autowired
	private JenkinsCollectService service;

	@Autowired
	private ScripInfoService InfoService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/jenkinsCollect")
	@ResponseBody
	public BaseResponse collect(HttpServletRequest request) {
		PlainResponse result = new PlainResponse();
		Map<String, Object> map = new HashMap<>();
		long startTime = System.currentTimeMillis();
		String no = request.getParameter("no");
		String token = request.getParameter("token");
		String id = request.getParameter("id");
		String url = request.getParameter("url");
		String jobName = request.getParameter("jobName");
		String otherAccount = request.getParameter("otherAccount");
		ScripInfo startInfo = new ScripInfo();
		startInfo.setMessage("开始采集Jenkins数据" + url);
		startInfo.setNo(no);
		startInfo.setResult(null);
		startInfo.setMesType("info");
		startInfo.setToken(token);
		InfoService.insertErrorMessage(startInfo);
		BaseResponse response = service.collect(no, id, token, url, jobName, otherAccount);
		String code = "fail";
		String zxResult = "更新异常";
		ScripInfo endInfo = new ScripInfo();
		endInfo.setNo(no);
		endInfo.setResult("complete");
		endInfo.setToken(token);
		if ("jenkins success".equals(response.getMessage())) {
			zxResult = "更新成功";
			code = "success";
			endInfo.setMessage("获取Jenkins数据成功:" + url);
			endInfo.setMesType("info");
		} else {
			endInfo.setMessage(response.getMessage());
			endInfo.setMesType("error");
		}
		InfoService.insertErrorMessage(endInfo);
		long endTime = System.currentTimeMillis();// 采集结束时间
		String mesType = "jenkins数据采集中......";
		result.setCode(code);
		map.put("mesType", mesType);
		map.put("zxResult", zxResult);
		map.put("workTime", StringUtilsLocal.formatTime(endTime - startTime));
		result.setData(map);
		return result;
	}

}
