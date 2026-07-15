package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moyuyo.dao.entity.FavoriteEntity;
import com.moyuyo.dao.mapper.FavoriteMapper;
import com.moyuyo.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

  private final FavoriteMapper favoriteMapper;

  @Override
  public List<FavoriteEntity> getUserFavorites(Long userId) {
    return favoriteMapper.selectList(
        new LambdaQueryWrapper<FavoriteEntity>()
            .eq(FavoriteEntity::getUserId, userId)
            .orderByDesc(FavoriteEntity::getCreateTime));
  }

  @Override
  @Transactional
  public void addFavorite(Long userId, Long productId, Long skuId, Long groupId) {
    Long count = favoriteMapper.selectCount(
        new LambdaQueryWrapper<FavoriteEntity>()
            .eq(FavoriteEntity::getUserId, userId)
            .eq(FavoriteEntity::getProductId, productId));

    if (count > 0) {
      throw new IllegalArgumentException("该商品已收藏");
    }

    FavoriteEntity favorite = new FavoriteEntity();
    favorite.setUserId(userId);
    favorite.setProductId(productId);
    favorite.setGroupId(groupId);
    favoriteMapper.insert(favorite);

    log.info("Favorite added: userId={}, productId={}", userId, productId);
  }

  @Override
  @Transactional
  public void removeFavorite(Long userId, Long productId, Long skuId) {
    favoriteMapper.delete(
        new LambdaQueryWrapper<FavoriteEntity>()
            .eq(FavoriteEntity::getUserId, userId)
            .eq(FavoriteEntity::getProductId, productId));
  }
}
