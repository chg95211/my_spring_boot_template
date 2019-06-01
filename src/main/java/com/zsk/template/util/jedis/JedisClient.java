package com.zsk.template.util.jedis;

public interface JedisClient
{
    String get(String key);

    String set(String key, String value);

    long hset(String hkey, String key, String value);

    String hget(String hkey, String key);

    long incr(String key);

    long decr(String key);

    long expire(String key, int second);

    long ttl(String key);

    long del(String key);

    long delHkey(String hkey, String key);
}
