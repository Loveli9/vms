package com.icss.mvp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.icss.mvp.constant.Constants;

public class LoginInterceptor implements HandlerInterceptor {
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// 获取请求的URL
		String url = request.getRequestURI();
		String url1 = request.getHeader("Referer");
		// URL:login.jsp是公开的;这个demo是除了login.jsp是可以公开访问的，其它的URL都进行拦截控制
		if(url.indexOf("/env/") >= 0) {
			return true;
		}	
		if (url1!=null&&url1.indexOf("demo.html") >= 0) {
			return true;
		}
		if (url1!=null&&url1.indexOf("developIndexYH.html") >= 0) {
			return true;
		}
		if (url.indexOf("login.html") >= 0) {
			return true;
		}
		Object userInfo = request.getSession().getAttribute(Constants.USER_KEY);

		if (null == userInfo) {
			// 判断是否是ajax请求
			if (request.getHeader("x-requested-with") != null
					&& "XMLHttpRequest".equalsIgnoreCase(request.getHeader("x-requested-with")))// 如果是ajax请求响应头会有，x-requested-with；
			{
				response.setHeader("sessionstatus", "TIMEOUT");// 在响应头设置session状态
                response.setHeader("CONTEXTPATH", request.getContextPath() + "/view/login.html");
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			} else {
				request.getRequestDispatcher("/view/login.html").forward(
						request, response);

			}
			return false;
		} else {
			return true;
		}
	}

}
