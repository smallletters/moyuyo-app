package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.common.dto.favorite.FavoriteRequest;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.FavoriteEntity;
import com.moyuyo.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "收藏管理")
@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

  private final FavoriteService favoriteService;

  @Operation(summary = "收藏列表")
  @GetMapping
  public Result<List<FavoriteEntity>> getFavorites() {
    return Result.success(favoriteService.getUserFavorites(UserContextHolder.getUserId()));
  }

  @Operation(summary = "添加收藏")
  @PostMapping
  public Result<Void> addFavorite(@RequestBody FavoriteRequest request) {
    favoriteService.addFavorite(UserContextHolder.getUserId(), request.getProductId(), request.getSkuId(), request.getGroupId());
    return Result.success();
  }

  @Operation(summary = "取消收藏")
  @DeleteMapping
  public Result<Void> removeFavorite(@RequestBody FavoriteRequest request) {
    favoriteService.removeFavorite(UserContextHolder.getUserId(), request.getProductId(), request.getSkuId());
    return Result.success();
  }
}
