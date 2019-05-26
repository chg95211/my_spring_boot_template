package com.zsk.template.config.shiro;

import com.zsk.template.constant.RedisKeyPrefix;
import com.zsk.template.util.IdUtil;
import com.zsk.template.util.JsonUtil;
import com.zsk.template.util.jedis.JedisClient;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-12 20:00
 **/
public class ShiroRedisSessionDao extends EnterpriseCacheSessionDAO
{
    @Autowired
    private JedisClient jedisClient;

    // 创建session，保存到数据库
    @Override
    protected Serializable doCreate(Session session)
    {
        String sessionId = (String) super.doCreate(session);
        //RedisKeyPrefix.UserToken.prefix() + IdUtil.uuid();
//        this.assignSessionId(session, sessionId);
        String key = RedisKeyPrefix.UserToken.prefix() + sessionId;
        jedisClient.set(key, JsonUtil.objectToJson(session));
        jedisClient.expire(key, RedisKeyPrefix.UserToken.expire());
        return sessionId;
    }

    // 获取session
    @Override
    protected Session doReadSession(Serializable sessionId)
    {
        // 先从缓存中获取session，如果没有再去数据库中获取
        Session session = super.doReadSession(sessionId);
        if (session == null)
        {
            String json = jedisClient.get(RedisKeyPrefix.UserToken.prefix() + sessionId);
            if (json != null)
            {
                session = JsonUtil.jsonToPojo(json, Session.class);
            }
        }
        return session;
    }

    // 更新session的最后一次访问时间
    @Override
    protected void doUpdate(Session session)
    {
        super.doUpdate(session);
        String key = RedisKeyPrefix.UserToken.prefix() + session.getId();
        jedisClient.set(key,JsonUtil.objectToJson(session));
        jedisClient.expire(key, RedisKeyPrefix.UserToken.expire());
    }

    // 删除session
    @Override
    protected void doDelete(Session session)
    {
        super.doDelete(session);
        jedisClient.del(RedisKeyPrefix.UserToken.prefix() + session.getId());
    }

}
