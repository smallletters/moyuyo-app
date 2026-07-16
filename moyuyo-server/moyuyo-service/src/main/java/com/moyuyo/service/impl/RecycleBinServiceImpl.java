package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.dao.entity.OrderEntity;
import com.moyuyo.dao.mapper.OrderMapper;
import com.moyuyo.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements RecycleBinService {

  private final OrderMapper orderMapper;

  @Override
  public IPage<OrderEntity> listDeletedOrders(Long userId, int page, int size) {
    return orderMapper.selectPage(new Page<>(page, size),
        new LambdaQueryWrapper<OrderEntity>()
            .eq(OrderEntity::getUserId, userId)
            .eq(OrderEntity::getDeleteStatus, 1)
            .orderByDesc(OrderEntity::getUpdateTime));
  }

  @Override
  @Transactional
  public void restoreOrder(Long orderId, Long userId) {
    OrderEntity order = orderMapper.selectById(orderId);
    if (order == null) {
      throw new IllegalArgumentException("订单不存在");
    }
    if (!Objects.equals(order.getUserId(), userId)) {
      throw new IllegalArgumentException("无权操作该订单");
    }
    if (order.getDeleteStatus() != 1) {
      throw new IllegalStateException("订单不在回收站中");
    }
    order.setDeleteStatus(0);
    orderMapper.updateById(order);
    log.info("Order restored from recycle bin: orderId={}, userId={}", orderId, userId);
  }

  @Override
  @Transactional
  public void permanentlyDelete(Long orderId, Long userId) {
    OrderEntity order = orderMapper.selectById(orderId);
    if (order == null) {
      throw new IllegalArgumentException("订单不存在");
    }
    if (!Objects.equals(order.getUserId(), userId)) {
      throw new IllegalArgumentException("无权操作该订单");
    }
    if (order.getDeleteStatus() != 1) {
      throw new IllegalStateException("订单不在回收站中");
    }
    orderMapper.deleteById(orderId);
    log.info("Order permanently deleted: orderId={}, userId={}", orderId, userId);
  }

  @Override
  @Transactional
  public void clearAll(Long userId) {
    orderMapper.delete(
        new LambdaQueryWrapper<OrderEntity>()
            .eq(OrderEntity::getUserId, userId)
            .eq(OrderEntity::getDeleteStatus, 1));
    log.info("Recycle bin cleared: userId={}", userId);
  }
}
