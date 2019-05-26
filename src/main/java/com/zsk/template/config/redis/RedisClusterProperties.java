package com.zsk.template.config.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * @description:
 * @author: zsk
 * @create: 2019-05-11 22:18
 **/
@ConfigurationProperties(prefix = "redis.cluster")
public class RedisClusterProperties
{
    private List<String> nodes;
    private Boolean active;

    public List<String> getNodes()
    {
        return nodes;
    }

    public void setNodes(List<String> nodes)
    {
        this.nodes = nodes;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }
}
