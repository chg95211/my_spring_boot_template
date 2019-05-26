package com.zsk.template.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

/**
 * @description:
 * @author: zsk
 * @create: 2018-09-22 10:03
 **/
@Slf4j
@Component
@Aspect//aspect 由 pointcut 和 advice组成
public class LogAspect
{
    //使用ThreadLocal是防止单例下多线程访问使时间错乱
    private ThreadLocal<Instant> startTime = new ThreadLocal<>();

    //pointcut
    //*表示返回值任意
    // com.paic.ucmdb.search.controller..表示controller包和子包
    // *表示类名任意
    // insert*表示insert开头的方法
    // (..)表示参数任意
    @Pointcut("execution(* com.zsk.template.controller.*.*(..))")
    public void controllerLog(){}

    @Before(value = "controllerLog()")
    public void before(JoinPoint joinPoint)
    {
        startTime.set(Instant.now());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    //this method is advice
    @AfterReturning(value = "controllerLog()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result)
    {
        StringBuilder builder = new StringBuilder();
        this.logSplitStart(builder);

        this.logMethod(joinPoint, builder);
        if (result != null)
        {
            builder.append(String.format("Return: %s\n", result.toString()));
        }

        this.logCostTime(builder);

        this.logSplitEnd(builder);


        log.info(builder.toString());

        startTime.remove();
    }

    private void logCostTime(StringBuilder builder)
    {
        Instant endTime = Instant.now();
        long cost = Duration.between(startTime.get(), endTime).toMillis();


        builder.append(String.format("Cost: 执行时长[%s]ms\n", cost));
    }

    //this method is advice
    @AfterThrowing(value = "controllerLog()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e)
    {
        StringBuilder builder = new StringBuilder();
        this.logSplitStart(builder);


        this.logMethod(joinPoint, builder);
        if (e != null)
        {

        }

        this.logSplitEnd(builder);

        log.info(builder.toString());

        startTime.remove();
    }


    private void logMethod(JoinPoint joinPoint, StringBuilder builder)
    {
        builder.append(String.format("Method: %s\n", joinPoint.getSignature().toString()));
        this.logParameters(joinPoint, builder);
    }

    private void logParameters(JoinPoint joinPoint, StringBuilder builder)
    {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length <= 0)
            return;

        builder.append("Params: (");

        if (args.length > 0)
        {
            builder.append(args[0].toString());

            for (int i = 1; i < args.length; i++)
            {
                builder.append(",");
                builder.append(args[i].toString());
            }
        }

        builder.append(")\n");

    }


    private void logSplitStart(StringBuilder builder)
    {
        builder.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
    }

    private void logSplitEnd(StringBuilder builder)
    {
        builder.append("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
    }
}
