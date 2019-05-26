package com.zsk.template.config.redis;

import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-11 22:19
 **/
@Configuration
@EnableConfigurationProperties(RedisClusterProperties.class)
@ConditionalOnClass(RedisClusterProperties.class)
@ConditionalOnProperty("redis.cluster.active")
public class RedisClusterConfig
{
    @Resource
    private RedisClusterProperties redisProperties;

    @Bean
    public JedisCluster jedisCluster()
    {
        Set<HostAndPort> jedisClusterNode = new HashSet<>();

        for (String node : redisProperties.getNodes())
        {
            String[] data = node.split(":");
            String host = data[0];
            Integer port = Integer.parseInt(data[1]);

            HostAndPort hostAndPort = new HostAndPort(host, port);
            jedisClusterNode.add(hostAndPort);
        }

        return new JedisCluster(jedisClusterNode);
    }
}
