package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.dao.entity.LiveRoomEntity;
import com.moyuyo.dao.entity.LiveRoomProductEntity;
import com.moyuyo.service.LiveRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "直播间")
@RestController
@RequestMapping("/api/v1/live-rooms")
@RequiredArgsConstructor
public class LiveRoomController {

  private final LiveRoomService liveRoomService;

  @Operation(summary = "直播间列表")
  @GetMapping
  public Result<List<LiveRoomEntity>> list() {
    return Result.success(liveRoomService.list());
  }

  @Operation(summary = "直播间详情")
  @GetMapping("/{id}")
  public Result<LiveRoomEntity> getById(@PathVariable Long id) {
    return Result.success(liveRoomService.getDetail(id));
  }

  @Operation(summary = "直播商品列表")
  @GetMapping("/{id}/products")
  public Result<List<LiveRoomProductEntity>> getProducts(@PathVariable Long id) {
    return Result.success(liveRoomService.getProducts(id));
  }
}
