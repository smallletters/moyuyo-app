package com.moyuyo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moyuyo.dao.entity.AfterSalesEntity;

import java.math.BigDecimal;

public interface AfterSalesService {

  IPage<AfterSalesEntity> listAfterSales(Long userId, int page, int size, String status);

  AfterSalesEntity getAfterSalesDetail(Long id, Long userId);

  AfterSalesEntity createAfterSales(Long userId, Long orderId, Long orderItemId, String type,
                                    String reason, BigDecimal amount, String description, String images);
}
