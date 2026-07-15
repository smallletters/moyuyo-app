package com.moyuyo.common.dto.refund;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(description = "退款VO")
public class RefundVO {

    private Long id;
    private Long orderId;
    private String refundNo;
    private String type;
    private BigDecimal amount;
    private String reason;
    private String description;
    private String images;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime completeTime;
}
