package com.zsk.template.service.impl;

import com.zsk.template.service.AsyncService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-12 14:15
 **/
@Service
public class AsyncServiceImpl implements AsyncService
{
    @Async("taskExecutor")
    public void asyncTask()
    {
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        System.out.println("aync======================");
    }
}
