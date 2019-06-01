package com.zsk.template.controller;

import com.zsk.template.model.TbUser;
import com.zsk.template.model.response.Response;
import com.zsk.template.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-14 20:54
 **/
@RestController("/users")
public class UserController
{
    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public Response getUserInfo()
    {
        return Response.success(this.userService.getUserInfo());
    }

//    不能配置user/{id}，跟shiro有冲突
    @DeleteMapping("/user/{id}")
    public Response removeById(@PathVariable Long id)
    {
        return Response.success(this.userService.removeById(id));
    }


    @GetMapping("/user/{id}")
    public Response getById(@PathVariable Long id)
    {
        return Response.success(this.userService.getById(id));

    }

    @GetMapping("/info/{token}")
    public Response getByToken(@PathVariable String token)
    {
        return Response.success(this.userService.getByToken(token));
    }
}
