package com.moyuyo.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.common.Result;
import com.moyuyo.common.dto.review.CreateReviewRequest;
import com.moyuyo.common.dto.review.ReviewVO;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商品评价")
@RestController
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  @Operation(summary = "商品评价列表（公开）")
  @GetMapping("/api/v1/products/{productId}/reviews")
  public Result<Page<ReviewVO>> getProductReviews(
      @PathVariable Long productId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int size) {
    return Result.success(reviewService.getProductReviews(productId, page, size));
  }

  @Operation(summary = "创建评价")
  @PostMapping("/api/v1/reviews")
  public Result<ReviewVO> createReview(@Valid @RequestBody CreateReviewRequest request) {
    return Result.success(reviewService.createReview(UserContextHolder.getUserId(), request));
  }

  @Operation(summary = "我的评价列表")
  @GetMapping("/api/v1/reviews/mine")
  public Result<Page<ReviewVO>> getMyReviews(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int size) {
    return Result.success(reviewService.getUserReviews(UserContextHolder.getUserId(), page, size));
  }

  @Operation(summary = "删除评价")
  @DeleteMapping("/api/v1/reviews/{id}")
  public Result<Void> deleteReview(@PathVariable Long id) {
    reviewService.deleteReview(id, UserContextHolder.getUserId());
    return Result.success();
  }
}
