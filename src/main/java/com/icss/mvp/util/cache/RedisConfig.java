package com.icss.mvp.util.cache;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by Ray on 2018/11/21.
 */
@Configuration
public class RedisConfig {

    private static Logger logger = Logger.getLogger(RedisConfig.class);

    @Value("${redis.maxTotal}")
    private Integer       maxTotal;
    @Value("${redis.host}")
    private String        host;
    @Value("${redis.port}")
    private Integer       port;
    @Value("${redis.secret}")
    private String        secret;

    @Bean
    public ShardedJedisPool shardedJedisPool() {
        // 配置连接池
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);

        // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        // //TODO:考虑开销优化
        // jedisPoolConfig.setTestOnBorrow(true);

        ArrayList<JedisShardInfo> arrayList = new ArrayList<>();
        JedisShardInfo shared = new JedisShardInfo(host, port);
        shared.setPassword(secret);
        arrayList.add(shared);
        return new ShardedJedisPool(jedisPoolConfig, arrayList);
    }
}
