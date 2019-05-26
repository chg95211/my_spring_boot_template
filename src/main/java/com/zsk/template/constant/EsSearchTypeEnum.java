package com.zsk.template.constant;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-25 16:59
 **/
public enum  EsSearchTypeEnum
{
    User("user", "用户"),
    Test("test","测试");
    private String type;
    private String name;

    EsSearchTypeEnum(String type, String name)
    {
        this.type = type;
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }
}
