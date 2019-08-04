package com.zsk.template.service.impl;

import com.zsk.template.config.exception.ParameterException;
import com.zsk.template.constant.TtestStatus;
import com.zsk.template.dao.TtestDao;
import com.zsk.template.dao.UserDao;
import com.zsk.template.model.TbUser;
import com.zsk.template.model.Ttest;
import com.zsk.template.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-11 17:19
 **/
@Service
public class TestServiceImpl implements TestService
{
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TtestDao ttestDao;

    @Override
    public List<TbUser> list()
    {
        return userDao.selectAll();
    }

    @Transactional(readOnly = true,propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    @Override
    public Ttest getById(int id)
    {
//        throw new ParameterException("[getById]:======测试==测试=====");
//        throw new ParameterException(messageSource.getMessage("getByIdtesttest",null, Locale.CHINA));


        //        this.ttestDao.insertSelective(Ttest.builder().status(TtestStatus.open).build());
        return ttestDao.getTtestById(id);
    }
}
