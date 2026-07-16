package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.PetOutfitEntity;
import com.moyuyo.service.PetDresserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "宠物装扮")
@RestController
@RequestMapping("/api/v1/pets/{petId}/dresser")
@RequiredArgsConstructor
public class PetDresserController {

  private final PetDresserService petDresserService;

  @Operation(summary = "装扮列表")
  @GetMapping
  public Result<List<PetOutfitEntity>> list(@PathVariable Long petId) {
    return Result.success(petDresserService.listByPetId(petId, UserContextHolder.getUserId()));
  }

  @Operation(summary = "装备装扮")
  @PostMapping("/{id}/equip")
  public Result<Void> equip(@PathVariable Long petId, @PathVariable Long id) {
    petDresserService.equip(id, petId, UserContextHolder.getUserId());
    return Result.success();
  }

  @Operation(summary = "删除装扮")
  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable Long petId, @PathVariable Long id) {
    petDresserService.delete(id, petId, UserContextHolder.getUserId());
    return Result.success();
  }
}
