//package com.zsk.template.config.aop;
//
//import com.zsk.template.config.aop.annotation.Cacheable;
//import com.zsk.template.model.TbItem;
//import com.zsk.template.util.SpelParser;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//import java.util.concurrent.TimeUnit;
//
///**
// * @description:
// * @author: zsk
// * @create: 2018-12-17 22:55
// **/
//@Component
//@Aspect
//public class CacheableAspect
//{
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//
//    @Pointcut("@annotation(cacheable)")
//    public void cacheableAnnotation(Cacheable cacheable)
//    {
//    }
//
//    @Around(value = "cacheableAnnotation(cacheable)")
//    public Object around(ProceedingJoinPoint pjp, Cacheable cacheable)
//    {
//        Object result = null;
//
//        String key = getKeyValue(cacheable.key(), pjp);
//        String cacheName = cacheable.cacheName();
//
//        ValueOperations<String, TbItem> opt = this.redisTemplate.opsForValue();
//        try
//        {
//            result = opt.get(cacheName + key);
//            if (result != null)
//                return result;
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//
//        try
//        {
//            result = pjp.proceed();
//        }
//        catch (Throwable throwable)
//        {
//            throwable.printStackTrace();
//        }
//
//        try
//        {
//            opt.set(result + key, (TbItem) result, 20, TimeUnit.SECONDS);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    private String getKeyValue(String key, ProceedingJoinPoint pjp)
//    {
//        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
//        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
//        return SpelParser.getKey(key, parameterNames, pjp.getArgs());
//    }
//}
