package com.zsk.template.test;

import redis.clients.jedis.Pipeline;

@FunctionalInterface
public interface PipelineExecutor
{
    void load(Pipeline pipeline);
}
