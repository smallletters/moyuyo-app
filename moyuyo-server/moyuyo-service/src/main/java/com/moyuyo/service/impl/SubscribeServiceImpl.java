package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moyuyo.dao.entity.SubscribePlanEntity;
import com.moyuyo.dao.entity.SubscriptionHistoryEntity;
import com.moyuyo.dao.entity.UserSubscriptionEntity;
import com.moyuyo.dao.mapper.SubscribePlanMapper;
import com.moyuyo.dao.mapper.SubscriptionHistoryMapper;
import com.moyuyo.dao.mapper.UserSubscriptionMapper;
import com.moyuyo.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {

  private final SubscribePlanMapper subscribePlanMapper;
  private final UserSubscriptionMapper userSubscriptionMapper;
  private final SubscriptionHistoryMapper subscriptionHistoryMapper;

  @Override
  public List<SubscribePlanEntity> getPlans() {
    return subscribePlanMapper.selectList(
        new LambdaQueryWrapper<SubscribePlanEntity>()
            .eq(SubscribePlanEntity::getActive, true)
            .orderByAsc(SubscribePlanEntity::getSortOrder));
  }

  @Override
  @Transactional
  public UserSubscriptionEntity subscribe(Long userId, Long planId) {
    SubscribePlanEntity plan = subscribePlanMapper.selectById(planId);
    if (plan == null || !plan.getActive()) {
      throw new IllegalArgumentException("订阅方案不存在或已下架");
    }
    UserSubscriptionEntity existing = userSubscriptionMapper.selectOne(
        new LambdaQueryWrapper<UserSubscriptionEntity>()
            .eq(UserSubscriptionEntity::getUserId, userId)
            .eq(UserSubscriptionEntity::getStatus, "ACTIVE"));
    if (existing != null) {
      throw new IllegalArgumentException("您已有有效的订阅");
    }
    UserSubscriptionEntity sub = new UserSubscriptionEntity();
    sub.setUserId(userId);
    sub.setPlanId(planId);
    sub.setStatus("ACTIVE");
    sub.setStartTime(LocalDateTime.now());
    sub.setEndTime(LocalDateTime.now().plusMonths(plan.getDurationMonths()));
    sub.setAutoRenew(true);
    userSubscriptionMapper.insert(sub);
    SubscriptionHistoryEntity history = new SubscriptionHistoryEntity();
    history.setUserId(userId);
    history.setPlanId(planId);
    history.setAction("SUBSCRIBE");
    history.setAmount(plan.getPrice());
    subscriptionHistoryMapper.insert(history);
    log.info("User subscribed: userId={}, planId={}", userId, planId);
    return sub;
  }

  @Override
  public UserSubscriptionEntity getMySubscription(Long userId) {
    return userSubscriptionMapper.selectOne(
        new LambdaQueryWrapper<UserSubscriptionEntity>()
            .eq(UserSubscriptionEntity::getUserId, userId)
            .orderByDesc(UserSubscriptionEntity::getCreateTime)
            .last("LIMIT 1"));
  }

  @Override
  @Transactional
  public void updateAutoRenew(Long userId, Boolean autoRenew) {
    UserSubscriptionEntity sub = userSubscriptionMapper.selectOne(
        new LambdaQueryWrapper<UserSubscriptionEntity>()
            .eq(UserSubscriptionEntity::getUserId, userId)
            .eq(UserSubscriptionEntity::getStatus, "ACTIVE"));
    if (sub == null) {
      throw new IllegalArgumentException("没有有效的订阅");
    }
    sub.setAutoRenew(autoRenew);
    userSubscriptionMapper.updateById(sub);
    log.info("AutoRenew updated: userId={}, autoRenew={}", userId, autoRenew);
  }

  @Override
  public List<SubscriptionHistoryEntity> getHistory(Long userId) {
    return subscriptionHistoryMapper.selectList(
        new LambdaQueryWrapper<SubscriptionHistoryEntity>()
            .eq(SubscriptionHistoryEntity::getUserId, userId)
            .orderByDesc(SubscriptionHistoryEntity::getCreateTime));
  }
}
