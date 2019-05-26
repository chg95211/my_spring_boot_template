package com.zsk.template.config.shiro;

import com.zsk.template.config.shiro.realm.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-12 14:22
 **/
@Configuration
public class ShiroConfig
{
    @Bean
    public ShiroRedisSessionDao redisSessionDao() {
        return new ShiroRedisSessionDao();
    }

//    @Bean
//    public DefaultWebSessionManager sessionManager()
//    {
//        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        sessionManager.setSessionDAO(redisSessionDao());
//        return sessionManager;
//    }

    @Bean
    public DefaultHeaderSessionManager sessionManager()
    {
        DefaultHeaderSessionManager sessionManager = new DefaultHeaderSessionManager();
        sessionManager.setSessionDAO(redisSessionDao());
        return sessionManager;
    }


    @Bean
    public UserRealm userRealm()
    {
        UserRealm userRealm =  new UserRealm();
//        userRealm.setCredentialsMatcher(new HashedCredentialsMatcher("MD5"));
        return userRealm;
    }

    //多个realm的时候使用这个 同时可以指定认证策略
//    @Bean
//    public ModularRealmAuthenticator authenticator()
//    {
//        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
//
//        List<Realm> list = new ArrayList();
//        list.add(userRealm());
//        authenticator.setRealms(list);
//        authenticator.setAuthenticationStrategy();
//        return authenticator;
//    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(UserRealm userRealm,DefaultHeaderSessionManager sessionManager)
    {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(sessionManager);
//        securityManager.setAuthenticator(authenticator());
        //一般直接使用setRealms
//        securityManager.setRealms(null);
        return securityManager;
    }


    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager)
    {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

//            Ant风格
//            ？匹配一个字符
//            /*匹配多个字符
//            /**匹配多重路径

        //添加shiro内置过滤器
        /*
        *
        * enum DefaultFilter {
            anon(AnonymousFilter.class),
            authc(FormAuthenticationFilter.class),
            authcBasic(BasicHttpAuthenticationFilter.class),
            logout(LogoutFilter.class),
            noSessionCreation(NoSessionCreationFilter.class),
            perms(PermissionsAuthorizationFilter.class),
            port(PortFilter.class),
            rest(HttpMethodPermissionFilter.class),
            roles(RolesAuthorizationFilter.class),
            ssl(SslFilter.class),
            user(UserFilter.class);
        }
        *
        *
        * */
        //之所以用linkedHashMap是i因为有顺序
        Map<String,String> filterMap = new LinkedHashMap<>();
        filterMap.put("/login", "anon");
        filterMap.put("/page/toLogin", "anon");
        filterMap.put("/page/unAuth", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/test/*", "authc");
        filterMap.put("/test", "perms[user:list]");//可以用注解完成，当然注意若是有transaction注解那么夹在controller层好
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        //登录url
        shiroFilterFactoryBean.setLoginUrl("/page/toLogin");
        //未登录跳转的url
        shiroFilterFactoryBean.setUnauthorizedUrl("/page/unAuth");
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);


        return shiroFilterFactoryBean;
    }
}
