package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_product_review")
public class ProductReviewEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long productId;

    private Long userId;

    private Long orderId;

    private Long orderItemId;

    private Integer rating;

    private String content;

    private String tags;

    private String images;

    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
