package com.zsk.template.util;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-12 13:40
 **/
@Component
public class ResourcesUtil
{
    @Resource
    private MessageSource messageSource;


    public String getMessage(String key, String ...args)
    {
        return this.messageSource.getMessage(key,args, Locale.getDefault());
    }

}
