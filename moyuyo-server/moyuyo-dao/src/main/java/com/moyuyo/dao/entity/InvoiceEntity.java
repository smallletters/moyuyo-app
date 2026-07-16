package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("mo_invoice")
public class InvoiceEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long userId;

  private Long orderId;

  private String title;

  private String taxId;

  private String email;

  private String type;

  private BigDecimal amount;

  private String status;

  private String fileUrl;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
