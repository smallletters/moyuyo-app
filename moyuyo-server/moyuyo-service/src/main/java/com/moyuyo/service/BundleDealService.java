package com.moyuyo.service;

import com.moyuyo.dao.entity.BundleDealEntity;
import com.moyuyo.dao.entity.BundleDealItemEntity;

import java.util.List;

public interface BundleDealService {

  List<BundleDealEntity> list();

  BundleDealEntity getDetail(Long id);

  List<BundleDealItemEntity> getItems(Long id);
}
