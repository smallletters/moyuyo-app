package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_user_coupon")
public class UserCouponEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private Long couponId;

    private String status;

    private LocalDateTime usedTime;

    private Long usedOrderId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
