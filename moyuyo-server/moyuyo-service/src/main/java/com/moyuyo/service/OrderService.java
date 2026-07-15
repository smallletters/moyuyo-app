package com.moyuyo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moyuyo.dao.entity.OrderEntity;
import com.moyuyo.dao.entity.OrderItemEntity;

import java.util.List;

public interface OrderService {

  OrderEntity createOrder(Long userId, List<OrderItemEntity> items, Long addressId, String remark, String couponId);

  IPage<OrderEntity> listOrders(Long userId, int page, int size, String status);

  OrderEntity getOrderDetail(Long orderId, Long userId);

  void cancelOrder(Long orderId, Long userId, String reason);

  void payCallback(String orderNo, String payChannel, String transactionId);

  void confirmReceived(Long orderId, Long userId);

  void deleteOrder(Long orderId, Long userId);

  List<OrderItemEntity> getOrderItems(Long orderId);

  OrderEntity getOrderByOrderNo(String orderNo);
}
