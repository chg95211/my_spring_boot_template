//package com.zsk.template.config.schedule.elasticjob;
//
//import com.dangdang.ddframe.job.api.simple.SimpleJob;
//import com.dangdang.ddframe.job.config.JobCoreConfiguration;
//import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
//import com.dangdang.ddframe.job.event.JobEventConfiguration;
//import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
//import com.dangdang.ddframe.job.lite.api.JobScheduler;
//import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
//import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
//import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
//import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
//import com.zsk.template.config.schedule.elasticjob.job.MyElasticJob;
//import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
///**
// * @description:
// * @author: zsk
// * @create: 2018-10-01 23:02
// **/
//@Configuration
//@AutoConfigureAfter(MybatisAutoConfiguration.class)
//public class ElasticJobConfig
//{
//    @Value("${elasticjob.server-lists}")
//    private String serverList;
//    @Value("${elasticjob.namespace}")
//    private String namespace;
//
//    //配置zookeeper注册中心地址
//    @Bean
//    public ZookeeperConfiguration zkConfig()
//    {
//        return new ZookeeperConfiguration(serverList, namespace);
//    }
//
//    @Bean(initMethod = "init")
//    public ZookeeperRegistryCenter regCenter(ZookeeperConfiguration config)
//    {
//        return new ZookeeperRegistryCenter(config);
//    }
//
//    //job
//    @Bean
//    public SimpleJob simpleJob()
//    {
//        return new MyElasticJob();
//    }
//    //把job存储到数据库中
//    @Bean
//    public JobEventConfiguration jobEventConfiguration(DataSource dataSource)
//    {
//        return new JobEventRdbConfiguration(dataSource);
//    }
//
//
//    @Value("${simpleJob.cron}")
//    private String cron;
//    @Value("${simpleJob.shardingTotalCount}")
//    private int shardingTotalCount;
//    @Value("${simpleJob.shardingItemParameters}")
//    private String shardingItemParameters;
//
//    //scheduler
//    @Bean(initMethod = "init")
//    public JobScheduler simpleJobScheduler(ZookeeperRegistryCenter regCenter,
//                                           JobEventConfiguration jobEventConfiguration,
//                                           SimpleJob simpleJob,
//                                           LiteJobConfiguration jobConfiguration)
//    {
//        return new SpringJobScheduler(
//                simpleJob,
//                regCenter,
//                jobConfiguration,
//                jobEventConfiguration);
//    }
//
//
//    @Bean
//    public LiteJobConfiguration jobConfiguration()
//    {
//        Class<? extends SimpleJob> jobClass = SimpleJob.class;
//        JobCoreConfiguration.Builder builder = JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount);
//        JobCoreConfiguration build = builder.shardingItemParameters(shardingItemParameters).build();
//        SimpleJobConfiguration jobConfiguration = new SimpleJobConfiguration(build, jobClass.getCanonicalName());
//        return LiteJobConfiguration.newBuilder(jobConfiguration).overwrite(true).build();
//    }
//}