package com.zsk.template.config.aop.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit
{

    String key();
    String prefix();
    String limit();

}