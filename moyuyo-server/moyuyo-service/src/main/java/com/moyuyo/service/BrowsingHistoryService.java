package com.moyuyo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moyuyo.dao.entity.BrowsingHistoryEntity;

import java.util.Map;

public interface BrowsingHistoryService {

  IPage<Map<String, Object>> listHistoryGrouped(Long userId, int page, int size);

  void clearAll(Long userId);

  void deleteOne(Long id, Long userId);

  void addRecord(Long userId, Long productId);
}
