package com.zsk.template.service.impl;

import com.zsk.template.dao.UserDao;
import com.zsk.template.model.TbUser;
import com.zsk.template.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-11 17:19
 **/
@Service
public class TestServiceImpl implements TestService
{
    @Autowired
    private UserDao userDao;


    @Override
    public List<TbUser> list()
    {
        return userDao.selectAll();
    }
}
