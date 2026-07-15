package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_flash_sale_order")
public class FlashSaleOrderEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long flashSaleId;

    private Long userId;

    private Long orderId;

    private Integer quantity;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
