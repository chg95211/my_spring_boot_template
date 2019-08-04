package com.zsk.template.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-15 23:36
 **/
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "tb_order_item")
public class TbOrderItem implements Serializable
{
    @Id
    private String id;
    private String itemId;
    private String orderId;
    private Integer num;
    private String title;
    private Long price;
    private Long totalFee;
    private String picPath;
}
