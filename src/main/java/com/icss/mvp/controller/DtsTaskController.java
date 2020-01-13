package com.icss.mvp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.icss.mvp.entity.DtsInfo;
import com.icss.mvp.service.DtsTaskService;
import com.icss.mvp.service.ScripInfoService;
import com.icss.mvp.util.PropertiesUtil;

@RestController
@RequestMapping("/dtsTaskController")
@SuppressWarnings("all")
public class DtsTaskController {
	private static Logger logger = Logger.getLogger(DtsTaskController.class);

	@Resource
	private DtsTaskService dtsTaskService;
//	@Resource
//	private DtsTakService dtsTakService;
//	@Resource
//	private DtsLeaveDINumCollector dtsLeaveDINumCollector;
//	@Value("${dtsUrl}")
//	private String dtsUrl;
	private String dtsUrl = PropertiesUtil.getApplicationValue("micro_service_dts_url");
	@Resource
	HttpServletRequest request;
	@Resource
	HttpServletResponse response;
	
	 @Autowired
	 private ScripInfoService InfoService;
	
	@RequestMapping("/DtsSeverity")
	@ResponseBody
	public Map<String, Object> getDtsSeverity(String projNo,String isqueryNow){
//		Map<String, Object> req = new HashMap<>();
//		req.put("projNo", projNo);
//		String res = HttpExecuteUtils.httpGet(dtsUrl+"dtsTaskController/DtsSeverity", req);
//		return HttpExecuteUtils.parseResponseData(res, Map.class);
		Map<String, Object> req = new HashMap<String, Object>();
		if("1".equals(isqueryNow)) {//测试获取当前新增问题数据
			 req = dtsTaskService.getDtsSeverityToday(projNo);
		}else {
			 req = dtsTaskService.getDtsSeverity(projNo);
		}
		return req;
	}
	@RequestMapping("/DtsSeverityByVersion")
	@ResponseBody
	public List<HashMap<String, Object>> getDtsSeverityByVersion(String projNo){
//		Map<String, Object> req = new HashMap<>();
//		req.put("projNo", projNo);
//		String res = HttpExecuteUtils.httpGet(dtsUrl+"dtsTaskController/DtsSeverityByVersion", req);
//		return HttpExecuteUtils.parseResponseData(res, List.class);
		return dtsTaskService.getDtsSeverityByVersion(projNo);
	}
	
	@RequestMapping("/DtsSeverityByEven")
	@ResponseBody
	public List<HashMap<String, Object>> getDtsSeverityByEven(DtsInfo dtsInfo){
//		String res = HttpExecuteUtils.httpPost(dtsUrl+"dtsTaskController/DtsSeverityByEven", dtsInfo);
//		return HttpExecuteUtils.parseResponseData(res, List.class);
		return dtsTaskService.getDtsSeverityByEven(dtsInfo);
	}
	
	@RequestMapping("/DtsDiList")
	@ResponseBody
	public List<Map<String, Object>> getDtsDiList(String projNo){
//		Map<String, Object> req = new HashMap<>();
//		req.put("projNo", projNo);
//		String res = HttpExecuteUtils.httpGet(dtsUrl+"dtsTaskController/DtsDiList", req);
//		return HttpExecuteUtils.parseResponseData(res, List.class);
		List<Map<String, Object>> map = dtsTaskService.getDtsList(projNo);
		return dtsTaskService.queryDINums(map,projNo);
	}
	
//	@RequestMapping("/dtsTask")
//	@ResponseBody
//	public PlainResponse saveDtsLogs(String no,String token,String id){
//		Map<String , Object> map = new HashMap<String,Object>();
//		//开始采集时间
//		long startTime = System.currentTimeMillis();
//		PlainResponse result = new PlainResponse();
//		ScripInfo info = new ScripInfo();
//		int a =dtsTakService.getDTSDatas0(no,token,id);
//		long endTime = System.currentTimeMillis();//采集结束时间
//		String mesType = "更新DTS数据......";
//		String zxResult = "更新成功";
//		result.setCode("success");
//		if(a == 1) {
//			zxResult = "更新异常";
//			result.setCode("fail");
//		}
//		map.put("mesType", mesType);
//		map.put("zxResult", zxResult);
//		map.put("workTime", StringUtilsLocal.formatTime(endTime - startTime));
//		result.setData(map);
//		return result;
//	}
	
	@RequestMapping("/getDtsVersion")
	@ResponseBody
	public List<Map<String, Object>> getDtsVersion(String projNo){
//		Map<String, Object> req = new HashMap<>();
//		req.put("projNo", projNo);
//		HttpExecuteUtils.httpGet(dtsUrl+"dtsTaskController/getDtsVersion", req);
		List<Map<String, Object>> ret = dtsTaskService.getDtsVersion(projNo);
		return ret;
	}
	
	@RequestMapping("/dtsDownload")
	@ResponseBody
	public void dtsDownload(String no, String severity, String selected, String selectedVal, String bVersion, String curentStatus, HttpServletResponse response) throws Exception
	{
		byte[] fileContents = dtsTaskService.dtsDownload(no, severity, selected, selectedVal, bVersion, curentStatus);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = "DTS问题单" + sf.format(new Date()).toString() + ".xls";
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(fileName.getBytes(), "iso-8859-1"));
		response.getOutputStream().write(fileContents);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	@RequestMapping("/savedtsinfo")
	@ResponseBody
	public void saveSvninfo(String dtsuser, String dtspass) {
		try {
			addNameCookie(dtsuser, response, request);
			addPassCookie(dtspass, response, request);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error("addNameCookie & addPassCookie exception, error: "+e.getMessage());
		}
		
	}
	
	// 将用户名保存到本地cookie中
	private static void addNameCookie(String username, HttpServletResponse response, HttpServletRequest request)
			throws UnsupportedEncodingException {
		if (StringUtils.isNotBlank(username)) {

			Cookie dtsuserCookie = new Cookie("dtsuser", URLEncoder.encode(username, "utf-8"));
			dtsuserCookie.setPath("/");
			dtsuserCookie.setMaxAge(7 * 24 * 60 * 60);
			response.addCookie(dtsuserCookie);
		}
	}

	// 将密码保存到本地cookie中
	private static void addPassCookie(String password, HttpServletResponse response, HttpServletRequest request)
			throws UnsupportedEncodingException {
		if (StringUtils.isNotBlank(password)) {

			Cookie dtspassCookie = new Cookie("dtspass", URLEncoder.encode(password, "utf-8"));
			dtspassCookie.setPath("/");
			dtspassCookie.setMaxAge(7 * 24 * 60 * 60);
			response.addCookie(dtspassCookie);
		}
	}
}
