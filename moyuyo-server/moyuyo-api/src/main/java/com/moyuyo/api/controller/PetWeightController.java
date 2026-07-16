package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.PetWeightEntity;
import com.moyuyo.service.PetWeightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "宠物体重")
@RestController
@RequestMapping("/api/v1/pets/{petId}/weights")
@RequiredArgsConstructor
public class PetWeightController {

  private final PetWeightService petWeightService;

  @Operation(summary = "体重列表")
  @GetMapping
  public Result<List<PetWeightEntity>> list(@PathVariable Long petId) {
    return Result.success(petWeightService.listByPetId(petId, UserContextHolder.getUserId()));
  }

  @Operation(summary = "添加体重")
  @PostMapping
  public Result<PetWeightEntity> create(@PathVariable Long petId, @RequestBody PetWeightEntity entity) {
    return Result.success(petWeightService.create(petId, UserContextHolder.getUserId(), entity));
  }

  @Operation(summary = "体重图表数据")
  @GetMapping("/chart")
  public Result<List<PetWeightEntity>> chart(@PathVariable Long petId) {
    return Result.success(petWeightService.getChart(petId, UserContextHolder.getUserId()));
  }
}
