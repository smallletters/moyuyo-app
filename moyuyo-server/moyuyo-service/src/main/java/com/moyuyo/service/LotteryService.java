package com.moyuyo.service;

import com.moyuyo.dao.entity.LotteryEntity;
import com.moyuyo.dao.entity.LotteryRecordEntity;

import java.util.List;
import java.util.Map;

public interface LotteryService {

  List<LotteryEntity> list();

  LotteryRecordEntity spin(Long userId, Long lotteryId);

  List<LotteryRecordEntity> getHistory(Long userId);

  Map<String, Object> getStats(Long userId);
}
