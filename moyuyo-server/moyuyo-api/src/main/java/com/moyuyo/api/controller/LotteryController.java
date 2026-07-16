package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.LotteryEntity;
import com.moyuyo.dao.entity.LotteryRecordEntity;
import com.moyuyo.service.LotteryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "抽奖活动")
@RestController
@RequestMapping("/api/v1/lotteries")
@RequiredArgsConstructor
public class LotteryController {

  private final LotteryService lotteryService;

  @Operation(summary = "抽奖活动列表")
  @GetMapping
  public Result<List<LotteryEntity>> list() {
    return Result.success(lotteryService.list());
  }

  @Operation(summary = "抽奖")
  @PostMapping("/{id}/spin")
  public Result<LotteryRecordEntity> spin(@PathVariable Long id) {
    return Result.success(lotteryService.spin(UserContextHolder.getUserId(), id));
  }

  @Operation(summary = "抽奖历史")
  @GetMapping("/history")
  public Result<List<LotteryRecordEntity>> getHistory() {
    return Result.success(lotteryService.getHistory(UserContextHolder.getUserId()));
  }

  @Operation(summary = "抽奖统计")
  @GetMapping("/stats")
  public Result<Map<String, Object>> getStats() {
    return Result.success(lotteryService.getStats(UserContextHolder.getUserId()));
  }
}
