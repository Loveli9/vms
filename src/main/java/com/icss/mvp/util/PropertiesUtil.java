package com.icss.mvp.util;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.log4j.Logger;

public class PropertiesUtil {
	private final static Logger LOG = Logger.getLogger(PropertiesUtil.class);

	private static Properties taskProperties;
	private static Properties applicationProperties;

	static {
		taskInit();
		applicationInit();
	}

	/**
	 * 初始化taskProperties变量，获取task.properties文件里的值
	 */
	private static void taskInit() {
		taskProperties = new Properties();
		try {
			String path = Thread.currentThread().getContextClassLoader().getResource("task.properties").getPath()
					.replaceAll("%20", " ");
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(path));
			taskProperties.load(inStream);
		} catch (Exception e) {
			LOG.error("get properties failed!", e);
		}
	}

	/**
	 * 初始化applicationProperties变量，获取application.properties文件里的值
	 */
	private static void applicationInit() {
		applicationProperties = new Properties();
		try {
			String path = Thread.currentThread().getContextClassLoader().getResource("application.properties").getPath()
					.replaceAll("%20", " ");
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(path));
			applicationProperties.load(inStream);
		} catch (Exception e) {
			LOG.error("get properties failed!", e);
		}
	}

	/**
	 * 根据变量名获取task.properties里的变量值
	 * 
	 * @param name
	 * @return
	 */
	public static String getTaskValue(String name) {
		if (taskProperties == null) {
			taskInit();
		}
		return taskProperties.getProperty(name);
	}

	/**
	 * 根据变量名获取application.properties里的变量值
	 * 
	 * @param name
	 * @return
	 */
	public static String getApplicationValue(String name) {
		if (applicationProperties == null) {
			applicationInit();
		}
		return applicationProperties.getProperty(name);
	}

	/**
	 * 根据变量名获取application.properties里的变量值
	 * 
	 * @param name
	 * @return
	 */
	public static String getValue(String filePath, String name) {
		Properties properties = new Properties();
		try {
			String path = Thread.currentThread().getContextClassLoader().getResource(filePath).getPath()
					.replaceAll("%20", " ");
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(path));
			properties.load(inStream);
		} catch (Exception e) {
			LOG.error("get properties failed!", e);
		}
		return properties.getProperty(name);
	}

	public static void main(String[] args) {
		System.out.println(getApplicationValue("micro_service_tmss_url"));
		System.out.println(getValue("application.properties", "micro_service_tmss_url"));
	}
}