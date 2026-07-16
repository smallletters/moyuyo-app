package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moyuyo.dao.entity.BundleDealEntity;
import com.moyuyo.dao.entity.BundleDealItemEntity;
import com.moyuyo.dao.mapper.BundleDealItemMapper;
import com.moyuyo.dao.mapper.BundleDealMapper;
import com.moyuyo.service.BundleDealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BundleDealServiceImpl implements BundleDealService {

  private final BundleDealMapper bundleDealMapper;
  private final BundleDealItemMapper bundleDealItemMapper;

  @Override
  public List<BundleDealEntity> list() {
    return bundleDealMapper.selectList(
        new LambdaQueryWrapper<BundleDealEntity>()
            .eq(BundleDealEntity::getActive, true)
            .orderByDesc(BundleDealEntity::getCreateTime));
  }

  @Override
  public BundleDealEntity getDetail(Long id) {
    BundleDealEntity entity = bundleDealMapper.selectById(id);
    if (entity == null) {
      throw new IllegalArgumentException("套餐活动不存在");
    }
    return entity;
  }

  @Override
  public List<BundleDealItemEntity> getItems(Long id) {
    return bundleDealItemMapper.selectList(
        new LambdaQueryWrapper<BundleDealItemEntity>()
            .eq(BundleDealItemEntity::getBundleId, id)
            .orderByAsc(BundleDealItemEntity::getCreateTime));
  }
}
