package com.zsk.template.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.zsk.template.config.date.CustomDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-11 17:20
 **/
//@Document(indexName = "tb_user",type = "tb_user",shards = 1,replicas = 1)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "tb_user")
public class TbUser implements Serializable
{
    @Id
    private Long id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String phone;
    private String email;
//    @JsonDeserialize(using = CustomDateDeserializer.class)
//    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
//    private LocalDateTime created;
//    @JsonDeserialize(using = CustomDateDeserializer.class)
//    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
//    private LocalDateTime updated;
    private Integer level;
    private String collection;
    private String perms;

}
