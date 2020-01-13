package com.icss.mvp.util.cache;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.alibaba.fastjson.JSON;

/**
 * Created by Ray on 2018/11/21.
 */
@Component
public class RedisUtilsImp implements RedisUtils {

    private static Logger logger = Logger.getLogger(RedisUtilsImp.class);

    public final static Integer EXPIRED_SECONDS_DEFAULT = 60;

    @Autowired(required = false) private ShardedJedisPool shardedJedisPool;

    public <T> T execute(Function<ShardedJedis, T> fun) {
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();

            return fun.callback(shardedJedis);
        } catch (Exception e) {
            logger.error("redis exception, error: " + e.getMessage());
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
        return null;
    }

    /**
     * 保存
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public String set(String key, String value) {
        return execute(e -> e.set(key, value));
    }

    // /**
    // * 根据key查询
    // *
    // * @param key
    // * @return
    // */
    // @Override
    // public String get(String key) {
    // return execute(e -> e.get(key));
    // }

    /**
     * 删除
     *
     * @param key
     * @return
     */
    @Override
    public Long delete(String key) {
        return execute(e -> e.del(key));
    }

    /**
     * 设置key生存时间，单位秒
     *
     * @param key
     * @param seconds
     * @return
     */
    @Override
    public Long expire(String key, Integer seconds) {
        return execute(e -> e.expire(key, seconds));
    }

    // /**
    // * 设置一个值并设置生存时间
    // *
    // * @param key
    // * @param value
    // * @param seconds
    // * @return
    // */
    // @Override
    // public Long set(String key, String value, Integer seconds) {
    // return execute(e -> {
    // e.set(key, value);
    // return e.expire(key, seconds);
    // });
    // }

    /**
     * 是否有key
     *
     * @param key
     * @return
     */
    @Override
    public Boolean exists(String key) {
        return execute(e -> e.exists(key));
    }

    /**
     * 读取list最末元素即为最新的值
     * 
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        return execute(e -> {
            // Set<String> stored = e.zrevrange(key, 0, -1);
            // return String.valueOf(stored.stream().findFirst());

            return e.exists(key) ? e.lrange(key, -1, -1).get(0) : null;
        });
    }

    /**
     * 以list存储对象值，向list尾部追加同主键对象值
     * 
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    @Override
    public Long set(String key, String value, Integer... seconds) {
        return execute(e -> {
            e.rpush(key, value);
            int duration = (seconds.length > 0 && seconds[0] > 0) ? seconds[0] : EXPIRED_SECONDS_DEFAULT;
            return e.expire(key, duration);
        });
    }

    /**
     * 设置一个值并设置生存时间
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    @Override
    public <T> Long set(String key, T value, Integer... seconds) {
        String stringValue;
        if (value instanceof String) {
            stringValue = String.valueOf(value);
        } else {
            stringValue = JSON.toJSONString(value);
        }

        return set(key, stringValue, seconds);
    }

}
