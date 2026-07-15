package com.moyuyo.service;

import com.moyuyo.dao.entity.LogisticsEntity;

public interface LogisticsService {

    LogisticsEntity shipOrder(Long orderId, String carrier, String trackingNumber);

    LogisticsEntity getLogisticsByOrderId(Long orderId);

    LogisticsEntity updateTracking(Long logisticsId, String traces);

    void confirmReceived(Long orderId);
}
