package com.moyuyo.service;

import com.moyuyo.dao.entity.OrderEntity;

public interface WooCommerceSyncService {

    void syncOrderToWooCommerce(OrderEntity order);

    void syncOrderStatus(Long wooOrderId, String status);

    void syncAllPendingOrders();
}
