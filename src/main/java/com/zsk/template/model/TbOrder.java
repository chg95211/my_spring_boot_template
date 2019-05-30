package com.zsk.template.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.zsk.template.config.date.CustomDateDeserializer;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-15 23:36
 **/
@Entity
@Table(name = "tb_order")
public class TbOrder
{
    @Id
    private String orderId;
    private String payment;
    private Integer paymentType;
    private String postFee;
    private Integer status;
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private LocalDateTime createTime;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime paymentTime;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime consignTime;
    @JsonDeserialize(using = CustomDateDeserializer.class)

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime endTime;
    @JsonDeserialize(using = CustomDateDeserializer.class)

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime closeTime;
    private String shippingName;
    private String shippingCode;
    private Long userId;
    private String buyerMessage;
    private String buyerNick;
    private Integer buyerRate;

}
