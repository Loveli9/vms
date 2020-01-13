package com.icss.mvp.controller;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.icss.mvp.entity.ScripInfo;
import com.icss.mvp.service.ScripInfoService;
import com.icss.mvp.util.HttpExecuteUtils;
import com.icss.mvp.util.PropertiesUtil;

@Controller
@RequestMapping("/tmss")
public class TmssTaskController{
	private final static Logger LOG = Logger.getLogger(TmssTaskController.class);
	
//	@Value("${tmssUrl}")
//	private String tmssUrl;
	private String tmssUrl = PropertiesUtil.getApplicationValue("micro_service_tmss_url");
	@Resource
	HttpServletRequest request;
	@Resource
	HttpServletResponse response;
	
	@Autowired
    private ScripInfoService InfoService;
	
	@RequestMapping("/savetmssinfo")
	@ResponseBody
	public void saveSvninfo(String tmssuser, String tmsspass) {
		try {
			addNameCookie(tmssuser, response, request);
			addPassCookie(tmsspass, response, request);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			LOG.error("addNameCookie & addPassCookie exception, error: "+e.getMessage());
		}
		
	}
	
	// 将用户名保存到本地cookie中
	private static void addNameCookie(String username, HttpServletResponse response, HttpServletRequest request)
			throws UnsupportedEncodingException {
		if (StringUtils.isNotBlank(username)) {

			Cookie tmssuserCookie = new Cookie("tmssuser", URLEncoder.encode(username, "utf-8"));
			tmssuserCookie.setPath("/");
			tmssuserCookie.setMaxAge(7 * 24 * 60 * 60);
			response.addCookie(tmssuserCookie);
		}
	}

	// 将密码保存到本地cookie中
	private static void addPassCookie(String password, HttpServletResponse response, HttpServletRequest request)
			throws UnsupportedEncodingException {
		if (StringUtils.isNotBlank(password)) {

			Cookie tmsspassCookie = new Cookie("tmsspass", URLEncoder.encode(password, "utf-8"));
			tmsspassCookie.setPath("/");
			tmsspassCookie.setMaxAge(7 * 24 * 60 * 60);
			response.addCookie(tmsspassCookie);
		}
	}
	
	/**
	 * 采集tmss数据并入库
	 * @param no
	 */
	@RequestMapping("/tmssTask")
	@ResponseBody
	public Map<String, Object> tmssTask(String no,String token,String tmsUrl,String id){
		long startTime = System.currentTimeMillis();
		int flag = 0;
		Map<String, Object> req = new HashMap<>();
        Map<String, String> tmssName = new HashMap<>();
        Map<String, String> tmssPass = new HashMap<>();
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                String key = cookie.getName();
                if (key.startsWith("tmssuser")||"username".equals(key)) {
                    try {
                    	tmssName.put(key, URLDecoder.decode(cookie.getValue(), "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                    	LOG.error("转换失败"+e.getMessage());
                    }
                }
                if (key.startsWith("tmsspass")||"password".equals(key)) {
                	try {
                		tmssPass.put(key, URLDecoder.decode(cookie.getValue(), "utf-8"));
                	} catch (UnsupportedEncodingException e) {
                		LOG.error("转换失败"+e.getMessage());
                	}
                }
            }
        }

        String tmssNameStr = JSON.toJSONString(tmssName);
        String tmssPassStr = JSON.toJSONString(tmssPass);
        req.put("tmssName", tmssNameStr);
        req.put("tmssPass", tmssPassStr);
		req.put("projectId", no);
		req.put("id", id);
		try {
			String res = HttpExecuteUtils.httpGet(tmssUrl + "tmss/getInfos", req);
			long endTime = System.currentTimeMillis();//采集结束时间
			req.clear();
			req = HttpExecuteUtils.parseResponse(res, Map.class, new HashMap<>());
			String code = StringUtilsLocal.valueOf(req.get("code"));
			if(!"200".equals(code)) {
				flag = 1;
				String mess = StringUtilsLocal.valueOf(req.get("message"));
				LOG.error("tmss抓取异常："+mess);
				ScripInfo info = new ScripInfo();
				info.setMessage("更新TMS数据失败：tmss抓取异常，采集路径："+tmsUrl);
				info.setNo(no);
				info.setToken(token);
				info.setMesType("error");
				InfoService.insertErrorMessage(info);
			}
			req = HttpExecuteUtils.parseResponse(JSON.toJSONString(req.get("data")), Map.class, req);
			
			String mesType = "更新TMS数据......";
			String zxResult = "更新成功"; 
			req.put("code", "success");
			if(flag == 1) {
				zxResult = "更新异常";
				req.put("code", "fail");
			}else {
				req.put("code", "success");
			}
			req.put("mesType", mesType);
			req.put("zxResult", zxResult);
			req.put("workTime", StringUtilsLocal.formatTime(endTime - startTime));
			
			
			return req;
		} catch (Exception e) {
			req.clear();
			req.put("err", "通信异常，请检查服务端是否联通");
			LOG.error("通信异常，请检查服务端是否联通"+e.getMessage());
			
			flag = 1;
			ScripInfo info = new ScripInfo();
			info.setMessage("更新TMS数据失败：通信异常，请检查服务端是否联通，采集路径:"+tmsUrl);
			info.setNo(no);
			info.setToken(token);
			info.setMesType("error");
			InfoService.insertErrorMessage(info);
			
			return req;
		}
		
	}
}
