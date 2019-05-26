package com.zsk.template.util;

import com.zsk.template.config.exception.ParameterException;
import lombok.NoArgsConstructor;
import okhttp3.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-14 23:19
 **/
public class HttpUtil
{

    public static void main(String[] args)
    {
        Map map = getForPojo("http://ip.taobao.com/service/getIpInfo.php?ip=63.223.108.42", Map.class);
        System.out.println(map);
    }

    private HttpUtil(){}

    private static final OkHttpClient httpClient = new OkHttpClient();

    public  static <T> T getForPojo(String url, Class<T> clazz)
    {
        HttpUrl.Builder urlBuilder =  HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("test", "test");

        Request request = new Request.Builder()
                                        .url(urlBuilder.build())
                                        .build();

        String response = doCall(request);
        return JsonUtil.jsonToPojo(response, clazz);

    }

    private static String doCall(Request request)
    {
        try
        {
            Response response = httpClient.newCall(request).execute();
            if (!response.isSuccessful())
            {
                throw new ParameterException("服务器端错误: " + response);
            }
            String result = null;
            if (ObjectUtils.isNotEmpty(response.body()))
            {
                result = response.body().string();
            }
            return result;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ParameterException("服务器端错误: " );
        }
    }

    public static <T> T postForPojo(String url, Class<T> clazz)
    {
        MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/json");
        String postBody = null;
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_TEXT, postBody))
                .build();

        String response = doCall(request);
        return JsonUtil.jsonToPojo(response, clazz);
    }


    public static void addCookie(HttpServletResponse response, String key, String value, String path, int cookieExpireTime)
    {
//        Cookie cookie = new Cookie(key, value);
//        cookie.setPath(path);
//        cookie.setMaxAge(cookieExpireTime);
//        response.addCookie(cookie);

        response.addCookie(CookieBuilder.builder()
                .keyValue(key, value)
                .path(path)
                .maxAge(cookieExpireTime)
                .build());
    }

    public static void removeCookie(HttpServletResponse response, String key, String path)
    {
//        Cookie cookie = new Cookie(key, "");
//        cookie.setPath(path);
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);

        response.addCookie(CookieBuilder.builder()
                                        .keyValue(key, "")
                                        .path("/")
                                        .maxAge(0)
                                        .build());
    }


    public static String getCookieValue(HttpServletRequest request, String key)
    {
        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isEmpty(cookies))
        {
            return null;
        }
        for (Cookie cookie : cookies)
        {
            if (cookie.getName().equals(key))
            {
                return cookie.getValue();
            }
        }

        return null;
    }
}

class CookieBuilder
{
    private Cookie cookie;

    public static CookieBuilder builder()
    {
        return new CookieBuilder();
    }

    public Cookie build()
    {
        return this.cookie;
    }

    public CookieBuilder keyValue(String key, String value)
    {
        this.cookie = new Cookie(key, value);
        return this;
    }

    public CookieBuilder path(String path)
    {
        this.cookie.setPath(path);
        return this;
    }


    public CookieBuilder maxAge(int maxAge)
    {
        this.cookie.setMaxAge(maxAge);
        return this;
    }

}
