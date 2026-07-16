package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.dao.entity.BrowsingHistoryEntity;
import com.moyuyo.dao.mapper.BrowsingHistoryMapper;
import com.moyuyo.service.BrowsingHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrowsingHistoryServiceImpl implements BrowsingHistoryService {

  private final BrowsingHistoryMapper browsingHistoryMapper;

  @Override
  public IPage<Map<String, Object>> listHistoryGrouped(Long userId, int page, int size) {
    IPage<BrowsingHistoryEntity> entityPage = browsingHistoryMapper.selectPage(new Page<>(page, size),
        new LambdaQueryWrapper<BrowsingHistoryEntity>()
            .eq(BrowsingHistoryEntity::getUserId, userId)
            .orderByDesc(BrowsingHistoryEntity::getCreateTime));

    Map<String, List<BrowsingHistoryEntity>> grouped = entityPage.getRecords().stream()
        .collect(Collectors.groupingBy(
            e -> e.getCreateTime().toLocalDate().toString(),
            LinkedHashMap::new,
            Collectors.toList()));

    List<Map<String, Object>> records = grouped.entrySet().stream().map(entry -> {
      Map<String, Object> group = new LinkedHashMap<>();
      group.put("date", entry.getKey());
      group.put("items", entry.getValue());
      return group;
    }).collect(Collectors.toList());

    IPage<Map<String, Object>> result = new Page<>(entityPage.getCurrent(), entityPage.getSize());
    result.setTotal(entityPage.getTotal());
    result.setRecords(records);
    return result;
  }

  @Override
  @Transactional
  public void clearAll(Long userId) {
    browsingHistoryMapper.delete(
        new LambdaQueryWrapper<BrowsingHistoryEntity>()
            .eq(BrowsingHistoryEntity::getUserId, userId));
  }

  @Override
  @Transactional
  public void deleteOne(Long id, Long userId) {
    BrowsingHistoryEntity entity = browsingHistoryMapper.selectById(id);
    if (entity == null) {
      throw new IllegalArgumentException("记录不存在");
    }
    if (!Objects.equals(entity.getUserId(), userId)) {
      throw new IllegalArgumentException("无权删除该记录");
    }
    browsingHistoryMapper.deleteById(id);
  }

  @Override
  @Transactional
  public void addRecord(Long userId, Long productId) {
    BrowsingHistoryEntity entity = new BrowsingHistoryEntity();
    entity.setUserId(userId);
    entity.setProductId(productId);
    browsingHistoryMapper.insert(entity);
  }
}
