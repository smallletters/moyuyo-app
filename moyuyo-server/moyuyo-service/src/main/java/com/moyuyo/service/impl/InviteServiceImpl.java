package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.dao.entity.InviteEntity;
import com.moyuyo.dao.mapper.InviteMapper;
import com.moyuyo.service.InviteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

  private final InviteMapper inviteMapper;

  @Override
  public String getInviteCode(Long userId) {
    InviteEntity existing = inviteMapper.selectOne(
        new LambdaQueryWrapper<InviteEntity>()
            .eq(InviteEntity::getUserId, userId)
            .isNull(InviteEntity::getInvitedUserId));

    if (existing != null) {
      return existing.getInviteCode();
    }

    String code = generateInviteCode();
    InviteEntity entity = new InviteEntity();
    entity.setUserId(userId);
    entity.setInviteCode(code);
    entity.setStatus("PENDING");
    entity.setPointsAwarded(0);
    inviteMapper.insert(entity);

    return code;
  }

  @Override
  public Map<String, Object> getInviteStats(Long userId) {
    long invitedCount = inviteMapper.selectCount(
        new LambdaQueryWrapper<InviteEntity>()
            .eq(InviteEntity::getUserId, userId)
            .isNotNull(InviteEntity::getInvitedUserId));

    int earnedPoints = inviteMapper.selectList(
        new LambdaQueryWrapper<InviteEntity>()
            .eq(InviteEntity::getUserId, userId))
        .stream()
        .mapToInt(i -> i.getPointsAwarded() != null ? i.getPointsAwarded() : 0)
        .sum();

    long completedOrders = inviteMapper.selectCount(
        new LambdaQueryWrapper<InviteEntity>()
            .eq(InviteEntity::getUserId, userId)
            .eq(InviteEntity::getStatus, "ORDERED"));

    Map<String, Object> stats = new HashMap<>();
    stats.put("invitedCount", invitedCount);
    stats.put("earnedPoints", earnedPoints);
    stats.put("completedOrders", completedOrders);
    return stats;
  }

  @Override
  public IPage<InviteEntity> getInviteHistory(Long userId, int page, int size) {
    return inviteMapper.selectPage(new Page<>(page, size),
        new LambdaQueryWrapper<InviteEntity>()
            .eq(InviteEntity::getUserId, userId)
            .isNotNull(InviteEntity::getInvitedUserId)
            .orderByDesc(InviteEntity::getCreateTime));
  }

  private String generateInviteCode() {
    return "MY" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
  }
}
