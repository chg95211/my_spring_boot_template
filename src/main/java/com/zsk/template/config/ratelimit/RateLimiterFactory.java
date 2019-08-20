package com.zsk.template.config.ratelimit;

import com.zsk.template.util.jedis.JedisClient;
import com.zsk.template.util.lock.zookeeper.ZkLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 令牌桶限流器工厂
 */
@Component
@Slf4j
@ConditionalOnBean({ZkLockUtil.class, JedisClient.class})
public class RateLimiterFactory
{

    @Autowired
    private ZkLockUtil lockUtil;

    @Autowired
    private JedisClient jedisClient;

    /**
     * 本地持有对象
     */
    private volatile Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    /**
     * @param key              redis key
     * @param permitsPerSecond 每秒产生的令牌数
     * @param maxBurstSeconds  最大存储多少秒的令牌
     * @return
     */
    public RateLimiter build(String key, Double permitsPerSecond, Integer maxBurstSeconds)
    {
        //双重检测锁
        if (!rateLimiterMap.containsKey(key))
        {
            synchronized (this)
            {
                if (!rateLimiterMap.containsKey(key))
                {

                    rateLimiterMap.put(key, RateLimiter.builder()
                                                        .key(key)
                                                        .lockKey("/lock/" + key)
                                                        .permitsPerSecond(permitsPerSecond)
                                                        .maxBurstSeconds(maxBurstSeconds)
                                                        .lockUtil(lockUtil)
                                                        .jedisClient(jedisClient)
                                                        .build());
                }
            }
        }
        return rateLimiterMap.get(key);
    }
}