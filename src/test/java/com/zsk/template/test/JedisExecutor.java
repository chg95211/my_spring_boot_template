package com.zsk.template.test;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.List;

@FunctionalInterface
public interface JedisExecutor<T>
{
    T execute(Jedis jedis);
}

