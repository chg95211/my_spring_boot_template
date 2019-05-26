package com.zsk.template.config.schedule.spring;

import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-12 13:20
 **/
@Component
public class SpringTask
{
    @Scheduled(fixedRate = 2000)
    @SchedulerLock(name = "jobA", lockAtLeastForString = "2000", lockAtMostForString = "2000")
    public void jobA()
    {
        System.out.println("TaskA" + new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSS")
                .format(new Date()));
    }


    @Scheduled(cron = "* * * * * ?")
    @SchedulerLock(name = "jobB", lockAtLeastForString = "2000", lockAtMostForString = "2000")
    public void jobB()
    {
        System.err.println("TaskB" + new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSS")
                .format(new Date()));
    }
}
