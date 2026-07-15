package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.UserEntity;
import com.moyuyo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public Result<UserEntity> getCurrentUser() {
        return Result.success(authService.getCurrentUser(UserContextHolder.getUserId()));
    }

    @Operation(summary = "更新当前用户信息")
    @PutMapping("/me")
    public Result<UserEntity> updateCurrentUser(@RequestBody UserEntity update) {
        return Result.success(authService.updateCurrentUser(UserContextHolder.getUserId(), update));
    }
}
