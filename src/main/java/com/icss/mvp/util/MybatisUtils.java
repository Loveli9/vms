package com.icss.mvp.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Ray on 2019/2/26.
 *
 * @author :Ray
 * @date :2019/2/26 11:40
 */
public class MybatisUtils {

    /**
     * 以逗号连接的多条件查询，在parameter中以 column+s 为key，mapper中拼装为 column in (v1, v2, ..., vn)
     */
    public static final String SCOPE_SUFFIX_FORMAT = "%ss";

    /**
     * 按逗号分割，去重，并按最终集合长度设定精确查询还是集和查询，column = 还是 columns in ()
     * 
     * @param parameter
     * @param value
     * @param key
     */
    public static void buildScopeParam(Map<String, Object> parameter, String value, String key) {
        List<String> ids = CollectionUtilsLocal.splitToList(value);

        // 使用where column in (collection)时，key为 'columns'
        // 集合仅有一个元素时，key为 'column'
        int size = ids.size();
        if (size == 1) {
            parameter.put(key, ids.get(0));
        } else if (size > 1) {
            parameter.put(String.format(SCOPE_SUFFIX_FORMAT, key), ids);
        }
    }

    public static <T> void buildParam(Map<String, Object> parameter, T value, String key) {
        if (value instanceof String) {
            if (StringUtils.isNotEmpty((String) value)) {
                parameter.put(key, value);
            }
        } else if (value != null) {
            parameter.put(key, value);
        }
    }
}
