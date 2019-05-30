package com.zsk.template.dao;

import com.zsk.template.model.Ttest;
import com.zsk.template.util.MyMapper;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-11 17:21
 **/
@Repository
public interface TtestDao extends MyMapper<Ttest>
{
    Ttest getTtestWithSubs(Integer id);
    Ttest getTtestWithSubs2(Integer id);

    @Insert("insert into ttest (id, status) values (#{id}, #{status})")
    int insertRecode(Ttest tTest);

}
