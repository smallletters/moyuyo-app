package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.dao.entity.BundleDealEntity;
import com.moyuyo.dao.entity.BundleDealItemEntity;
import com.moyuyo.service.BundleDealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "套餐活动")
@RestController
@RequestMapping("/api/v1/bundle-deals")
@RequiredArgsConstructor
public class BundleDealController {

  private final BundleDealService bundleDealService;

  @Operation(summary = "套餐活动列表")
  @GetMapping
  public Result<List<BundleDealEntity>> list() {
    return Result.success(bundleDealService.list());
  }

  @Operation(summary = "套餐活动详情")
  @GetMapping("/{id}")
  public Result<BundleDealEntity> getById(@PathVariable Long id) {
    return Result.success(bundleDealService.getDetail(id));
  }

  @Operation(summary = "套餐商品列表")
  @GetMapping("/{id}/items")
  public Result<List<BundleDealItemEntity>> getItems(@PathVariable Long id) {
    return Result.success(bundleDealService.getItems(id));
  }
}
