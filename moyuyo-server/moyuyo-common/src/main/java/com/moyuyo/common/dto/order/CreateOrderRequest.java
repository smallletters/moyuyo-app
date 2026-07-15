package com.moyuyo.common.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

  private List<OrderItemRequest> items;

  private Long addressId;

  private String remark;

  private String couponId;
}
