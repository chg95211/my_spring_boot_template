package com.zsk.template.util.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

/**
 * @author yzw
 * @ClassName: EhCacheEventListenerObject
 * @Description: 一级缓存监听
 * @date 2018年7月25日 下午5:25:47
 */
@Slf4j
public class EhCacheEventListenerObject implements CacheEventListener
{
    @Override
    public void onEvent(CacheEvent cacheEvent)
    {
        System.out.println("*************************************************");
        log.info("[缓存：]Cache event {} for item with key {}. Old value = {}, New value = {}", cacheEvent.getType(), cacheEvent.getKey(), cacheEvent.getOldValue(), cacheEvent.getNewValue());
    }
}
