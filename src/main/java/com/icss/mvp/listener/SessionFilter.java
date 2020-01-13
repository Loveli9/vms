package com.icss.mvp.listener;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.icss.mvp.constant.Constants;

/**
 * <pre>
 * <b>描述：session过滤器</b>
 * <b>任务编号：</b>
 * <b>作者：fpy</b> 
 * <b>创建日期： 2017年8月10日 下午2:55:48</b>
 * </pre>
 */
public class SessionFilter implements Filter {
	private static Logger logger = Logger.getLogger(SessionFilter.class);

	/**
	 * Default constructor.
	 */
	public SessionFilter() {
	}
	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		// 登陆url
		String loginUrl = httpRequest.getContextPath() + "/view/login.html";
		String url = httpRequest.getRequestURI();
		// 超时处理，ajax请求超时设置超时状态，页面请求超时则返回提示并重定向
		if (session.getAttribute(Constants.USER_KEY) == null
				&& url.indexOf("login.html") < 0) {
			// 判断是否为ajax请求
			if (httpRequest.getHeader("x-requested-with") != null
					&& "XMLHttpRequest".equalsIgnoreCase(httpRequest.getHeader("x-requested-with"))) {
				httpResponse.addHeader("sessionstatus", "timeOut");
				httpResponse.addHeader("loginPath", loginUrl);
				chain.doFilter(request, response);// 不可少，否则请求会出错
			} else {
				String str = "<script language='javascript'>alert('会话异常,请重新登录');"
						+ "window.top.location.href='"
						+ loginUrl
						+ "';</script>";
				response.setContentType("text/html;charset=UTF-8");// 解决中文乱码
				try {
					PrintWriter writer = response.getWriter();
					writer.write(str);
					writer.flush();
					writer.close();
				} catch (Exception e) {
					logger.info("##### SessionFilter writer fail #####", e);
				}
				chain.doFilter(request, response);
			}
		}else{
			chain.doFilter(request, response);
		}
		
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
