package com.zsk.template.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.zsk.template.config.date.CustomDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-15 23:36
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "tb_item")
public class TbItem implements Serializable
{
    @Id
    private Long id;
    private String title;
    private String sellPoint;
    private Long price;
    private Integer num;
    private String barcode;
    private String image;
    private Long cid;
    private Byte status;

//    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
//    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Timestamp created;

//    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
//    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Timestamp updated;

}
