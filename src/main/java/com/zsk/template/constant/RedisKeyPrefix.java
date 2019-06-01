package com.zsk.template.constant;


/**
 * @description:
 * @author: zsk
 * @create: 2019-05-12 19:36
 **/
public enum RedisKeyPrefix
{
    UserToken("user:token:", 30 * 60),
    UserCache("user:id:",30 * 60),
    MiaoShaItem("miaosha:id:",-1);

    RedisKeyPrefix(String prefix, Integer expire)
    {
        this.prefix = prefix;
        this.expire = expire;
    }

    private String prefix;
    private Integer expire;

    public String prefix()
    {
        return prefix;
    }

    public Integer expire()
    {
        return expire;
    }
}
