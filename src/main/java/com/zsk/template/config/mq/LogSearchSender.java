package com.zsk.template.config.mq;

import com.zsk.template.model.SearchLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
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
    private AmqpTemplate amqpTemplate;

    public void sendLogSearchLogMsg(SearchLog searchLog)
    {
        log.info("[sendLogSearchLogMsg] Save search log to es : {}", searchLog);
        this.amqpTemplate.convertAndSend(RabbitMqConfig.SEARCH_LOG_DIRECT_EXCHANGE, RabbitMqConfig.SEARCH_LOG_ROUTING_KEY, searchLog);
    }
}
