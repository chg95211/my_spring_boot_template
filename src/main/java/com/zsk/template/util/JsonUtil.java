package com.zsk.template.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

//json序列化工具类
public class JsonUtil
{
    private static final ObjectMapper mapper = new ObjectMapper();

    static
    {
        //solve the problem that Unrecognized field, not marked as ignorable
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    }

    /**
     * @Description: pojo to json str
     * @Param:
     * @return:
     */
    public static String objectToJson(Object object)
    {
        if (object == null)
        {
            throw new NullPointerException("object is null");
        }

        String jsonStr = null;

        try
        {
            jsonStr = mapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return jsonStr;

    }

    /**
     * @Description: json str to pojo
     * @Param:
     * @return:
     */
    public static <T> T jsonToPojo(String json, Class<T> pojoType)
    {
        if (StringUtils.isEmpty(json) || pojoType == null)
        {
            throw new NullPointerException("json or pojoType is null");
        }


        T t = null;
        try
        {
            t = mapper.readValue(json, pojoType);
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

        return t;
    }

    /**
     * @Description: json str to list
     * @Param:
     * @return:
     */
    public static <T> List<T> jsonToList(String json, Class<T> pojoType)
    {
        if (StringUtils.isEmpty(json) || pojoType == null)
        {
            throw new NullPointerException("json or pojoType is null");
        }

        //转换一下type为list
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, pojoType);
        List<T> list = null;
        try
        {
            list = mapper.readValue(json, javaType);
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

        return list;
    }
}
