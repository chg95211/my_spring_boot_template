package com.zsk.template.service;

import com.zsk.template.model.TbUser;
import com.zsk.template.model.Ttest;

import java.util.List;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-11 17:18
 **/
public interface TestService
{
    List<TbUser> list();
    Ttest getById(int id);
}
