package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_member_prime")
public class MemberPrimeEntity {

  public enum PrimeLevel {
    MONTHLY, QUARTERLY, YEARLY
  }

  public enum Status {
    ACTIVE, EXPIRED, CANCELLED
  }

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long userId;

  private PrimeLevel primeLevel;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  private Boolean autoRenew;

  private Status status;

  @TableLogic
  private Integer deleted;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createdAt;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updatedAt;
}
