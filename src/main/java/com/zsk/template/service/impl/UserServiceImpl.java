package com.zsk.template.service.impl;

import com.zsk.template.constant.RedisKeyPrefix;
import com.zsk.template.dao.UserDao;
import com.zsk.template.model.TbUser;
import com.zsk.template.service.UserService;
import com.zsk.template.util.JsonUtil;
import com.zsk.template.util.jedis.JedisClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-12 16:44
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserDao userDao;

    @Autowired
    private JedisClient jedisClient;

    @Override
    public TbUser getByName(String name)
    {
        Example example = new Example(TbUser.class);
        example.createCriteria().andEqualTo("username", name);
        return this.userDao.selectOneByExample(example);
    }

    @Cacheable(value = "userCache", key = "'user:id:'.concat(#id)", unless="#result == null")
    @Override
    public TbUser getById(Long id)
    {
        //        Example example = new Example(TbUser.class);
        //        example.createCriteria().andEqualTo("id",id);
        //必须在id上加@Id注解

        try
        {
            String userJson = jedisClient.get(RedisKeyPrefix.UserCache.prefix() + id);
            if (StringUtils.isNotEmpty(userJson))
            {
                return JsonUtil.jsonToPojo(userJson, TbUser.class);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        TbUser user = this.userDao.selectByPrimaryKey(id);

        try
        {
            if (user != null)
            {
                jedisClient.set(RedisKeyPrefix.UserCache.prefix() + id, JsonUtil.objectToJson(user));
                jedisClient.expire(RedisKeyPrefix.UserCache.prefix() + id, RedisKeyPrefix.UserCache.expire());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return user;
    }

    @CacheEvict(value = "userCache", key = "'user:id:'.concat(#id)")
    @Override
    public int removeById(Long id)
    {
        try
        {
            jedisClient.del(RedisKeyPrefix.UserCache.prefix() + id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this.userDao.deleteByPrimaryKey(id);
    }

    @Override
    public TbUser getUserInfo()
    {
        Subject currentUesr = SecurityUtils.getSubject();
        //        if (currentUesr.isAuthenticated())
        //        {
        Object principal = currentUesr.getPrincipal();
        if (principal instanceof TbUser)
        {
            return (TbUser) principal;
        }
        //        }

        return null;
    }

}
