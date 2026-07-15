package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("mo_coupon")
public class CouponEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String code;

    private String name;

    private String description;

    private String type;

    private BigDecimal discountValue;

    private BigDecimal minOrderAmount;

    private BigDecimal maxDiscountAmount;

    private Integer totalCount;

    private Integer claimedCount;

    private Integer usedCount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean active;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
