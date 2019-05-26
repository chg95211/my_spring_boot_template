package com.zsk.template.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
* @Description: tk mybatis通用mapper。注意此时SpringApplication上的@MapperScan注解必须用tkmybatis的，否则报错
* @Param:
* @return:
*/
//这个类不能被扫描到
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T>
{
}
