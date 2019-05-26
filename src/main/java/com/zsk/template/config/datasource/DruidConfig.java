package com.zsk.template.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-11 17:06
 **/
@Configuration
public class DruidConfig
{
    //开启druid web监控
//    localhost:8000/druid
    @Bean
    public ServletRegistrationBean druidServlet()
    {
        ServletRegistrationBean servletRegistrationBean =
                new ServletRegistrationBean(new StatViewServlet(), "/druid/*");

        servletRegistrationBean.addInitParameter("allow", "127.0.0.1,192.168.199.10");
        servletRegistrationBean.addInitParameter("deny", "192.168.199.100");
        servletRegistrationBean.addInitParameter("loginUsername", "zsk");
        servletRegistrationBean.addInitParameter("loginPassword", "zskroot");
        servletRegistrationBean.addInitParameter("resetEnable", "false");

        return servletRegistrationBean;
    }


    //配置拦截url
    @Bean
    public FilterRegistrationBean filterRegistrationBean()
    {
        FilterRegistrationBean filterRegistrationBean =
                new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.css,/druid/*");
        return filterRegistrationBean;
    }

    //配置datasource
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource()
    {
        return new DruidDataSource();
    }
}
