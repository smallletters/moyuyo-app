package com.moyuyo.common.dto.address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "地址验证响应")
public class AddressValidateResponse {

    @Schema(description = "是否可配送", example = "true")
    private boolean shippable;

    @Schema(description = "提示信息", example = "We currently ship to this region")
    private String message;
}
