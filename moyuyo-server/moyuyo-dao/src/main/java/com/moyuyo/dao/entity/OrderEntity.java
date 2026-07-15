package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("mo_order")
public class OrderEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private String orderNo;

  private Long userId;

  private Long addressId;

  private BigDecimal goodsAmount;

  private BigDecimal freight;

  private BigDecimal taxAmount;

  private BigDecimal couponDiscount;

  private BigDecimal pointsDiscount;

  private BigDecimal payAmount;

  private String currency;

  private String couponId;

  private Integer pointsUsed;

  private String status;

  private String payChannel;

  private String payTransactionId;

  private Long wooOrderId;

  private Integer syncStatus;

  private Integer syncRetryCount;

  private LocalDateTime syncLastTime;

  private String trackingNumber;

  private String shippingCarrier;

  private String remark;

  private String senderName;

  private String senderPhone;

  private String senderAddress;

  private String receiverName;

  private String receiverPhone;

  private String receiverAddress;

  private String receiverZip;

  private String shippingMethod;

  private String deliveryNote;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  private LocalDateTime paidAt;

  private LocalDateTime cancelTime;

  private String cancelReason;

  private LocalDateTime deliverTime;

  private LocalDateTime receivedTime;

  private Integer deleteStatus;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;

  @TableField(exist = false)
  private List<OrderItemEntity> items;
}
