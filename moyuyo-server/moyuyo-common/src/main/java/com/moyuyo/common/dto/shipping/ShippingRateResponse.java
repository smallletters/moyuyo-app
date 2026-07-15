package com.moyuyo.common.dto.shipping;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Schema(description = "运费估算响应")
public class ShippingRateResponse {

    @Schema(description = "快递公司", example = "UPS")
    private String carrier;

    @Schema(description = "配送方式", example = "Standard")
    private String method;

    @Schema(description = "运费", example = "5.99")
    private BigDecimal rate;

    @Schema(description = "预计送达天数", example = "3-5")
    private String estimatedDays;

    @Schema(description = "是否免费", example = "false")
    private boolean free;
}
