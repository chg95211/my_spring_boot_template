package com.zsk.template.service;

import com.zsk.template.model.TbUser;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-12 16:44
 **/
public interface UserService
{
    TbUser getByName(String name);
    TbUser getById(Long id);
    int removeById(Long id);
    TbUser getUserInfo();

    TbUser getByToken(String token);
}
