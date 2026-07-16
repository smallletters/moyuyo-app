package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.GiftCardEntity;
import com.moyuyo.dao.entity.GiftCardTransactionEntity;
import com.moyuyo.service.GiftCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "礼品卡")
@RestController
@RequestMapping("/api/v1/gift-cards")
@RequiredArgsConstructor
public class GiftCardController {

  private final GiftCardService giftCardService;

  @Operation(summary = "礼品卡列表")
  @GetMapping
  public Result<List<GiftCardEntity>> list() {
    return Result.success(giftCardService.listByUserId(UserContextHolder.getUserId()));
  }

  @Operation(summary = "绑定礼品卡")
  @PostMapping("/bind")
  public Result<GiftCardEntity> bind(@RequestParam String cardNo, @RequestParam String pin) {
    return Result.success(giftCardService.bind(UserContextHolder.getUserId(), cardNo, pin));
  }

  @Operation(summary = "购买礼品卡")
  @PostMapping("/purchase")
  public Result<List<GiftCardEntity>> purchase(@RequestParam Integer amount, @RequestParam(defaultValue = "1") Integer quantity) {
    return Result.success(giftCardService.purchase(UserContextHolder.getUserId(), amount, quantity));
  }

  @Operation(summary = "交易记录")
  @GetMapping("/{id}/transactions")
  public Result<List<GiftCardTransactionEntity>> getTransactions(@PathVariable Long id) {
    return Result.success(giftCardService.getTransactions(id, UserContextHolder.getUserId()));
  }
}
