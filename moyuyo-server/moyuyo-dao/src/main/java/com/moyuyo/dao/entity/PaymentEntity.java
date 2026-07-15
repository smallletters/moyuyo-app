package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("mo_payment")
public class PaymentEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long orderId;

  private String payChannel;

  private String transactionId;

  private BigDecimal amount;

  private String currency;

  private String status;

  private LocalDateTime paidAt;

  private String rawResponse;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
