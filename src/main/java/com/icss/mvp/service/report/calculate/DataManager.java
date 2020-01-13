package com.icss.mvp.service.report.calculate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private Map<String, List<Object>> datas = new HashMap<>();

    /**
     * 根据参数名称返回需要的数据
     *
     * @param key
     * @return
     */
    public List<Object> get(String key) {
        return datas.get(key);
    }

    /**
     * 将数据存放入数据提供者
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        List<Object> v = datas.get(key);
        if (v != null) {
            v.add(value);
        } else {
            v = new ArrayList<>();
            v.add(value);
            datas.put(key, v);
        }
    }

    /**
     * 将数据放入数据提供者
     *
     * @param key
     * @param values
     */
    public void put(String key, List<Object> values) {
        List<Object> v = datas.get(key);
        if (v != null) {
            v.addAll(values);
        } else {
            datas.put(key, values);
        }

    }

    /**
     * 判断是否包含某个键
     *
     * @param key
     * @return
     */
    public boolean containsKey(String key) {
        return datas.containsKey(key);
    }

    public boolean isEmpty() {
        return datas.isEmpty();
    }

    public Integer length() {
        return datas.size();
    }
}
