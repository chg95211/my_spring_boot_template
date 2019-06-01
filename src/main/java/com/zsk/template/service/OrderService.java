package com.zsk.template.service;

import com.zsk.template.model.TbOrder;
import com.zsk.template.model.TbOrderItem;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-31 23:30
 **/
public interface OrderService
{
    void createOrder(TbOrder order);
    void createOrderItem(TbOrderItem orderItem);

}
