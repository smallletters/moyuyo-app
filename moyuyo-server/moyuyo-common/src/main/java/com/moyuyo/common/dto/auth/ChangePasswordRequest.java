package com.moyuyo.common.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "修改密码请求")
public class ChangePasswordRequest {

    @NotBlank(message = "旧密码不能为空")
    @Schema(description = "旧密码", example = "MyOldPass123")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, max = 64, message = "密码长度需8-64位")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,64}$",
             message = "密码需包含大小写字母和数字")
    @Schema(description = "新密码", example = "MyNewPass456")
    private String newPassword;
}
