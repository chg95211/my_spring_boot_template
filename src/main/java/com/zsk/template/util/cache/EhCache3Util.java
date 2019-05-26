package com.zsk.template.util.cache;

import java.io.File;
import java.time.Duration;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.event.EventType;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;

public class EhCache3Util
{
    private EhCache3Util()
    {

    }

    //监听器配置
    private static CacheEventListenerConfigurationBuilder cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
            .newEventListenerConfiguration(new EhCacheEventListenerObject(), EventType.EXPIRED)
            .unordered()
            .asynchronous();

    //缓存配置
    private static CacheConfiguration<String, String> cacheConfig = CacheConfigurationBuilder
            .newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.newResourcePoolsBuilder()
                    .heap(DataConstants.HEAP_CACHE_SIZE, MemoryUnit.KB)    //堆内缓存大小

                    .offheap(DataConstants.OFF_HEAP_CACHE_SIZE, MemoryUnit.MB)  //堆外缓存大小

                    .disk(DataConstants.DISK_CACHE_SIZE, MemoryUnit.MB)                     //文件缓存大小
    )
            .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(DataConstants.EHCACHE_TTL))) //缓存超时时间
            .withSizeOfMaxObjectGraph(DataConstants.HEAP_MAX_OBJECT_GRAPH)  //统计对象大小时对象图遍历深度
            .withSizeOfMaxObjectSize(DataConstants.HEAP_MAX_OBJECT_SIZE, MemoryUnit.KB) //可缓存的最大对象大小
            .add(cacheEventListenerConfiguration).build();


    //单例配置
    private static class EhCacheHolder
    {
        private final static CacheManager cacheManager =
                CacheManagerBuilder
                        .newCacheManagerBuilder()
                        .with(CacheManagerBuilder.persistence(new File(DataConstants.DISK_CACHE_DIR, DataConstants.EHCACHE_CACHE_NAME))).withCache(DataConstants.EHCACHE_CACHE_NAME, cacheConfig).build(true);
        private final static Cache<String, String> defaultCache = cacheManager.getCache(DataConstants.EHCACHE_CACHE_NAME, String.class, String.class);

    }

    //获取缓存
    private static Cache<String, String> getEhCache()
    {
        return EhCacheHolder.defaultCache;
    }



    //缓存存值
    public static void put(String key, Object value)
    {
        String json = JSON.toJSONString(value);
        getEhCache().put(key, json);
    }

    //获取缓存值
    public static <T> T get(String key, Class<T> clazz)
    {
        String json = getEhCache().get(key);
        if (!ObjectUtils.isEmpty(json))
        {
            return JSON.parseObject(json, clazz);
        }
        return null;

    }

    //清除缓存值
    public static void remove(String key)
    {
        getEhCache().remove(key);
    }


    //清除缓存值
    public static void removeCache()
    {
        EhCacheHolder.cacheManager.removeCache(DataConstants.EHCACHE_CACHE_NAME);
    }


}
