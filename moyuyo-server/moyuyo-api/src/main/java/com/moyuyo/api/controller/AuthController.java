package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.common.dto.auth.*;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        return Result.success(authService.register(request));
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @Operation(summary = "刷新 Token")
    @PostMapping("/refresh")
    public Result<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return Result.success(authService.refreshToken(request));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        String token = UserContextHolder.getToken();
        authService.logout(UserContextHolder.getUserId(), token);
        return Result.success();
    }

    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/email/verify")
    public Result<Void> sendEmailVerification(@Valid @RequestBody EmailVerifyRequest request) {
        authService.sendEmailVerification(request);
        return Result.success();
    }

    @Operation(summary = "确认邮箱验证码")
    @PostMapping("/email/verify-confirm")
    public Result<Void> confirmEmailVerification(@Valid @RequestBody EmailVerifyConfirmRequest request) {
        authService.confirmEmailVerification(request);
        return Result.success();
    }

    @Operation(summary = "发送密码重置邮件")
    @PostMapping("/password/forgot")
    public Result<Void> forgotPassword(@Valid @RequestBody EmailVerifyRequest request) {
        authService.sendPasswordReset(request);
        return Result.success();
    }

    @Operation(summary = "重置密码")
    @PostMapping("/password/reset")
    public Result<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return Result.success();
    }

    @Operation(summary = "修改密码（需要登录）")
    @PostMapping("/password/change")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(UserContextHolder.getUserId(), request);
        return Result.success();
    }

    @Operation(summary = "发送 Magic Link 邮件")
    @PostMapping("/magic-link/send")
    public Result<Void> sendMagicLink(@Valid @RequestBody EmailVerifyRequest request) {
        authService.sendMagicLink(request);
        return Result.success();
    }

    @Operation(summary = "验证 Magic Link 并登录")
    @PostMapping("/magic-link/verify")
    public Result<TokenResponse> verifyMagicLink(@Valid @RequestBody MagicLinkVerifyRequest request) {
        return Result.success(authService.verifyMagicLink(request.getToken()));
    }

    @Operation(summary = "发送 2FA 验证码")
    @PostMapping("/2fa/send")
    public Result<Void> sendTwoFactorCode() {
        authService.sendTwoFactorCode(UserContextHolder.getUserId());
        return Result.success();
    }

    @Operation(summary = "验证 2FA 验证码")
    @PostMapping("/2fa/verify")
    public Result<Void> verifyTwoFactorCode(@Valid @RequestBody TwoFactorRequest request) {
        authService.verifyTwoFactorCode(UserContextHolder.getUserId(), request.getCode());
        return Result.success();
    }
}
