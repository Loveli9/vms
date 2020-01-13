package com.icss.mvp.util.cache;

/**
 * Created by Ray on 2018/11/21.
 */
public interface RedisUtils {

    /**
     * 保存
     * 
     * @param key
     * @param value
     * @return
     */
    String set(String key, String value);

    /**
     * 根据key查询
     * 
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 删除
     * 
     * @param key
     * @return
     */
    Long delete(String key);

    /**
     * 设置key生存时间
     * 
     * @param key
     * @param seconds
     * @return
     */
    Long expire(String key, Integer seconds);

    /**
     * 设置一个值并设置生存时间
     * 
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    Long set(String key, String value, Integer... seconds);

    /**
     * 设置一个值并设置生存时间
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    <T> Long set(String key, T value, Integer... seconds);

    /**
     * 是否有key
     * 
     * @param key
     * @return
     */
    Boolean exists(String key);
}
