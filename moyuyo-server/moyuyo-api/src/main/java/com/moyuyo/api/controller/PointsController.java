package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.PointsLogEntity;
import com.moyuyo.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "积分管理")
@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointsController {

  private final MemberService memberService;

  @Operation(summary = "获取积分流水")
  @GetMapping("/log")
  public Result<com.baomidou.mybatisplus.extension.plugins.pagination.Page<PointsLogEntity>> getPointsLog(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int size) {
    return Result.success(memberService.getPointsLog(UserContextHolder.getUserId(), page, size));
  }

  @Operation(summary = "获取积分余额")
  @GetMapping("/balance")
  public Result<Integer> getBalance() {
    return Result.success(memberService.getPointsBalance(UserContextHolder.getUserId()));
  }

  @Operation(summary = "签到得积分")
  @PostMapping("/checkin")
  public Result<Void> checkin() {
    memberService.addPoints(UserContextHolder.getUserId(), 5, "CHECKIN", null, "Daily checkin bonus");
    return Result.success();
  }
}
