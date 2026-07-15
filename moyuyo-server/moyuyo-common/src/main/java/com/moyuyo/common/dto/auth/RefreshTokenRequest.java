package com.moyuyo.common.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "刷新 Token 请求")
public class RefreshTokenRequest {

    @NotBlank(message = "刷新令牌不能为空")
    @Schema(description = "刷新令牌", example = "550e8400-e29b-41d4-a716-446655440000")
    private String refreshToken;
}
