package com.zsk.template.service.impl;

import com.zsk.template.service.AuthService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-12 22:58
 **/
@Service
public class AuthServiceImpl implements AuthService
{

    @Override
    public void logout()
    {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated())
        {
            subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓
        }
    }

    @Override
    public String login(String name, String password)
    {
        Subject currentUesr = SecurityUtils.getSubject();

        if (!currentUesr.isAuthenticated())
        {
//            new SimpleHash("MD5", String.valueOf(password));
            UsernamePasswordToken token = new UsernamePasswordToken(name, DigestUtils.md5DigestAsHex(String.valueOf(password).getBytes()));
            //todo In order for the DefaultSerializer to serialize this object, the [org.apache.shiro.subject.SimplePrincipalCollection] class must implement java.io.Serializable
//            token.setRememberMe(true);
            //最终token会传递到UserRealm的doGetAuthenticationInfo方法里
            currentUesr.login(token);
        }
        Session session = currentUesr.getSession();
        return (String) session.getId();
    }
}
