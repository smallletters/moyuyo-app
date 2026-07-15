package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("mo_product_image")
public class ProductImageEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long productId;

    private String url;

    private Integer sort;
}
