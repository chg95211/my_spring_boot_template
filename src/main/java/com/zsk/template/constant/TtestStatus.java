package com.zsk.template.constant;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zsk.template.config.exception.ParameterException;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-29 22:04
 **/
public enum TtestStatus
{
    //name决定了插入数据库的值
    closed("closed", "关闭"), open("open", "打开");
    private String key;
    private String value;

    TtestStatus(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    @JsonCreator//决定了前端传值必须为key
    public static TtestStatus getItem(String key)
    {
        for (TtestStatus item : values())
        {
            if (item.getKey().equals(key))
            {
                return item;
            }
        }
        throw new ParameterException(String.format("无法序列化的status:%s", key));
    }


    @JsonValue//决定了返回给前端的是key
    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
