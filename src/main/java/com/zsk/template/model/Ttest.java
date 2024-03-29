package com.zsk.template.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.zsk.template.constant.TtestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-25 09:51
 **/
//@Document(indexName = "ttest",type = "ttest",shards = 1,replicas = 1,createIndex = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ttest")
public class Ttest implements Serializable
{

    private static final long serialVersionUID = -4430640845955619171L;
    @Id
//    @org.springframework.data.annotation.Id
    private Integer id;
    @Column(name = "status")
//    @Field(type = FieldType.String,analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private TtestStatus status;

    @Transient
    private String subs;

    @Transient
//    @Field(type=FieldType.Nested,includeInParent = true)
    private List<TtestSub> ttestSubs;
}
