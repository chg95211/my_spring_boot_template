package com.zsk.template.service;

import com.zsk.template.model.TaoMiaosha;

import java.util.List;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-30 22:53
 **/
public interface MiaoShaService
{
    Object miaosha(TaoMiaosha miaosha);

    List<TaoMiaosha> list();

    void doMiaosha(TaoMiaosha order);
}
