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
 * @create: 2019-08-04 20:25
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_export", schema = "tao", catalog = "")
public class TbExport implements Serializable
{
    @Id
    private Long id;
    private Long userId;
    private String fileName;
    private String result;

}
