package com.zsk.template.util.lock.zookeeper;

import com.zsk.template.util.lock.LockUtil;
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
public class ZkLockUtil implements LockUtil
{
    private static CuratorFramework client = null;
    private ConcurrentHashMap<String, InterProcessMutex> lockMap = new ConcurrentHashMap<>();

    static
    {
        //创建zookeeper的客户端
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        client.start();
    }

    @Override
    public Boolean lock(String key)
    {
        //创建分布式锁, 锁空间的根节点路径为key
        InterProcessMutex mutex = new InterProcessMutex(client, key);
        try
        {
            mutex.acquire();
            lockMap.put(key, mutex);
            log.info("获取了锁");
            TimeUnit.SECONDS.sleep(5);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean unlock(String key)
    {
        //创建分布式锁, 锁空间的根节点路径为key
        InterProcessMutex mutex = lockMap.get(key);//new InterProcessMutex(client, key);
        if(mutex == null)
        {
            return false;
        }
        //完成业务流程, 释放锁
        try
        {
            mutex.release();
            log.info("释放锁");
            lockMap.remove(key);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception
    {
        LockUtil lockUtil = new ZkLockUtil();
        lockUtil.lock("/test/lock");
        System.out.println("开始做事");
        lockUtil.unlock("/test/lock");
    }
}
