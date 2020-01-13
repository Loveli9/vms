package com.icss.mvp.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.icss.mvp.util.CookieUtils;

/**
 * Created by Ray on 2018/6/8.
 */
public class CommonService {

    private static Logger      logger              = Logger.getLogger(CommonService.class);

    public final static String COOKIE_KEY_USERNAME = "username";

    public final static String COOKIE_KEY_PASSWORD = "password";

    @Resource
    HttpServletRequest         request;

    /**
     * 获取cookie中指定键值的数据
     * 
     * @param key
     * @return
     */
    protected String getCookie(String key) {
        return CookieUtils.getCookie(request, key);
    }
}
