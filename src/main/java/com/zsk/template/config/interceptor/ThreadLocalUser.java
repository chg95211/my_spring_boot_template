package com.zsk.template.config.interceptor;


import com.zsk.template.model.TbUser;

/**
 * @description:
 * @author: zsk
 * @create: 2018-10-02 18:22
 **/
public class ThreadLocalUser
{
    private static final ThreadLocal<TbUser> THREAD_LOCAL_USER = new ThreadLocal<>();

    public static void setUser(TbUser user)
    {
        THREAD_LOCAL_USER.set(user);
    }

    public static TbUser getUser()
    {
        return THREAD_LOCAL_USER.get();
    }

    public static void removeUser()
    {
        THREAD_LOCAL_USER.remove();
    }
}
