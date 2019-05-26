package com.zsk.template.config.aop;

import com.zsk.template.config.aop.annotation.CostTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * @description:
 * @author: zsk
 * @create: 2018-10-04 13:29
 **/
@Slf4j
@Component
@Aspect
public class CostTimeAspect
{

    private ThreadLocal<Instant> startTime = new ThreadLocal<>();

    @Pointcut("@annotation(costTime)")
    public void costTimeAnnotation(CostTime costTime)
    {
    }

    @Before(value = "costTimeAnnotation(costTime)")
    public void before(JoinPoint joinPoint, CostTime costTime)
    {
        startTime.set(Instant.now());
    }

    @AfterReturning(value = "costTimeAnnotation(costTime)")
    public void after(JoinPoint joinPoint, CostTime costTime)
    {
        Instant endTime = Instant.now();

        long cost = Duration.between(startTime.get(), endTime).toMillis();

        if (cost > costTime.threshold())
            log.info("\nMethod:[{}]执行时长[{}]ms", joinPoint.getSignature().toString(), cost);

        startTime.remove();
    }

    @AfterThrowing(value = "costTimeAnnotation(costTime)", throwing = "e")
    public void afterException(JoinPoint joinPoint, CostTime costTime, Throwable e)
    {
        startTime.remove();
    }

}
