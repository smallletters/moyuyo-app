package com.moyuyo.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moyuyo.common.Result;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.AfterSalesEntity;
import com.moyuyo.service.AfterSalesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "售后管理")
@RestController
@RequestMapping("/api/v1/after-sales")
@RequiredArgsConstructor
public class AfterSalesController {

  private final AfterSalesService afterSalesService;

  @Operation(summary = "售后列表")
  @GetMapping
  public Result<IPage<AfterSalesEntity>> list(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String status) {
    return Result.success(afterSalesService.listAfterSales(UserContextHolder.getUserId(), page, size, status));
  }

  @Operation(summary = "售后详情")
  @GetMapping("/{id}")
  public Result<AfterSalesEntity> getDetail(@PathVariable Long id) {
    return Result.success(afterSalesService.getAfterSalesDetail(id, UserContextHolder.getUserId()));
  }

  @Operation(summary = "创建售后申请")
  @PostMapping
  public Result<AfterSalesEntity> create(@RequestParam Long orderId,
                                         @RequestParam(required = false) Long orderItemId,
                                         @RequestParam String type,
                                         @RequestParam String reason,
                                         @RequestParam(required = false) BigDecimal amount,
                                         @RequestParam(required = false) String description,
                                         @RequestParam(required = false) String images) {
    return Result.success(afterSalesService.createAfterSales(
        UserContextHolder.getUserId(), orderId, orderItemId, type, reason, amount, description, images));
  }
}
