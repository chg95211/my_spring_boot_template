package com.zsk.template.util.jedis.impl;

import com.zsk.template.util.jedis.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

@ConditionalOnProperty("redis.cluster.active")
@Service
public class JedisClusterClient implements JedisClient
{
    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public String get(String key)
    {
        return this.jedisCluster.get(key);
    }

    @Override
    public String set(String key, String value)
    {
        return this.jedisCluster.set(key, value);
    }

    @Override
    public long hset(String hkey, String key, String value)
    {
        return this.jedisCluster.hset(hkey, key, value);
    }

    @Override
    public String hget(String hkey, String key)
    {
        return this.jedisCluster.hget(hkey, key);
    }

    @Override
    public long incr(String key)
    {
        return this.jedisCluster.incr(key);
    }

    @Override
    public long expire(String key, int second)
    {
        return this.jedisCluster.expire(key, second);
    }

    @Override
    public long ttl(String key)
    {
        return this.jedisCluster.ttl(key);
    }

    @Override
    public long del(String key)
    {
        return this.jedisCluster.del(key);
    }

    @Override
    public long delHkey(String hkey, String key)
    {
        return this.jedisCluster.hdel(hkey, key);
    }
}
