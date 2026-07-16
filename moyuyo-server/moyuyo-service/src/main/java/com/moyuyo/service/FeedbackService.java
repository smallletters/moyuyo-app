package com.moyuyo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moyuyo.dao.entity.FeedbackEntity;

public interface FeedbackService {

  void submitFeedback(Long userId, String type, String content, String images, String contact);

  IPage<FeedbackEntity> listMyFeedback(Long userId, int page, int size);
}
