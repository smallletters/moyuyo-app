package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.dao.entity.CategoryEntity;
import com.moyuyo.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "分类管理")
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @Operation(summary = "分类树")
  @GetMapping
  public Result<List<CategoryEntity>> getCategoryTree() {
    return Result.success(categoryService.getCategoryTree());
  }

  @Operation(summary = "子分类列表")
  @GetMapping("/{parentId}/children")
  public Result<List<CategoryEntity>> getCategoryByParent(
      @Parameter(description = "父分类ID") @PathVariable Long parentId) {
    return Result.success(categoryService.getCategoryByParent(parentId));
  }
}
