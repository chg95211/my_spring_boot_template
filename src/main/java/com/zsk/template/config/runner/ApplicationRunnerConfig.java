package com.zsk.template.config.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/*
 * @Description: 这个类会在程序启动时跟着执行run方法
 * @Author: zsk
 * @Date: 2018-09-21 14:26
 */
@Component
public class ApplicationRunnerConfig implements ApplicationRunner
{
//    @Autowired
//    private QuartzService quartzService;
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception
    {

        System.out.println("==================================服务已启动==================================");

//        this.quartzService.esDataSync();
    }

}
