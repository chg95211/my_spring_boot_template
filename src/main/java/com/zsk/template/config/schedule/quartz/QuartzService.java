//package com.zsk.template.config.schedule.quartz;
//
//import org.quartz.*;
//import org.quartz.impl.StdSchedulerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
///*
// * @Description: 使用quartz执行的调度任务
// * @Author: zsk
// * @Date: 2018-09-21 15:40
// */
//@Service
//public class QuartzService
//{
//    private static Logger logger = LoggerFactory.getLogger(QuartzService.class);
//
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    @Value("${QUARTZ_SCHEDULE_TIME_INTERVAL}")
//    private Integer QUARTZ_SCHEDULE_TIME_INTERVAL;
//
//    @Autowired
//    private EsAdminService esAdminService;
//
//    /**
//    * @Description: 同步数据库中的数据到es中
//    * @Param:
//    * @return:
//    */
//    public void esDataSync()
//    {
//
//        String[] types = {};//.subList(0, 10);
//        if (types != null)
//        {
//            for (String type : types)
//            {
//                //传递的参数
//                JobDataMap map = new JobDataMap();
//                map.put("type", type);
//                map.put("esAdminService", this.esAdminService);
//                map.put("redisTemplate", redisTemplate);
//
//                //创建job
//                JobDetail jobDetail = JobBuilder.newJob(SyncDataJob.class)
//                        .usingJobData(map)
//                        .withIdentity(type,"es-sync-data")
//                        .build();
//                //创建trigger
//                Trigger trigger = TriggerBuilder.newTrigger()
//                        .forJob(jobDetail)
//                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(QUARTZ_SCHEDULE_TIME_INTERVAL))
//                        .withIdentity(type,"es-sync-data")
//                        .build();
//
//                this.executeTask(jobDetail, trigger);
//            }
//        }
//    }
//
//    //执行调度任务
//    private void executeTask(JobDetail jobDetail, Trigger trigger)
//    {
//        try
//        {
//            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//            scheduler.scheduleJob(jobDetail, trigger);
//            scheduler.start();
//        }
//        catch (SchedulerException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//}
