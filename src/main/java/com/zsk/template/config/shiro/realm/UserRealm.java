package com.zsk.template.config.shiro.realm;

import com.zsk.template.model.TbUser;
import com.zsk.template.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-12 14:23
 **/
public class UserRealm extends AuthorizingRealm
{


    @Autowired
    private UserService userService;
    /*
    授权逻辑
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection)
    {
        System.out.println("授权逻辑");


        Subject subject = SecurityUtils.getSubject();
        //由doGetAuthenticationInfo的SimpleAuthenticationInfo的第一个参数传递
        TbUser user = (TbUser) subject.getPrincipal();
//        TbUser user = (TbUser)principalCollection.getPrimaryPrincipal();

        TbUser dbUser = userService.getById(user.getId());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermission(dbUser.getPerms());

        return info;
    }



    /*
    认证逻辑
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException
    {
        System.out.println("认证逻辑");

        //判断用户名 由SecurityUtils.getSubject().login(token)传递
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        TbUser user = userService.getByName(token.getUsername());
        if (null == user)
        {
            //Shiro会抛出UnknownAccoutException
//            throw new UnknownAccountException("用户不存在");
            return null;
        }

        //判断密码
        //数据库中user的实体类(doGetAuthorizationInfo的参数) 数据库中密码 当前realm对应的name
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }
}
