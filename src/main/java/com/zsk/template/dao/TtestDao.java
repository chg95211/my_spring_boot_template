package com.zsk.template.dao;

import com.zsk.template.model.Ttest;
import com.zsk.template.util.MyMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-11 17:21
 **/
//@CacheNamespace
/*
* eviction 缓存策略
* flushInterval 缓存刷新间隔
* readOnly 是否只读
* size 缓存空间大小 map.size
* implementation 二级缓存实现类
* */
@Repository
public interface TtestDao extends MyMapper<Ttest>
{
//    #{}占位符？
//    ${}是拼串
    Ttest getTtestWithSubs(Integer id);
    Ttest getTtestWithSubs2(Integer id);

    @Insert("insert into ttest (id, status) values (#{id}, #{status})")
    int insertRecode(Ttest tTest);

    int insert2(Ttest ttest);

    @Select("select * from ttest where id = #{id}")
    Ttest getTtestById(int id);

    @Select("select * from ttest where status=#{status}")
    Ttest getTtestByStatus(String status);


    Ttest selectByStats(String status);


    Ttest selectByIdOrStatus(Ttest test);

    void updateOne(Ttest ttest);

    void batchInsert(List<Ttest> ttests);
}
