package com.zsk.template.service.impl;

import com.zsk.template.config.exception.ParameterException;
import com.zsk.template.dao.TaoMiaoShaDao;
import com.zsk.template.model.TaoMiaosha;
import com.zsk.template.service.MiaoShaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-30 22:53
 **/
@Slf4j
@Service
public class MiaoShaServiceImpl implements MiaoShaService
{
    @Autowired
    private TaoMiaoShaDao miaoShaDao;

    @Override
    public Object doMiaoSha(TaoMiaosha miaosha)
    {
        TaoMiaosha miaoshaDb = this.miaoShaDao.getByGoodsId(miaosha);
        if (ObjectUtils.isEmpty(miaoshaDb))
        {
            throw new ParameterException(String.format("商品%s不存在", miaosha.getGoodsId()));
        }

        if (miaoshaDb.getStockCount() <= 0)
        {
            throw new ParameterException("商品%s已售完");
        }


        int updated = this.miaoShaDao.miaoSha(miaoshaDb);
        String msg = updated > 0 ? "秒杀成功" : "秒杀失败";
        return msg;
    }
}
