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
import java.util.Date;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-30 22:50
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tao_miaosha", schema = "tao", catalog = "")
public class TaoMiaosha implements Serializable
{
    @Id
    private Long id;
    private Long goodsId;
    private Integer price;
    private Integer stockCount;
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date startDate;
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date endDate;
    @Transient
    private Long userId;
}
