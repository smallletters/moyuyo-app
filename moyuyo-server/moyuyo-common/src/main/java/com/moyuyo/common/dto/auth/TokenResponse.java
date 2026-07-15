package com.moyuyo.common.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Token 响应")
public class TokenResponse {

    @Schema(description = "访问令牌（JWT）", example = "eyJhbGciOiJIUzI1NiIs...")
    private String accessToken;

    @Schema(description = "刷新令牌", example = "550e8400-e29b-41d4-a716-446655440000")
    private String refreshToken;

    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType;

    @Schema(description = "过期时间（秒）", example = "7200")
    private long expiresIn;
}
