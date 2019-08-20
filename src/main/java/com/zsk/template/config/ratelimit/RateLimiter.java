package com.zsk.template.config.ratelimit;

import com.zsk.template.util.JsonUtil;
import com.zsk.template.util.jedis.JedisClient;
import com.zsk.template.util.lock.zookeeper.ZkLockUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.shaded.com.google.common.math.LongMath;

import java.util.concurrent.TimeUnit;

/**
 * 令牌桶限流器
 */
@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateLimiter
{

    /**
     * redis key
     */
    private String key;
    /**
     * redis分布式锁的key
     *
     * @return
     */
    private String lockKey;
    /**
     * 每秒存入的令牌数
     */
    private Double permitsPerSecond;
    /**
     * 最大存储maxBurstSeconds秒生成的令牌
     */
    private Integer maxBurstSeconds;
    /**
     * 分布式同步锁
     */
    private ZkLockUtil lockUtil;

    private JedisClient jedisClient;

//    public RateLimiter(String key, Double permitsPerSecond, Integer maxBurstSeconds, ZkLockUtil lockUtil, JedisClient jedisClient)
//    {
//        this.key = key;
//        this.lockKey = "/lock/" + key;
//        this.permitsPerSecond = permitsPerSecond;
//        this.maxBurstSeconds = maxBurstSeconds;
//        this.lockUtil = lockUtil;
//        this.jedisClient = jedisClient;
//    }

    /**
     * 生成并存储默认令牌桶
     *
     * @return
     */
    private RedisPermits putDefaultPermits()
    {
//        InterProcessMutex lock = lockUtil.lock(this.lockKey);
        try
        {
            String json = this.jedisClient.get(key);
            if (null == json)
            {
                RedisPermits permits = new RedisPermits(permitsPerSecond, maxBurstSeconds);
                this.jedisClient.set(key, JsonUtil.objectToJson(permits));
                this.jedisClient.expire(key, permits.expires());
                return permits;
            }

            RedisPermits redisPermits = JsonUtil.jsonToPojo(json, RedisPermits.class);
            return redisPermits;
        }
        finally
        {
//            lockUtil.unlock(lock, lockKey);
        }

    }

    /**
     * 获取令牌桶
     *
     * @return
     */
    public RedisPermits getPermits()
    {
        String json = this.jedisClient.get(key);
        if (null == json)
        {
            return putDefaultPermits();
        }
        return JsonUtil.jsonToPojo(json, RedisPermits.class);
    }

    /**
     * 更新令牌桶
     *
     * @param permits
     */
    public void setPermits(RedisPermits permits)
    {
        this.jedisClient.set(key, JsonUtil.objectToJson(permits));
        this.jedisClient.expire(key, permits.expires());
    }

    /**
     * 等待直到获取指定数量的令牌
     *
     * @param tokens
     * @return
     * @throws InterruptedException
     */
    public Long acquire(Long tokens) throws InterruptedException
    {
        long milliToWait = this.reserve(tokens);
//        log.info("acquire for {}ms {}", milliToWait, Thread.currentThread().getName());
        Thread.sleep(milliToWait);
        return milliToWait;
    }

    /**
     * 获取1一个令牌
     *
     * @return
     * @throws InterruptedException
     */
    public long acquire() throws InterruptedException
    {
        return acquire(1L);
    }

    /**
     * @param tokens  要获取的令牌数
     * @param timeout 获取令牌等待的时间，负数被视为0
     * @param unit
     * @return
     * @throws InterruptedException
     */
    private Boolean tryAcquire(Long tokens, Long timeout, TimeUnit unit) throws InterruptedException
    {
        long timeoutMicros = Math.max(unit.toMillis(timeout), 0);
        checkTokens(tokens);
        Long milliToWait;
        InterProcessMutex lock = this.lockUtil.lock(lockKey);
        try
        {
            if (!this.canAcquire(tokens, timeoutMicros))
            {
                return false;
            }
            else
            {
                milliToWait = this.reserveAndGetWaitLength(tokens);
            }
        }
        finally
        {
            this.lockUtil.unlock(lock, lockKey);
        }
        Thread.sleep(milliToWait);
        return true;
    }

    /**
     * 获取一个令牌
     *
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    private Boolean tryAcquire(Long timeout, TimeUnit unit) throws InterruptedException
    {
        return tryAcquire(1L, timeout, unit);
    }

    private long redisNow()
    {
        Long time = null;
        return null == time ? System.currentTimeMillis() : time;
    }

    /**
     * 获取令牌n个需要等待的时间
     *
     * @param tokens
     * @return
     */
    private long reserve(Long tokens)
    {
        this.checkTokens(tokens);
        InterProcessMutex lock = this.lockUtil.lock(lockKey);
        try
        {
            return this.reserveAndGetWaitLength(tokens);
        }
        finally
        {
            this.lockUtil.unlock(lock, lockKey);
        }
    }

    /**
     * 校验token值
     *
     * @param tokens
     */
    private void checkTokens(Long tokens)
    {
        if (tokens < 0)
        {
            throw new IllegalArgumentException("Requested tokens " + tokens + " must be positive");
        }
    }

    /**
     * 在等待的时间内是否可以获取到令牌
     *
     * @param tokens
     * @param timeoutMillis
     * @return
     */
    private Boolean canAcquire(Long tokens, Long timeoutMillis)
    {
        return queryEarliestAvailable(tokens) - timeoutMillis <= 0;
    }

    /**
     * 返回获取{tokens}个令牌最早可用的时间
     *
     * @param tokens
     * @return
     */
    private Long queryEarliestAvailable(Long tokens)
    {
        long n = redisNow();
        RedisPermits permit = this.getPermits();
        permit.reSync(n);
        // 可以消耗的令牌数
        long storedPermitsToSpend = Math.min(tokens, permit.getStoredPermits());
        // 需要等待的令牌数
        long freshPermits = tokens - storedPermitsToSpend;
        // 需要等待的时间
        long waitMillis = freshPermits * permit.getIntervalMillis();
        return LongMath.saturatedAdd(permit.getNextFreeTicketMillis() - n, waitMillis);
    }

    /**
     * 预定@{tokens}个令牌并返回所需要等待的时间
     *
     * @param tokens
     * @return
     */
    private Long reserveAndGetWaitLength(Long tokens)
    {
        long n = redisNow();
        RedisPermits permit = this.getPermits();
        permit.reSync(n);
        // 可以消耗的令牌数
        long storedPermitsToSpend = Math.min(tokens, permit.getStoredPermits());
        // 需要等待的令牌数
        long freshPermits = tokens - storedPermitsToSpend;
        // 需要等待的时间
        long waitMillis = freshPermits * permit.getIntervalMillis();
        permit.setNextFreeTicketMillis(LongMath.saturatedAdd(permit.getNextFreeTicketMillis(), waitMillis));
        permit.setStoredPermits(permit.getStoredPermits() - storedPermitsToSpend);
        this.setPermits(permit);
        return permit.getNextFreeTicketMillis() - n;
    }
}