package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("mo_refund")
public class RefundEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long orderId;

  private String refundNo;

  private String type;

  private BigDecimal amount;

  private String reason;

  private String description;

  private String images;

  private String status;

  private Long wooRefundId;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  private LocalDateTime completeTime;
}
