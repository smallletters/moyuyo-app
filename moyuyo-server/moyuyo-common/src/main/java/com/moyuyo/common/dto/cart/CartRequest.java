package com.moyuyo.common.dto.cart;

import lombok.Data;

@Data
public class CartRequest {

    private Long skuId;

    private Integer quantity;

    private Boolean selected;
}
