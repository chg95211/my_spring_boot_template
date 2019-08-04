package com.zsk.template.config.mq;

import com.rabbitmq.client.Channel;
import com.zsk.template.model.SearchLog;
import com.zsk.template.model.TaoMiaosha;
import com.zsk.template.model.TbExport;
import com.zsk.template.service.ExportService;
import com.zsk.template.service.MiaoShaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @description:
 * @author: zsk
 * @create: 2018-10-02 13:51
 **/
@Slf4j
@Component
public class LogSearchReceiver
{
    @Autowired
    private MiaoShaService miaoShaService;

    @Autowired
    private ExportService exportService;

    @RabbitListener(queues = RabbitMqConfig.SEARCH_LOG_QUEUE)
    @RabbitHandler
    public void receiveLogSearchLogMsg(@Payload SearchLog searchLog,
                               @Header(AmqpHeaders.CHANNEL) Channel channel,
                               @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException
    {
        log.info("[receiveLogSearchLogMsg] Save search log to es : {}", searchLog);
        try
        {
            channel.basicAck(tag, false);//手动确认,防止消费者挂了消息丢失
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @RabbitListener(queues = RabbitMqConfig.MIAOSHA_ORDER_QUEUE)
    @RabbitHandler
    public void receiveMiaoshaOrder(@Payload TaoMiaosha order,
                                       @Header(AmqpHeaders.CHANNEL) Channel channel,
                                       @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException
    {
        log.info("[receiveMiaoshaOrder] create orde msg : {}", order);
        try
        {
            miaoShaService.doMiaosha(order);
            channel.basicAck(tag, false);//手动确认,防止消费者挂了消息丢失
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RabbitMqConfig.EXPORT_QUEUE)
    @RabbitHandler
    public void receiveExport(@Payload TbExport export,
                                    @Header(AmqpHeaders.CHANNEL) Channel channel,
                                    @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException
    {
        log.info("[receiveExport] export msg : {}", export);
        try
        {
            exportService.doExport(export);
            channel.basicAck(tag, false);//手动确认,防止消费者挂了消息丢失
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
