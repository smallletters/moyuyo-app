package com.moyuyo.common.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "2FA 验证请求")
public class TwoFactorRequest {

    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码为6位数字")
    @Schema(description = "短信验证码", example = "123456")
    private String code;
}
