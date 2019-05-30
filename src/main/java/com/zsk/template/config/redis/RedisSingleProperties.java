package com.zsk.template.config.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-11 20:51
 **/
@ConfigurationProperties(prefix = "redis.single")
public class RedisSingleProperties
{
    private Integer maxTotal;
    private Integer maxIdle;
    private Integer numTestsPerEvictionRun;
    private Integer timeBetweenEvictionRunsMillis;
    private Integer minEvictableIdleTimeMillis;
    private Integer softMinEvictableIdleTimeMillis;
    private Integer maxWaitMillis;
    private Boolean testOnBorrow;
    private Boolean testWhileIdle;
    private Boolean blockWhenExhausted;
    private String host;
    private Integer port;
    private Boolean active;
    private String password;
    private Integer timeout;

    public Integer getTimeout()
    {
        return timeout;
    }

    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
    }

    public Integer getMaxTotal()
    {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal)
    {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxIdle()
    {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle)
    {
        this.maxIdle = maxIdle;
    }

    public Integer getNumTestsPerEvictionRun()
    {
        return numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(Integer numTestsPerEvictionRun)
    {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public Integer getTimeBetweenEvictionRunsMillis()
    {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(Integer timeBetweenEvictionRunsMillis)
    {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public Integer getMinEvictableIdleTimeMillis()
    {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(Integer minEvictableIdleTimeMillis)
    {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public Integer getSoftMinEvictableIdleTimeMillis()
    {
        return softMinEvictableIdleTimeMillis;
    }

    public void setSoftMinEvictableIdleTimeMillis(Integer softMinEvictableIdleTimeMillis)
    {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }

    public Integer getMaxWaitMillis()
    {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(Integer maxWaitMillis)
    {
        this.maxWaitMillis = maxWaitMillis;
    }

    public Boolean getTestOnBorrow()
    {
        return testOnBorrow;
    }

    public void setTestOnBorrow(Boolean testOnBorrow)
    {
        this.testOnBorrow = testOnBorrow;
    }

    public Boolean getTestWhileIdle()
    {
        return testWhileIdle;
    }

    public void setTestWhileIdle(Boolean testWhileIdle)
    {
        this.testWhileIdle = testWhileIdle;
    }

    public Boolean getBlockWhenExhausted()
    {
        return blockWhenExhausted;
    }

    public void setBlockWhenExhausted(Boolean blockWhenExhausted)
    {
        this.blockWhenExhausted = blockWhenExhausted;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public Integer getPort()
    {
        return port;
    }

    public void setPort(Integer port)
    {
        this.port = port;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
