package com.zsk.template.util;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

/**
 * @description:
 * @author: zsk
 * @create: 2018-09-16 17:25
 **/
public class StringUtil
{
    /**
     * @Description: test_data -> testData
     * @Param:
     * @return:
     */
    public static String test_data2testData(String source)
    {
        if (StringUtils.isEmpty(source))
            throw new NullPointerException("source is null");

        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, source);
    }


    /**
     * @Description: test_data- > TestData
     * @Param:
     * @return:
     */
    public static String test_data2TestData(String source)
    {
        if (StringUtils.isEmpty(source))
            throw new NullPointerException("source is null");

        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, source);
    }


    /**
     * @Description: TestData ->  test_data
     * @Param:
     * @return:
     */
    public static String TestData2test_data(String source)
    {
        if (StringUtils.isEmpty(source))
            throw new NullPointerException("source is null");

        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, source);
    }

    /**
     * @Description: 所有字母转换成大写
     * @Param:
     * @return:
     */
    public static String toUpperCase(String source)
    {
        if (StringUtils.isEmpty(source))
            throw new NullPointerException("source is null");

        return source.toUpperCase();
    }

    /**
     * @Description: 所有字母转换成大写
     * @Param:
     * @return:
     */
    public static String toLowerCase(String source)
    {
        if (StringUtils.isEmpty(source))
            throw new NullPointerException("source is null");

        return source.toLowerCase();
    }

    /**
     * @Description: 首字母转大写
     * @Param:
     * @return:
     */
    public static String initialCapitalization(String source)
    {
        //source = source.substring(0, 1).toUpperCase() + source.substring(1);
        //return source;
        char[] chars = source.toCharArray();
        chars[0] -= 32;
        return String.valueOf(chars);
    }

}
