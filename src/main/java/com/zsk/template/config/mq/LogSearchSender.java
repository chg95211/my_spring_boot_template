package com.zsk.template.config.mq;

import com.zsk.template.model.SearchLog;
import com.zsk.template.model.TaoMiaosha;
import com.zsk.template.model.TbExport;
import com.zsk.template.model.TbOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @description:
 * @author: zsk
 * @create: 2018-10-02 11:41
 **/
@Component
@Slf4j
public class LogSearchSender
{
    @Autowired
    private RabbitTemplate amqpTemplate;

    public void sendLogSearchLogMsg(SearchLog searchLog)
    {
        log.info("[sendLogSearchLogMsg] Save search log to es : {}", searchLog);
        this.amqpTemplate.convertAndSend(RabbitMqConfig.SEARCH_LOG_DIRECT_EXCHANGE, RabbitMqConfig.SEARCH_LOG_ROUTING_KEY, searchLog);
    }

    public void sendMiaoshaOrder(TaoMiaosha order)
    {
        log.info("[sendMiaoshaOrder] create order msg : {}", order);
        this.amqpTemplate.convertAndSend(RabbitMqConfig.MIAOSHA_ORDER_DIRECT_EXCHANGE, RabbitMqConfig.MIAOSHA_ORDER_ROUTING_KEY, order);
    }

    public void sendExport(TbExport export)
    {
        log.info("[sendExport] create export msg : {}", export);
        this.amqpTemplate.convertAndSend(RabbitMqConfig.EXPORT_QUEUE,  export);
//        this.amqpTemplate.setConfirmCallback();
//        this.amqpTemplate.setReturnCallback();
    }
}
