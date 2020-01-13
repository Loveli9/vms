package com.icss.mvp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LogInterceptor implements HandlerInterceptor {
	private final static Logger LOG = Logger.getLogger(LogInterceptor.class);
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		MDC.remove("sessionId");
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		MDC.put("sessionId", getCard());
		LOG.debug("######### Controller："+handler.getClass().getName());
		LOG.debug("######### 方法名："+request.getServletPath());
		return true;
	}


	public static String getCard(){
		java.util.Random rand=new java.util.Random(); 
		StringBuilder cardNnumer = new StringBuilder();
		for(int a=0;a<10;a++){
			cardNnumer.append(rand.nextInt(10));
		}
		return cardNnumer.toString();
	}
}
