package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_cart")
public class CartEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private Long productId;

    private Long skuId;

    private Integer quantity;

    private Boolean checked;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
