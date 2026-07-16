package com.moyuyo.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moyuyo.common.Result;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.NotificationEntity;
import com.moyuyo.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "通知管理")
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @Operation(summary = "获取通知列表")
  @GetMapping
  public Result<IPage<NotificationEntity>> list(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int size) {
    return Result.success(notificationService.listNotifications(UserContextHolder.getUserId(), page, size));
  }

  @Operation(summary = "通知详情")
  @GetMapping("/{id}")
  public Result<NotificationEntity> getDetail(@PathVariable Long id) {
    return Result.success(notificationService.getNotificationDetail(id, UserContextHolder.getUserId()));
  }

  @Operation(summary = "标记已读")
  @PutMapping("/{id}/read")
  public Result<Void> markAsRead(@PathVariable Long id) {
    notificationService.markAsRead(id, UserContextHolder.getUserId());
    return Result.success();
  }

  @Operation(summary = "全部标记已读")
  @PostMapping("/read-all")
  public Result<Void> markAllAsRead() {
    notificationService.markAllAsRead(UserContextHolder.getUserId());
    return Result.success();
  }

  @Operation(summary = "删除通知")
  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable Long id) {
    notificationService.deleteNotification(id, UserContextHolder.getUserId());
    return Result.success();
  }
}
