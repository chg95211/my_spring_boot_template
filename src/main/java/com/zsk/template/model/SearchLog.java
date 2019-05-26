package com.zsk.template.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;

/*
 * @Description:
 * @Author: ZHANGSHENGKUN938
 * @Date: 2018-09-30 15:19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchLog implements Serializable
{
    private String id;

    /*
    测试分词效果
    GET /search_log/_analyze
    {
	"field":"q",
	"text":"好像似乎"
    }
     */
    private String q; //搜索的关键词

    private Date date; //搜索日期

    private String um; //搜索的用户um
}
