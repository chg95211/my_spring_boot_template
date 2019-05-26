package com.zsk.template.util;

import java.util.UUID;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-12 19:47
 **/
public class IdUtil
{
    public static String uuid()
    {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
