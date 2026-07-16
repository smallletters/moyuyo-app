package com.moyuyo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moyuyo.dao.entity.OrderEntity;

public interface RecycleBinService {

  IPage<OrderEntity> listDeletedOrders(Long userId, int page, int size);

  void restoreOrder(Long orderId, Long userId);

  void permanentlyDelete(Long orderId, Long userId);

  void clearAll(Long userId);
}
