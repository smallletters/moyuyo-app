package com.moyuyo.common.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "购物车结算请求")
public class CartCheckoutRequest {

    @Schema(description = "收货地址ID", example = "1")
    private Long addressId;

    @Schema(description = "订单备注", example = "Please leave at door")
    private String remark;

    @Schema(description = "优惠券ID", example = "COUPON001")
    private String couponId;
}
