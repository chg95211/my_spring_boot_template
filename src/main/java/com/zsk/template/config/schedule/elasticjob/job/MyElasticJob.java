//package com.zsk.template.config.schedule.elasticjob.job;
//
//import com.dangdang.ddframe.job.api.ShardingContext;
//import com.dangdang.ddframe.job.api.simple.SimpleJob;
//import com.zsk.template.config.aop.annotation.CostTime;
//import com.zsk.template.constant.EsSearchTypeEnum;
//import com.zsk.template.service.EsAdminService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//
//
///**
// * @description:
// * @author: zsk
// * @create: 2018-10-01 23:02
// **/
//@Slf4j
//public class MyElasticJob implements SimpleJob
//{
//    @Autowired
//    private EsAdminService esAdminService;
//
//    @CostTime(threshold = 5000)//记录同步超过5s
//    @Override
//    public void execute(ShardingContext shardingContext)
//    {
//
//        log.info(
//                String.format("------Thread ID: %s, 任务总片数: %s, 当前分片项: %s",
//                Thread.currentThread().getId(),
//                shardingContext.getShardingTotalCount(),
//                shardingContext.getShardingItem()));
//
//        EsSearchTypeEnum[] types = EsSearchTypeEnum.values();
//
//        for (int i = 0; i < types.length; i++)
//        {
//            if (i % shardingContext.getShardingTotalCount() == shardingContext.getShardingItem())
//            {
//                esAdminService.syncData(types[i].getType());
//            }
//        }
//
//    }
//}