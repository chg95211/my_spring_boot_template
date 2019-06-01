package com.zsk.template.config.mq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


/**
 * @description:
 * @author: zsk
 * @create: 2018-10-02 11:36
 **/
@Configuration
public class RabbitMqConfig
{
/*
* 简单队列：一个生产者一个消费者        |----------------默认轮询
* 工作队列：一个生产者多个消费者（一个消息同时发给一个消费者）   -----|
*                                  |-------------能者多劳 设置prefectchCount和手动确认
* 发布订阅者模式：一个生产者多个消费者（一个消息同时发给多个消费者） 多了交换机
* 路由模式：把交换机和队列用routing key绑定
* 主题模式：routing key有了正则表达式 #匹配一个或多个 *匹配一个
* */
    public static final String SEARCH_LOG_QUEUE = "search-log-queue";
    public static final String SEARCH_LOG_DIRECT_EXCHANGE = "search-log-direct-exchange";
    public static final String SEARCH_LOG_ROUTING_KEY = "search-log-routing-key";

    public static final String MIAOSHA_ORDER_QUEUE = "miaosha-order-queue";
    public static final String MIAOSHA_ORDER_DIRECT_EXCHANGE = "miaosha-order-direct-exchange";
    public static final String MIAOSHA_ORDER_ROUTING_KEY = "miaosha-order-routing-key";

    //定义一个queue
    @Bean
    Queue queue2()
    {
        String name = MIAOSHA_ORDER_QUEUE;
        boolean durable = true; //持久化,防止rabbitmq挂了消息丢失
        boolean exclusive = false; //非declarer's connection 也可以使用
        boolean autoDelete = false; //不自动删除
        return new Queue(name, durable, exclusive, autoDelete);
    }

    //定义一个direct交换机, routing key 完全匹配
    @Bean("directExchange2")
    DirectExchange directExchange2()
    {
        String name = MIAOSHA_ORDER_DIRECT_EXCHANGE;
        boolean durable = true;
        boolean autoDelete = false;
        Map<String, Object> arguments = null;
        return new DirectExchange(name, durable, autoDelete, arguments);
    }



    //绑定交换机和队列:完全匹配
    @Bean("directBinding2")
    Binding directBinding2()
    {
        return BindingBuilder.bind(queue2()).to(directExchange2()).with(MIAOSHA_ORDER_ROUTING_KEY);
    }

    //定义一个queue
    @Bean("queue")
    Queue queue()
    {
        String name = SEARCH_LOG_QUEUE;
        boolean durable = true; //持久化,防止rabbitmq挂了消息丢失
        boolean exclusive = false; //非declarer's connection 也可以使用
        boolean autoDelete = false; //不自动删除
        return new Queue(name, durable, exclusive, autoDelete);
    }

    //定义一个direct交换机, routing key 完全匹配
    @Bean("directExchange")
    DirectExchange directExchange()
    {
        String name = SEARCH_LOG_DIRECT_EXCHANGE;
        boolean durable = true;
        boolean autoDelete = false;
        Map<String, Object> arguments = null;
        return new DirectExchange(name, durable, autoDelete, arguments);
    }



    //绑定交换机和队列:完全匹配
    @Bean
    Binding directBinding()
    {
        return BindingBuilder.bind(queue()).to(directExchange()).with(SEARCH_LOG_ROUTING_KEY);
    }


    //解决:无限循环org.springframework.amqp.AmqpException: No method found for class [B
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());//更换sender的消息转换器为Jackson2JsonMessageConverter

        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());//更换receiver的消息转换器为Jackson2JsonMessageConverter
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);//手动确认,防止消费者挂了消息丢失
        // factory.setPrefetchCount(1);//接收的最大消息数量,消费者一次性取多少条消息

        return factory;
    }

}
