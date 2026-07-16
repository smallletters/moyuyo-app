package com.moyuyo.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moyuyo.common.Result;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.FeedbackEntity;
import com.moyuyo.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "意见反馈")
@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {

  private final FeedbackService feedbackService;

  @Operation(summary = "提交反馈")
  @PostMapping
  public Result<Void> submit(@RequestParam String type,
                             @RequestParam String content,
                             @RequestParam(required = false) String images,
                             @RequestParam(required = false) String contact) {
    feedbackService.submitFeedback(UserContextHolder.getUserId(), type, content, images, contact);
    return Result.success();
  }

  @Operation(summary = "我的反馈列表")
  @GetMapping
  public Result<IPage<FeedbackEntity>> list(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    return Result.success(feedbackService.listMyFeedback(UserContextHolder.getUserId(), page, size));
  }
}
