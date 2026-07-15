package com.moyuyo.service;

import com.moyuyo.dao.entity.FavoriteEntity;

import java.util.List;

public interface FavoriteService {

  List<FavoriteEntity> getUserFavorites(Long userId);

  void addFavorite(Long userId, Long productId, Long skuId, Long groupId);

  void removeFavorite(Long userId, Long productId, Long skuId);
}
