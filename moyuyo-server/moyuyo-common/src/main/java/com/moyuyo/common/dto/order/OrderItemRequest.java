package com.moyuyo.common.dto.order;

import lombok.Data;

@Data
public class OrderItemRequest {

  private Long skuId;

  private Long productId;

  private Integer quantity;
}
