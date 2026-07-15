package com.moyuyo.common.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "注册请求")
public class RegisterRequest {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱", example = "user@example.com")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 64, message = "密码长度需8-64位")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,64}$",
             message = "密码需包含大小写字母和数字")
    @Schema(description = "密码（需包含大小写字母和数字）", example = "MyPassword123")
    private String password;

    @NotBlank(message = "昵称不能为空")
    @Size(max = 50, message = "昵称最长50个字符")
    @Schema(description = "昵称", example = "John")
    private String nickname;

    @Schema(description = "国家 ISO 3166-1 alpha-2", example = "US")
    private String country;

    @Schema(description = "是否同意营销邮件", example = "false")
    private Boolean marketingOptIn;
}
