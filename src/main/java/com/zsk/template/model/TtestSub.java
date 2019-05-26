package com.zsk.template.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;


/**
 * @description:
 * @author: zsk
 * @create: 2019-05-25 09:51
 **/
@Entity
@Data
@Table(name = "ttest_sub", schema = "public", catalog = "test")
public class TtestSub
{
    @Id
    private Integer id;
    private Integer ttestId;
    private String name;

    @Transient
    private Ttest ttest;
}
