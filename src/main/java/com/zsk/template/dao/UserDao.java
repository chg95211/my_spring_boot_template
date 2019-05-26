package com.zsk.template.dao;

import com.zsk.template.model.TbUser;
import com.zsk.template.util.MyMapper;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-11 17:21
 **/
@Repository
public interface UserDao extends MyMapper<TbUser>
{

}
