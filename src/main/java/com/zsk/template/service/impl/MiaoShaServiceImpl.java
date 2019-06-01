package com.zsk.template.service.impl;

import com.zsk.template.config.exception.ParameterException;
import com.zsk.template.config.interceptor.ThreadLocalUser;
import com.zsk.template.config.mq.LogSearchSender;
import com.zsk.template.constant.RedisKeyPrefix;
import com.zsk.template.dao.TaoMiaoShaDao;
import com.zsk.template.model.TaoMiaosha;
import com.zsk.template.model.TbOrder;
import com.zsk.template.model.TbOrderItem;
import com.zsk.template.model.TbUser;
import com.zsk.template.service.MiaoShaService;
import com.zsk.template.service.OrderService;
import com.zsk.template.util.JsonUtil;
import com.zsk.template.util.SnowflakeId;
import com.zsk.template.util.jedis.JedisClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-30 22:53
 **/
@Slf4j
@Service
public class MiaoShaServiceImpl implements MiaoShaService, InitializingBean
{
    @Autowired
    private TaoMiaoShaDao miaoShaDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private LogSearchSender searchSender;

    @Autowired
    private SnowflakeId snowflakeId;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public Object miaosha(TaoMiaosha miaosha)
    {
//        TaoMiaosha miaoshaDb = this.miaoShaDao.getByGoodsId(miaosha);
//        if (ObjectUtils.isEmpty(miaoshaDb))
//        {
//            throw new ParameterException(String.format("商品%s不存在", miaosha.getGoodsId()));
//        }
//
//        if (miaoshaDb.getStockCount() <= 0)
//        {
//            throw new ParameterException(String.format("商品%s已售完", miaosha.getGoodsId()));
//        }


//        int updated = this.miaoShaDao.miaoSha(miaoshaDb);

//        TbOrder order = TbOrder.builder().payment(String.valueOf(miaoshaDb.getPrice())).build();
//        orderService.createOrder(order);
//
//        String msg = updated > 0 ? "秒杀成功" : "秒杀失败";
//        return msg;


        long decr = jedisClient.decr(RedisKeyPrefix.MiaoShaItem.prefix() + miaosha.getId());
        //mq
        if(decr >= 0)
        {
            searchSender.sendMiaoshaOrder(TaoMiaosha.builder().id(miaosha.getId()).userId(ThreadLocalUser.getUser().getId()).build());
        }
        String msg = decr >= 0 ? "秒杀成功" : "秒杀失败";
        return msg;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        List<TaoMiaosha> list = this.list();
        if (CollectionUtils.isNotEmpty(list))
        {
            for (TaoMiaosha item : list)
            {
                jedisClient.set(RedisKeyPrefix.MiaoShaItem.prefix() + item.getId(), String.valueOf(item.getStockCount()));

            }
        }
    }

    @Override
    public List<TaoMiaosha> list()
    {
        return this.miaoShaDao.selectAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public void doMiaosha(TaoMiaosha order)
    {
        //减库存
        TaoMiaosha miaosha = this.miaoShaDao.selectByPrimaryKey(order.getId());
        this.miaoShaDao.miaoSha(miaosha);
        //创建订单和订单item
        String orderId = String.valueOf(snowflakeId.nextId());
        orderService.createOrder(TbOrder.builder().orderId(orderId).payment(String.valueOf(miaosha.getPrice())).userId(order.getUserId()).build());
        orderService.createOrderItem(TbOrderItem.builder().orderId(orderId).itemId(String.valueOf(miaosha.getGoodsId())).num(1).price(Long.valueOf(miaosha.getPrice())).build());
    }
}
