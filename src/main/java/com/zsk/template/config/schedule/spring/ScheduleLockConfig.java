package com.zsk.template.config.schedule.spring;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SchedulerLock;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.ScheduledLockConfiguration;
import net.javacrumbs.shedlock.spring.ScheduledLockConfigurationBuilder;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * @Description: 定时调度配置
 * @Author: zsk
 * @Date: 2018-09-11 09:04
 */
@Slf4j
@Configuration
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class ScheduleLockConfig
{
    //配置防止定时任务重复执行的锁，但不是分布式锁
    @Bean
    public LockProvider lockProvider(DataSource ds)
    {
        return new JdbcTemplateLockProvider(ds, "SHEDLOCK");
    }
    @Bean
    public ScheduledLockConfiguration taskScheduler(LockProvider lockProvider)
    {
        return ScheduledLockConfigurationBuilder
                .withLockProvider(lockProvider)
                .withPoolSize(10)
                .withDefaultLockAtMostFor(Duration.ofMinutes(10))
                .build();
    }


}
