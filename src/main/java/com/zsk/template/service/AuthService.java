package com.zsk.template.service;

import com.zsk.template.model.TbUser;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-12 22:58
 **/
public interface AuthService
{
    void logout();

    String login(String name, String password);

}
