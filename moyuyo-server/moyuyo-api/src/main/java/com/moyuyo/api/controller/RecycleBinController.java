package com.moyuyo.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moyuyo.common.Result;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.OrderEntity;
import com.moyuyo.service.RecycleBinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "订单回收站")
@RestController
@RequestMapping("/api/v1/recycle-bin")
@RequiredArgsConstructor
public class RecycleBinController {

  private final RecycleBinService recycleBinService;

  @Operation(summary = "已删除订单列表")
  @GetMapping
  public Result<IPage<OrderEntity>> list(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    return Result.success(recycleBinService.listDeletedOrders(UserContextHolder.getUserId(), page, size));
  }

  @Operation(summary = "恢复订单")
  @PostMapping("/{orderId}/restore")
  public Result<Void> restore(@PathVariable Long orderId) {
    recycleBinService.restoreOrder(orderId, UserContextHolder.getUserId());
    return Result.success();
  }

  @Operation(summary = "永久删除订单")
  @DeleteMapping("/{orderId}")
  public Result<Void> permanentlyDelete(@PathVariable Long orderId) {
    recycleBinService.permanentlyDelete(orderId, UserContextHolder.getUserId());
    return Result.success();
  }

  @Operation(summary = "清空回收站")
  @DeleteMapping("/clear")
  public Result<Void> clearAll() {
    recycleBinService.clearAll(UserContextHolder.getUserId());
    return Result.success();
  }
}
