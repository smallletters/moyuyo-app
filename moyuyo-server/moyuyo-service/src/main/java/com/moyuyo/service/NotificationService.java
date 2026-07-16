package com.moyuyo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moyuyo.dao.entity.NotificationEntity;

public interface NotificationService {

  IPage<NotificationEntity> listNotifications(Long userId, int page, int size);

  NotificationEntity getNotificationDetail(Long id, Long userId);

  void markAsRead(Long id, Long userId);

  void markAllAsRead(Long userId);

  void deleteNotification(Long id, Long userId);
}
