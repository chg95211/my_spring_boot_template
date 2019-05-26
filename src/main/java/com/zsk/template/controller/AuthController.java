package com.zsk.template.controller;

import com.zsk.template.model.TbUser;
import com.zsk.template.model.response.Response;
import com.zsk.template.service.AuthService;
import com.zsk.template.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-12 16:43
 **/
@Slf4j
@RestController
public class AuthController
{
    @Autowired
    private AuthService authService;

    @PostMapping("/logout")
    public Response logout()
    {
        authService.logout();

        return Response.success("退出成功");

    }

    @PostMapping("/login")
    public Response login(String name, String password)
    {
        String token = null;
        try
        {
            token = authService.login(name, password);
        }catch (UnknownAccountException e)
        {
            //登录失败
            e.printStackTrace();
            return Response.fail("用户名不存在");
        }
        catch (IncorrectCredentialsException e)
        {
            e.printStackTrace();
            return Response.fail("密码错误");
        }

        return Response.success(token);
    }

}
