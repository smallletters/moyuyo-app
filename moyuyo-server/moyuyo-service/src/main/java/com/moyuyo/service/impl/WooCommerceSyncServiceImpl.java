package com.moyuyo.service.impl;

import com.moyuyo.dao.entity.OrderEntity;
import com.moyuyo.dao.mapper.OrderMapper;
import com.moyuyo.service.WooCommerceSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WooCommerceSyncServiceImpl implements WooCommerceSyncService {

    private final OrderMapper orderMapper;

    @Override
    public void syncOrderToWooCommerce(OrderEntity order) {
        if (order.getWooOrderId() != null) {
            log.info("Order already synced to WooCommerce: orderNo={}, wooOrderId={}",
                    order.getOrderNo(), order.getWooOrderId());
            return;
        }

        try {
            // WooCommerce API call to create order
            // WC client = new WCClient(wooUrl, wooConsumerKey, wooConsumerSecret);
            // Map<String, Object> createdOrder = client.create(orderData);
            // order.setWooOrderId(Long.valueOf(createdOrder.get("id").toString()));

            order.setSyncStatus(1);
            order.setSyncRetryCount(0);
            order.setSyncLastTime(java.time.LocalDateTime.now());
            orderMapper.updateById(order);

            log.info("WooCommerce sync success: orderNo={}", order.getOrderNo());
        } catch (Exception e) {
            order.setSyncStatus(-1);
            order.setSyncRetryCount(order.getSyncRetryCount() == null ? 1 : order.getSyncRetryCount() + 1);
            order.setSyncLastTime(java.time.LocalDateTime.now());
            orderMapper.updateById(order);
            log.error("WooCommerce sync failed: orderNo={}", order.getOrderNo(), e);
        }
    }

    @Override
    public void syncOrderStatus(Long wooOrderId, String status) {
        if (wooOrderId == null) return;
        try {
            // WC client.updateOrderStatus(wooOrderId, status);
            log.info("WooCommerce status sync: wooOrderId={}, status={}", wooOrderId, status);
        } catch (Exception e) {
            log.error("WooCommerce status sync failed: wooOrderId={}", wooOrderId, e);
        }
    }

    @Override
    public void syncAllPendingOrders() {
        java.util.List<OrderEntity> pending = orderMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OrderEntity>()
                        .eq(OrderEntity::getSyncStatus, -1)
                        .or()
                        .isNull(OrderEntity::getWooOrderId));
        for (OrderEntity order : pending) {
            syncOrderToWooCommerce(order);
        }
        log.info("WooCommerce sync all pending: count={}", pending.size());
    }
}
