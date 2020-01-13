package com.icss.mvp.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.icss.mvp.controller.ProjectLableController;

/**
 * @ClassName: JSONUtils
 * @Description:json与对象之间转换工具
 * @author chengchenhui
 * @date 2018年8月6日
 */
public class JSONUtils {
    private static Logger logger = Logger.getLogger(JSONUtils.class);

    /**
     * Bean对象转JSON
     * 
     * @param object
     * @param dataFormatString
     * @return
     */
    public static String beanToJson(Object object, String dataFormatString) {
        if (object != null) {
            if (StringUtils.isEmpty(dataFormatString)) {
                return JSONObject.toJSONString(object);
            }
            return JSON.toJSONStringWithDateFormat(object, dataFormatString);
        } else {
            return null;
        }
    }

    public static String parseString(Object entity) {
        return entity instanceof String ? (String) entity : JSON.toJSONString(entity);
    }

    public static JSONObject parseInstance(Object entity) {
        return JSON.parseObject(parseString(entity));
    }

    public static <T> T parseInstance(Object entity, Class<T> clazz) {
        return JSON.parseObject(parseString(entity), clazz);
    }

    public static <T> T parseInstance(Object entity, Class<T> clazz, T defaultValue) {
        T result = parseInstance(entity, clazz);
        return result == null ? defaultValue : result;
    }

    /**
     * parse to collection
     *
     * @param entity Java Object
     * @param clazz Class of Java Object
     * @param <T> the type of elements in collection
     * @return
     */
    public static <T> List<T> parseCollection(Object entity, Class<T> clazz) {
        List<T> result = JSON.parseArray(parseString(entity), clazz);
        return (result == null || result.isEmpty()) ? new ArrayList<>() : result;
    }

    /**
     * parse by key
     *
     * @param entity
     * @param key
     * @return
     */
    public static JSONObject parseSelectedInstance(Object entity, String key) {
        JSONObject result = null;

        try {
            Object parseResult = JSON.toJSON(entity);
            if (parseResult instanceof JSONObject) {
                result = parseInstance(((JSONObject) parseResult).getString(key));
            }
        } catch (Exception e) {
            logger.error("parseInstance exception, error: "+e.getMessage());
        }

        return result;
    }

    /**
     * parse by key
     *
     * @param entity
     * @param key
     * @return
     */
    public static <T> T parseSelectedInstance(Object entity, String key, Class<T> clazz) {
        if (key.contains("/")) {
            return parseSelectedInstance(entity, clazz, key.split("/"));
        }

        T result = null;

        try {
            JSONObject parseResult = parseInstance(entity);
            result = parseInstance(parseResult.getString(key), clazz);
//            Object parseResult = JSON.toJSON(entity);
//            if (parseResult instanceof JSONObject) {
//                result = parseInstance(((JSONObject) parseResult).getString(key), clazz);
//            }
        } catch (Exception e) {
            logger.error("parseInstance exception, error: "+e.getMessage());
        }

        return result;
    }

    public static <T> T parseSelectedInstance(Object entity, String key, Class<T> clazz, T defaultValue) {
        T result = parseSelectedInstance(entity, key, clazz);
        return result == null ? defaultValue : result;
    }

    public static <T> T parseSelectedInstance(Object entity, Class<T> clazz, String... paths) {
        if (paths.length == 0) {
            return parseInstance(entity, clazz);
        }

        if (paths.length == 1) {
            return parseSelectedInstance(entity, paths[0], clazz);
        }

        JSONObject step = parseSelectedInstance(entity, paths[0]);
        for (int i = 1; i < paths.length - 1; i++) {
            step = parseSelectedInstance(step, paths[i]);
        }
        return parseSelectedInstance(step, paths[paths.length - 1], clazz);
    }

    public static <T> T parseSelectedInstance(Object entity, Class<T> clazz, T defaultValue, String... paths) {
        T result = parseSelectedInstance(entity, clazz, paths);
        return result == null ? defaultValue : result;
    }

    public static <T> List<T> parseSelectedCollection(Object entity, String key, Class<T> clazz) {
        List<T> result = null;

        try {
            Object parseResult = JSON.toJSON(entity);
            if (parseResult instanceof JSONObject) {
                result = parseCollection(((JSONObject) parseResult).getString(key), clazz);
            }
        } catch (Exception e) {
            logger.error("parseCollection excecption, error: "+e.getMessage());
        }

        return result;
    }

    public static <T> List<T> parseSelectedCollection(Object entity, Class<T> clazz, String... paths) {
        if (paths.length == 0) {
            return parseCollection(entity, clazz);
        }

        if (paths.length == 1) {
            return parseSelectedCollection(entity, paths[0], clazz);
        }

        JSONObject step = parseSelectedInstance(entity, paths[0]);
        for (int i = 1; i < paths.length - 1; i++) {
            step = parseSelectedInstance(entity, paths[i]);
        }
        return parseSelectedCollection(step, paths[paths.length - 1], clazz);
    }

}
