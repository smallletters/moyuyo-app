package com.moyuyo.common.dto.logistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "物流信息VO")
public class LogisticsVO {

    @Schema(description = "物流记录ID")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "快递公司")
    private String carrier;

    @Schema(description = "运单号")
    private String trackingNumber;

    @Schema(description = "物流轨迹")
    private List<TraceItem> traces;

    @Schema(description = "发货时间")
    private LocalDateTime shippedAt;

    @Schema(description = "签收时间")
    private LocalDateTime receivedAt;

    @Schema(description = "当前物流状态")
    private String currentStatus;

    @Data
    public static class TraceItem {
        private String time;
        private String location;
        private String desc;
        private String status;
    }
}
