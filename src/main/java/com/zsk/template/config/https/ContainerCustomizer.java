//package com.zsk.template.config.https;
//
//import org.apache.catalina.Context;
//import org.apache.catalina.connector.Connector;
//import org.apache.tomcat.util.descriptor.web.SecurityCollection;
//import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
//import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 设置将访问80端口的请求跳转到指定的端口上去，这样用户就可以直接输入设备地址访问而不需要输入前缀和端口了
// *
// * @author 阿信sxq
// */
//@Configuration
//public class ContainerCustomizer
//{
//
//    @Value("${server.port}")
//    private Integer webPort;
//
//    /**
//     * 构建servlet容器的工厂类
//     * 将80端口跳转到{@linkplain #webPort}端口
//     *
//     * @return 内置servlet容器类的工厂实例
//     */
//    @Bean
//    public EmbeddedServletContainerFactory servletContainer()
//    {
//        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory()
//        {
//
//            @Override
//            protected void postProcessContext(Context context)
//            {
//                SecurityConstraint securityConstraint = new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//            }
//        };
//        factory.addAdditionalTomcatConnectors(createConnector());
//        return factory;
//    }
//
//    /**
//     * 创建tomcat连接器。
//     * 该连接器将会接收http的80端口的访问，并且重定向到指定的端口上去，{@linkplain #webPort}
//     *
//     * @return tomcat连接器
//     */
//    private Connector createConnector()
//    {
//        final Connector connector = new Connector();
//
//        connector.setPort(80);
//        connector.setRedirectPort(webPort);
//        return connector;
//    }
//}