package com.zsk.template.util.jedis.impl;

import com.zsk.template.util.jedis.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@ConditionalOnProperty("redis.single.active")
@Service
public class JedisSingleClient implements JedisClient
{
    @Autowired
    private JedisPool jedisPool;

    @Override
    public String get(String key)
    {
        Jedis jedis = this.jedisPool.getResource();
        String val = jedis.get(key);
        jedis.close();
        return val;
    }

    @Override
    public String set(String key, String value)
    {
        Jedis jedis = this.jedisPool.getResource();
        String val = jedis.set(key, value);
        jedis.close();
        return val;
    }

    @Override
    public long hset(String hkey, String key, String value)
    {
        Jedis jedis = this.jedisPool.getResource();
        long sec = jedis.hset(hkey, key, value);
        jedis.close();
        return sec;
    }

    @Override
    public String hget(String hkey, String key)
    {
        Jedis jedis = this.jedisPool.getResource();
        String val = jedis.hget(hkey, key);
        jedis.close();
        return val;
    }

    @Override
    public long incr(String key)
    {
        Jedis jedis = this.jedisPool.getResource();
        long sec = jedis.incr(key);
        jedis.close();
        return sec;
    }

    @Override
    public long decr(String key)
    {
        Jedis jedis = this.jedisPool.getResource();
        long sec = jedis.decr(key);
        jedis.close();
        return sec;
    }

    @Override
    public long expire(String key, int second)
    {
        Jedis jedis = this.jedisPool.getResource();
        long sec = jedis.expire(key, second);
        jedis.close();
        return sec;
    }

    @Override
    public long ttl(String key)
    {
        Jedis jedis = this.jedisPool.getResource();
        long sec = jedis.ttl(key);
        jedis.close();
        return sec;
    }

    @Override
    public long del(String key)
    {
        Jedis jedis = this.jedisPool.getResource();
        long sec = jedis.del(key);
        jedis.close();
        return sec;
    }

    @Override
    public long delHkey(String hkey, String key)
    {
        Jedis jedis = this.jedisPool.getResource();
        long sec = jedis.hdel(hkey, key);
        jedis.close();
        return sec;
    }

}
