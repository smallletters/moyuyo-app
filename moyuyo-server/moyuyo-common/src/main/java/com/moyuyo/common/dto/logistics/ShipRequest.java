package com.moyuyo.common.dto.logistics;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "发货请求")
public class ShipRequest {

    @NotNull(message = "订单ID不能为空")
    @Schema(description = "订单ID", example = "1")
    private Long orderId;

    @NotBlank(message = "快递公司不能为空")
    @Schema(description = "快递公司", example = "UPS")
    private String carrier;

    @NotBlank(message = "运单号不能为空")
    @Schema(description = "运单号", example = "1Z999AA10123456784")
    private String trackingNumber;
}
