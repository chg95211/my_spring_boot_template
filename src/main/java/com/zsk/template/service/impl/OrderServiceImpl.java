package com.zsk.template.service.impl;

import com.zsk.template.dao.TbOrderDao;
import com.zsk.template.dao.TbOrderItemDao;
import com.zsk.template.model.TbOrder;
import com.zsk.template.model.TbOrderItem;
import com.zsk.template.model.TbUser;
import com.zsk.template.service.OrderService;
import com.zsk.template.util.SnowflakeId;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-31 23:30
 **/
@Service
@Slf4j
public class OrderServiceImpl implements OrderService
{
    @Autowired
    private TbOrderDao orderDao;

    @Autowired
    private SnowflakeId snowflakeId;

    @Autowired
    private TbOrderItemDao orderItemDao;

    @Override
    public void createOrder(TbOrder order)
    {
        LocalDateTime now = LocalDateTime.now();
        order.setOrderId(String.valueOf(snowflakeId.nextId()));
        order.setCreateTime(now);
        order.setUpdateTime(now);
        //        Object principal = SecurityUtils.getSubject().getPrincipal();
        //        if (principal instanceof TbUser)
        //        {
        //            TbUser user = (TbUser) principal;
        //            order.setUserId(user.getId());
        //        }
        orderDao.insertSelective(order);
    }

    @Override
    public void createOrderItem(TbOrderItem orderItem)
    {
        String id = String.valueOf(snowflakeId.nextId());
        LocalDateTime now = LocalDateTime.now();
        orderItem.setOrderId(String.valueOf(snowflakeId.nextId()));
        orderItem.setId(id);
        this.orderItemDao.insertSelective(orderItem);
    }


}
