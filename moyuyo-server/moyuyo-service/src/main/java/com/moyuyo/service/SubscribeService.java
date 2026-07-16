package com.moyuyo.service;

import com.moyuyo.dao.entity.SubscribePlanEntity;
import com.moyuyo.dao.entity.SubscriptionHistoryEntity;
import com.moyuyo.dao.entity.UserSubscriptionEntity;

import java.util.List;

public interface SubscribeService {

  List<SubscribePlanEntity> getPlans();

  UserSubscriptionEntity subscribe(Long userId, Long planId);

  UserSubscriptionEntity getMySubscription(Long userId);

  void updateAutoRenew(Long userId, Boolean autoRenew);

  List<SubscriptionHistoryEntity> getHistory(Long userId);
}
