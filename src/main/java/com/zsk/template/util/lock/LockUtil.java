package com.zsk.template.util.lock;

/**
 * @description:
 * @author: zsk
 * @create: 2019-08-19 23:20
 **/
public interface LockUtil
{
    Boolean lock(String key);
    Boolean unlock(String key);
}
