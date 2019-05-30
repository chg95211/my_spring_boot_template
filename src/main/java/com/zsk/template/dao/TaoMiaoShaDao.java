package com.zsk.template.dao;

import com.zsk.template.model.TaoMiaosha;
import com.zsk.template.util.MyMapper;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-30 22:54
 **/
@Repository
public interface TaoMiaoShaDao extends MyMapper<TaoMiaosha>
{
    TaoMiaosha getByGoodsId(TaoMiaosha miaosha);

    int miaoSha(TaoMiaosha miaoshaDb);
}
