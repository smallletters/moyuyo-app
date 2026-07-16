package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.BargainEntity;
import com.moyuyo.dao.entity.BargainHelpEntity;
import com.moyuyo.service.BargainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "砍价活动")
@RestController
@RequestMapping("/api/v1/bargains")
@RequiredArgsConstructor
public class BargainController {

  private final BargainService bargainService;

  @Operation(summary = "砍价活动列表")
  @GetMapping
  public Result<List<BargainEntity>> list() {
    return Result.success(bargainService.list());
  }

  @Operation(summary = "砍价活动详情")
  @GetMapping("/{id}")
  public Result<BargainEntity> getById(@PathVariable Long id) {
    return Result.success(bargainService.getDetail(id));
  }

  @Operation(summary = "参与砍价")
  @PostMapping("/{id}/help")
  public Result<BargainHelpEntity> help(@PathVariable Long id) {
    return Result.success(bargainService.help(id, UserContextHolder.getUserId()));
  }

  @Operation(summary = "砍价助力列表")
  @GetMapping("/{id}/helpers")
  public Result<List<BargainHelpEntity>> getHelpers(@PathVariable Long id) {
    return Result.success(bargainService.getHelpers(id));
  }
}
