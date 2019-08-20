package com.zsk.template.config.aop;

import com.google.common.util.concurrent.RateLimiter;
import com.zsk.template.config.aop.annotation.RateLimit;
import com.zsk.template.config.exception.ParameterException;
import com.zsk.template.util.SpelParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Aspect
@Component
@Slf4j
public class RateLimiterAspect
{

    @Autowired
    private Environment env;

    private final ConcurrentHashMap<String, RateLimiter> limiters = new ConcurrentHashMap<>();

    @Pointcut("@annotation(rateLimit)")
    public void rateLimiteAnnotation(RateLimit rateLimit)
    {
    }

    @Around(value = "rateLimiteAnnotation(rateLimit)")
    public Object around(ProceedingJoinPoint pjp, RateLimit rateLimit)
    {
        Object result = null;

        String prefix = rateLimit.prefix();
        String keyProperty = prefix;
        String limit = rateLimit.limit();

        RateLimiter limiter = limiters.computeIfAbsent(keyProperty, createLimiter(keyProperty, limit));

        if (limiter != null)
        {
            if (limiter.tryAcquire())
            {
                log.info("可以访问");
                try
                {
                    result = pjp.proceed();
                }
                catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                }
            }
            else
            {
                throw new ParameterException(String.format("访问超过限制：%s", limiter.getRate()));
            }
        }


        return result;
    }

    private Function<? super String,? extends RateLimiter> createLimiter(String keyProperty, String limit)
    {
        try
        {
            double limitDoubleVal = Double.valueOf(limit);
            return name -> RateLimiter.create(limitDoubleVal);
        }
        catch (NumberFormatException e)
        {
            log.error("限流属性转换异常.... key : " + keyProperty);
            return name -> RateLimiter.create(Double.MAX_VALUE);
        }
    }

    public static void main(String[] args)
    {
        RateLimiter limiter = RateLimiter.create(10);
        for (int i = 0; i < 100; i++)
        {
            limiter.acquire();
            System.out.println(i + ":get lock");
        }
    }

}