package com.zsk.template.config.interceptor;

import com.zsk.template.model.TbUser;
import com.zsk.template.util.HttpUtil;
import com.zsk.template.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//无法用compoment来注解,可能是因为intercept是由其他类而不是spring先生成
@Slf4j
public class UserInfoInterceptor implements HandlerInterceptor
{
    /*
     * 一般网页使用isRemember或者isAuthenticated
     * 特殊的必须isAuthenticated
     *
     * */
    //请求之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception
    {
        String token = HttpUtil.getCookieValue(request, "token");
        if (StringUtils.isEmpty(token))
        {
            token = request.getHeader("token");

        }
        if (StringUtils.isNotEmpty(token))
        {
            Object principal = SecurityUtils.getSubject().getPrincipal();
            if (principal instanceof TbUser)
            {
                ThreadLocalUser.setUser((TbUser) principal);
                log.info("============用户已登录，登录用户为{}============", principal);
            }
            else
            {
                ThreadLocalUser.setUser(new TbUser());
                log.info("============用户未登录============");
            }
        }

        //        //获取request header中user json
        //        String userJson = request.getHeader("user");
        //
        //        TbUser user = null;
        //        //检查是否空串
        //        if (StringUtils.isEmpty(userJson))
        //            user = new TbUser();
        //        //不是则序列化为User对象
        //        else
        //            user = JsonUtil.jsonToPojo(userJson, TbUser.class);
        //        //把存储到ThreaLocal中
        //        ThreadLocalUser.setUser(user);
        return true;
    }

    //请求之后
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception
    {

    }

    //处理完成
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception
    {
        //请求结束后把User从ThreadLocal中删除,以免OOM
        ThreadLocalUser.removeUser();
    }
}
