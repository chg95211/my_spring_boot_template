package com.zsk.template.config.redis;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-11 20:46
 **/
@Configuration
@EnableConfigurationProperties(RedisSingleProperties.class)
@ConditionalOnClass(RedisSingleProperties.class)
@ConditionalOnProperty("redis.single.active")
public class RedisSingleConfig
{
    @Resource
    private RedisSingleProperties redisProperties;

    private JedisPoolConfig jedisPoolConfig()
    {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        jedisPoolConfig.setMaxTotal(redisProperties.getMaxTotal());
        jedisPoolConfig.setMaxIdle(redisProperties.getMaxIdle());
        jedisPoolConfig.setNumTestsPerEvictionRun(redisProperties.getNumTestsPerEvictionRun());
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(redisProperties.getTimeBetweenEvictionRunsMillis());
        jedisPoolConfig.setMinEvictableIdleTimeMillis(redisProperties.getMinEvictableIdleTimeMillis());
        jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(redisProperties.getSoftMinEvictableIdleTimeMillis());
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getMaxWaitMillis());
        jedisPoolConfig.setTestOnBorrow(redisProperties.getTestOnBorrow());
        jedisPoolConfig.setTestWhileIdle(redisProperties.getTestWhileIdle());
        jedisPoolConfig.setBlockWhenExhausted(redisProperties.getBlockWhenExhausted());

        return jedisPoolConfig;
    }

    @Bean
    public JedisPool jedisPool()
    {
        return new JedisPool(jedisPoolConfig(), redisProperties.getHost(), redisProperties.getPort());
    }

}
