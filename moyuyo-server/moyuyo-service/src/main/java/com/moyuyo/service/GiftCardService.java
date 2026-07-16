package com.moyuyo.service;

import com.moyuyo.dao.entity.GiftCardEntity;
import com.moyuyo.dao.entity.GiftCardTransactionEntity;

import java.util.List;

public interface GiftCardService {

  List<GiftCardEntity> listByUserId(Long userId);

  GiftCardEntity bind(Long userId, String cardNo, String pin);

  List<GiftCardEntity> purchase(Long userId, Integer amount, Integer quantity);

  List<GiftCardTransactionEntity> getTransactions(Long cardId, Long userId);
}
