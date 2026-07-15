package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_logistics")
public class LogisticsEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long orderId;

  private String carrier;

  private String trackingNumber;

  private String traces;

  private LocalDateTime shippedAt;

  private LocalDateTime receivedAt;
}
