package com.icss.mvp.controller.system;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.util.PropertiesUtil;

@Controller
@RequestMapping("/env")
@SuppressWarnings("all")
public class EnvController {
	private static Logger logger = Logger.getLogger(EnvController.class);

	@RequestMapping("/")
	@ResponseBody
	public PlainResponse getEnv() {
		PlainResponse result = new PlainResponse<>();

		// TODO: read application.properties

		Map<String, Object> map = new HashMap<>();
		String svnUrl = PropertiesUtil.getApplicationValue("micro_service_svn_url");
		String gitUrl = PropertiesUtil.getApplicationValue("micro_service_git_url");
		String tmssUrl = PropertiesUtil.getApplicationValue("micro_service_tmss_url");
		String dtsUrl = PropertiesUtil.getApplicationValue("micro_service_dts_url");
		String icpUrl = PropertiesUtil.getApplicationValue("micro_service_icp_url");
		Properties myProperty = new Properties();
		try {
			myProperty = Resources.getResourceAsProperties("jdbc.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Resources.getResourceAsProperties exception, error: "+e.getMessage());
		}

		String url = myProperty.getProperty("jdbc.url");
		String[] parts1 = url.split("[?]");
		String[] parts2 = parts1[0].split("[//]");
		String[] parts3 = parts2[2].split("[:]");
		String user = myProperty.getProperty("jdbc.username");
		String password = myProperty.getProperty("jdbc.password");

		map.put("micro_service_svn_url", svnUrl);
		map.put("micro_service_git_url", gitUrl);
		map.put("micro_service_tmss_url", tmssUrl);
		map.put("micro_service_dts_url", dtsUrl);
		map.put("micro_service_icp_url", icpUrl);
		map.put("database_table", parts2[3]);
		map.put("database_ip", parts3[0]);
		map.put("database_port", parts3[1]);
		map.put("database_user", user);
		map.put("database_password", password);
		result.setData(map);
		return result;
	}

	@RequestMapping("/version")
	@ResponseBody
	public String getVersion() {
		String version = PropertiesUtil.getApplicationValue("auto_collection_version");
		return version;
	}
}
