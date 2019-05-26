//package com.zsk.template.config.schedule.quartz;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.quartz.*;
//
//import java.util.UUID;
//
///*
// * @Description:
// * @Author: zsk
// * @Date: 2018-09-21 14:09
// */
//@Slf4j
//@DisallowConcurrentExecution
//public class SyncDataJob implements Job
//{
//
//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException
//    {
//        JobDataMap dataMap = context.getMergedJobDataMap();
//        String type = (String) dataMap.get("type");
//        EsAdminService esAdminService = (EsAdminService) dataMap.get("esAdminService");
//        StringRedisTemplate redisTemplate = (StringRedisTemplate) dataMap.get("redisTemplate");
//
//        if (!StringUtils.isEmpty(type) && esAdminService != null)
//        {
//
//            String uuid = UUID.randomUUID().toString();
//            try
//            {
//                //不停的尝试获取lock
//                while (!LockUtil.tryLock(redisTemplate, type, uuid, 10))
//                {
//                    Thread.sleep(1000);
//                    ;
//                }
//                log.info("同步type:[{}] 开始", type);
//                //业务逻辑
//                esAdminService.syncData(type);
//                /*
//                try
//                {
//                    Thread.sleep(10000);
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }*/
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//            finally
//            {
//                //解锁
//                LockUtil.releaseLock(redisTemplate, type, uuid);
//                log.info("同步type:[{}] 结束", type);
//
//            }
//        }
//    }
//}
