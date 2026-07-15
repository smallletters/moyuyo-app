package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("mo_product_sku")
public class ProductSkuEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long productId;

    private String skuCode;

    private Long wooVariationId;

    private String spec;

    private BigDecimal price;

    private Integer stock;

    private Integer sales;
}
