package com.moyuyo.common.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Magic Link 验证请求")
public class MagicLinkVerifyRequest {

    @NotBlank(message = "令牌不能为空")
    @Schema(description = "Magic Link 令牌", example = "550e8400-e29b-41d4-a716-446655440000")
    private String token;
}
