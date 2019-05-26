package com.zsk.template.config.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @author: zsk
 * @create: 2018-10-04 13:31
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CostTime
{
    long threshold() default 0;//阈值，单位：ms
}
