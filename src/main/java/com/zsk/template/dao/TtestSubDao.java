package com.zsk.template.dao;

import com.zsk.template.model.Ttest;
import com.zsk.template.model.TtestSub;
import com.zsk.template.util.MyMapper;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-11 17:21
 **/
@Repository
public interface TtestSubDao extends MyMapper<TtestSub>
{
    TtestSub getSubWithTest(Integer id);
}
