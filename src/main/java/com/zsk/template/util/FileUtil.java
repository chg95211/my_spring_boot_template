package com.zsk.template.util;

import com.zsk.template.config.exception.ParameterException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @description:
 * @author: zsk
 * @create: 2018-09-15 10:33
 **/
public class FileUtil
{
    private static String RE_FILE_RELATIVE_PATH = "^\\./?$|^\\w+$|(\\w+/)\\w*";
    
    /**
     * @Description: 获取相对于project根的目录
     * @Param: filePath 相对于project的目录名,如com/xxx/yyy/zzz
     * @return:
     */
    public static File getDirectoryByRelativePath(String filePath)
    {
        String pattern = RE_FILE_RELATIVE_PATH;
        if (StringUtils.isEmpty(filePath) || !RegexUtil.isMatch(filePath, pattern))
            throw new ParameterException("filePath格式错误,必须为xxx/yyy/zzz这种");

        File file = null;
        try
        {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(filePath);
            while (urls.hasMoreElements())
            {
                URL url = urls.nextElement();

                String protocol = url.getProtocol();
                if ("file".equals(protocol))
                {
                    file = new File(url.toString().substring(5));
                    break;

                }
                //其他类型:如jar,war等等
                //else if ()
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return file;
    }

    /**
     * @Description: 获取resources目录下的文件
     * @Param:
     * @return:
     */
    public static File getResourceFile(String filePath)
    {
        String pattern = RE_FILE_RELATIVE_PATH;
        if (StringUtils.isEmpty(filePath) || !RegexUtil.isMatch(filePath, pattern))
            throw new ParameterException("filePath格式错误,必须为xxx/yyy/zzz这种");

        File file = null;
        try
        {
            file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + filePath);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * @Description: 获取当前系统相关的文件路径分隔符. windows为\，linux为/
     * @Param:
     * @return:
     */
    public static String getSystemFilePathSeparator()
    {
        return File.separator;
    }

    /**
    * @Description: 处理windows路径为java路径
    * @Param:
    * @return:
    */
    public static String handleWindowsFilePath(String path)
    {
        return path.replaceAll("\\\\", "/");
    }

}
