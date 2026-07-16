package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.PetDiaryEntity;
import com.moyuyo.service.PetDiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "宠物日记")
@RestController
@RequestMapping("/api/v1/pets/{petId}/diaries")
@RequiredArgsConstructor
public class PetDiaryController {

  private final PetDiaryService petDiaryService;

  @Operation(summary = "日记列表")
  @GetMapping
  public Result<List<PetDiaryEntity>> list(@PathVariable Long petId) {
    return Result.success(petDiaryService.listByPetId(petId, UserContextHolder.getUserId()));
  }

  @Operation(summary = "创建日记")
  @PostMapping
  public Result<PetDiaryEntity> create(@PathVariable Long petId, @RequestBody PetDiaryEntity entity) {
    return Result.success(petDiaryService.create(petId, UserContextHolder.getUserId(), entity));
  }

  @Operation(summary = "日记详情")
  @GetMapping("/{id}")
  public Result<PetDiaryEntity> getById(@PathVariable Long petId, @PathVariable Long id) {
    return Result.success(petDiaryService.getDetail(id, petId, UserContextHolder.getUserId()));
  }

  @Operation(summary = "删除日记")
  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable Long petId, @PathVariable Long id) {
    petDiaryService.delete(id, petId, UserContextHolder.getUserId());
    return Result.success();
  }
}
