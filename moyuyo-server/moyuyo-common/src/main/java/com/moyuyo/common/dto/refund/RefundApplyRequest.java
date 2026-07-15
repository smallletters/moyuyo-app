package com.moyuyo.common.dto.refund;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "退款申请请求")
public class RefundApplyRequest {

    @NotNull(message = "订单ID不能为空")
    @Schema(description = "订单ID", example = "1")
    private Long orderId;

    @NotBlank(message = "退款类型不能为空")
    @Schema(description = "退款类型: FULL / PARTIAL", example = "FULL")
    private String type;

    @Schema(description = "退款金额(部分退款时必填)", example = "25.00")
    private BigDecimal amount;

    @NotBlank(message = "退款原因不能为空")
    @Schema(description = "退款原因", example = "Item damaged")
    private String reason;

    @Schema(description = "问题描述")
    private String description;

    @Schema(description = "图片URL列表(JSON)", example = "[\"https://...\"]")
    private String images;
}
