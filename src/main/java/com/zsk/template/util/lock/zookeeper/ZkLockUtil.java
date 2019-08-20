package com.zsk.template.util.lock.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: zsk
 * @create: 2019-08-04 22:47
 **/
@Slf4j
@Component("zkLockUtil")
public class ZkLockUtil
{
    private static CuratorFramework client = null;

    static
    {
        //创建zookeeper的客户端
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        client.start();
    }

    public InterProcessMutex lock(String key)
    {
        //创建分布式锁, 锁空间的根节点路径为key
        InterProcessMutex mutex = new InterProcessMutex(client, key);
        try
        {
            mutex.acquire();
            log.info(Thread.currentThread().getName() + ":获取了锁");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return mutex;
    }

    public void unlock(InterProcessMutex mutex, String key)
    {
        //完成业务流程, 释放锁
        try
        {
            mutex.release();
            log.info(Thread.currentThread().getName() + ":释放锁");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        ZkLockUtil lockUtil = new ZkLockUtil();
        InterProcessMutex lock = null;
        try
        {
            lock = lockUtil.lock("/test/lock1");

            System.out.println("开始做事");


        }finally
        {
            lockUtil.unlock(lock, "/test/lock1");
        }

        System.out.println("结束");


    }
}
