package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("mo_wallet")
public class WalletEntity {

  public enum Status {
    ACTIVE, FROZEN, CLOSED
  }

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long userId;

  private BigDecimal balance;

  private BigDecimal totalRecharged;

  private BigDecimal totalSpent;

  private Status status;

  @TableLogic
  private Integer deleted;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createdAt;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updatedAt;
}
