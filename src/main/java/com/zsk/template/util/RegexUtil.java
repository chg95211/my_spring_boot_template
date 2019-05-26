package com.zsk.template.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: zsk
 * @create: 2018-09-15 12:04
 **/
public class RegexUtil
{
    /**
     * @Description:判断line是否符合pattern模式
     * @Param:
     * @return:
     */
    public static boolean isMatch(String line, String pattern)
    {
        if (line == null || pattern == null)
            return false;

        Pattern re = Pattern.compile(pattern);
        Matcher m = re.matcher(line);
        return m.find();
    }

    /**
     * @Description: 获取正则匹配的所有组
     * @Param:
     * @return:
     */
    public static List<String> getMatchGroups(String line, String pattern)
    {
        List<String> groups = new ArrayList<>();

        if (line == null || pattern == null)
            return groups;

        Pattern re = Pattern.compile(pattern);
        Matcher m = re.matcher(line);

        if (m.find())
        {
            for (int i = 0; i < m.groupCount() + 1; i++)
            {
                groups.add(m.group(i));
            }
        }

        return groups;
    }
}
