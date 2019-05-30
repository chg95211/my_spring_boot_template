package com.zsk.template.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-15 23:36
 **/
@Entity
@Data
@Table(name = "tb_order_item")
public class TbOrderItem
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
