package com.icss.mvp.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CookieUtils {
    private static Logger logger = Logger.getLogger(CookieUtils.class);

    public static final String USER_NAME           = "username";

    public final static String COOKIE_KEY_USERNAME = "username";

    public final static String COOKIE_KEY_PASSWORD = "password";

    /**
     * 根据字段查询cookie中的值
     * 
     * @param request
     * @param param
     * @return
     */
    public static String value(HttpServletRequest request, String param) {
        String value = "";
        if (null == param) return value;
        for (Cookie cookie : request.getCookies()) {
            if (param.equals(cookie.getName())) {
                value = cookie.getValue();
                break;
            }
        }
        return value;
    }

    // @Autowired
    // ISvnTaskDao taskDao;

    /**
     * 获取cookie中指定序列的键值队
     *
     * @param keywords
     * @return
     */
    public static Map<String, String> getCookies(HttpServletRequest request, Set<String> keywords) {
        if (keywords == null) {
            keywords = new HashSet<>();
        }

        Map<String, String> cookies = new HashMap<>();
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                String key = cookie.getName();
                if (keywords.isEmpty() || keywords.contains(key)) {
                    try {
                        cookies.put(key, URLDecoder.decode(cookie.getValue(), "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        logger.error("URLDecoder.decode exception, error: "+e.getMessage());
                    }

                    if (keywords.remove(key) && keywords.isEmpty()) {
                        break;
                    }
                }
            }
        }

        return cookies;
    }

    /**
     * 获取cookie中指定键值的数据
     *
     * @param key
     * @return
     */
    public static String getCookie(HttpServletRequest request, String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        return getCookies(request, new HashSet<>(Collections.singletonList(key))).get(key);

        // String result = null;
        // if (request.getCookies() != null && StringUtils.isNotBlank(key)) {
        // for (Cookie cookie : request.getCookies()) {
        // if (key.equals(cookie.getName())) {
        // try {
        // result = URLDecoder.decode(cookie.getValue(), "utf-8");
        // } catch (UnsupportedEncodingException e) {
        // result = null;
        // }
        // break;
        // }
        // }
        // }
        // return result;
    }
}
