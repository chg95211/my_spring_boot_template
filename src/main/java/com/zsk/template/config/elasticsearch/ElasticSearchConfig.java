package com.zsk.template.config.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.shield.ShieldPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-28 20:58
 **/
@Configuration
public class ElasticSearchConfig
{
    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(Client client)
    {
        return new ElasticsearchTemplate(client);
    }

    //es transport client 增加shield配置
    @Bean
    public Client client() throws UnknownHostException
    {
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "myes")
                .put("shield.user","es_admin:zskroot")
                .build();

        Client client = TransportClient.builder()
                .addPlugin(ShieldPlugin.class) //shield依赖里面的类
                .settings(settings)
                .build()
                .addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9301),
                        new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9302));
        return client;
    }

}
