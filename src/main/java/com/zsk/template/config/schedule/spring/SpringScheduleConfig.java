package com.zsk.template.config.schedule.spring;

/*
 * @Description:配置执行调度任务的线程池
 * @Author: zsk
 * @Date: 2018-10-17 09:29
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SpringScheduleConfig implements SchedulingConfigurer
{

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar)
    {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.initialize();

        //设置线程池容量
        scheduler.setPoolSize(10);
        //线程名前缀
        scheduler.setThreadNamePrefix("sync-task-");
        //当scheduler shutdown时，等待当前被调度的任务完成
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        //配合setWaitForTasksToCompleteOnShutdown使用，当spring container关闭时最多等待的时长
        scheduler.setAwaitTerminationSeconds(60);
        //设置当任务被取消的同时从当前调度器移除的策略
        scheduler.setRemoveOnCancelPolicy(true);

        taskRegistrar.setTaskScheduler(scheduler);
    }
}
