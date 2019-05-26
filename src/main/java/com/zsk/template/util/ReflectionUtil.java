package com.zsk.template.util;

import com.zsk.template.config.exception.ParameterException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/*
 * @Description:
 * @Author: zsk
 * @Date: 2018-09-12 10:31
 */
public class ReflectionUtil
{

    private static String RE_PACKAGE = "\\w(\\.\\w)*";
    private static String RE_CLASS = "\\w+\\.class$";

    /**
     * @Description: 读取scanPackage下名为className的file并创建class.注意:只扫描一层
     * @Param:
     * @return:
     */
    public static Class scanClass(String scanPackage, String className)
    {
        Class clazz = null;

        if (StringUtils.isEmpty(scanPackage) || StringUtils.isEmpty(className))
            throw new NullPointerException("scanPacake or className is null");

        if (!RegexUtil.isMatch(scanPackage, RE_PACKAGE) || !RegexUtil.isMatch(className, RE_CLASS))
            throw new ParameterException("scanPacake or className is not valid. Example: scanPacake: xxx.yyy.zzz, className: XXX.class");

        String scanPackageFileName = scanPackage.replaceAll("\\.", "/");

        File dir = FileUtil.getDirectoryByRelativePath(scanPackageFileName);
        if (dir != null)
            clazz = getClass(dir, scanPackage, className);

        return clazz;
    }


    private static Class getClass(File dir, String scanPackage, String className)
    {
        Class clazz = null;
        for (File f : dir.listFiles())
        {
            String fName = f.getName();

            if (f.isFile() && fName.equals(className))
            {
                String classFullName = scanPackage + "." + fName.substring(0, fName.length() - 6);
                try
                {
                    clazz = Class.forName(classFullName);
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                break;
            }
        }

        return clazz;
    }


    /**
     * @Description:获取class上的annotation
     * @Param:
     * @return:
     */
    public static <T> T getAnnotation(Class clazz, Class<T> annotationClass)
    {
        if (clazz == null || annotationClass == null)
            throw new NullPointerException("clazz or annotationClass is null");

        return (T) clazz.getAnnotation(annotationClass);
    }


    /**
     * @Description: 获取class上被annotation修饰的method
     * @Param:
     * @return:
     */
    public static Method getMethod(Class clazz, Class annotation)
    {
        Method method = null;

        if (clazz == null || annotation == null)
            throw new NullPointerException("clazz or annotation is null");

        for (Method m : clazz.getDeclaredMethods())
        {
            if (hasAnnotation(m, annotation))
            {
                method = m;
                break;
            }

        }

        return method;
    }

    /**
     * @Description: 查看Field是否有 某个注解 修饰
     * @Param:
     * @return:
     */
    public static boolean hasAnnotation(Field field, Class annotation)
    {
        if (field == null || annotation == null)
            throw new NullPointerException("field or annotation is null");

        return ArrayUtils.isNotEmpty(field.getAnnotationsByType(annotation));
    }

    /**
     * @Description: 查看Method是否有 某个注解 修饰
     * @Param:
     * @return:
     */
    public static boolean hasAnnotation(Method method, Class annotation)
    {
        if (method == null || annotation == null)
            throw new NullPointerException("method or annotation is null");

        return ArrayUtils.isNotEmpty(method.getAnnotationsByType(annotation));
    }

    /**
     * @Description: 查看Class是否有 某个注解 修饰
     * @Param:
     * @return:
     */
    public static boolean hasAnnotation(Class clazz, Class annotation)
    {
        if (clazz == null || annotation == null)
            throw new NullPointerException("clazz or annotation is null");

        return ArrayUtils.isNotEmpty(clazz.getAnnotationsByType(annotation));
    }

    /**
     * @Description: 获取clazz中field name匹配paatern的field
     * @Param:
     * @return:
     */
    public static List<Field> getFieldMatchPattern(Class clazz, String pattern)
    {
        if (clazz == null || pattern == null)
            throw new NullPointerException("clazz or pattern is null");

        List<Field> fields = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields())
        {
            if (RegexUtil.isMatch(field.getName(), pattern))
                fields.add(field);
        }

        return fields;
    }

}
