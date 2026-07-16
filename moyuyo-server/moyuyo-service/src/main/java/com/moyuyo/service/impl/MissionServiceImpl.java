package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moyuyo.dao.entity.MissionEntity;
import com.moyuyo.dao.entity.UserMissionEntity;
import com.moyuyo.dao.mapper.MissionMapper;
import com.moyuyo.dao.mapper.UserMissionMapper;
import com.moyuyo.service.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {

  private final MissionMapper missionMapper;
  private final UserMissionMapper userMissionMapper;

  @Override
  public List<MissionEntity> listAllMissions() {
    return missionMapper.selectList(
        new LambdaQueryWrapper<MissionEntity>()
            .eq(MissionEntity::getActive, 1)
            .orderByAsc(MissionEntity::getSortOrder));
  }

  @Override
  public List<UserMissionEntity> listUserMissions(Long userId) {
    return userMissionMapper.selectList(
        new LambdaQueryWrapper<UserMissionEntity>()
            .eq(UserMissionEntity::getUserId, userId));
  }

  @Override
  @Transactional
  public void claimReward(Long userId, Long missionId) {
    MissionEntity mission = missionMapper.selectById(missionId);
    if (mission == null) {
      throw new IllegalArgumentException("任务不存在");
    }

    UserMissionEntity userMission = userMissionMapper.selectOne(
        new LambdaQueryWrapper<UserMissionEntity>()
            .eq(UserMissionEntity::getUserId, userId)
            .eq(UserMissionEntity::getMissionId, missionId));

    if (userMission == null) {
      throw new IllegalArgumentException("未领取该任务");
    }
    if (userMission.getCompleted() != 1) {
      throw new IllegalStateException("任务未完成，无法领取奖励");
    }
    if (userMission.getClaimed() == 1) {
      throw new IllegalStateException("奖励已领取");
    }

    userMission.setClaimed(1);
    userMissionMapper.updateById(userMission);
    log.info("Mission reward claimed: userId={}, missionId={}, points={}", userId, missionId, mission.getPoints());
  }

  @Override
  public Map<String, Object> getMissionStats(Long userId) {
    List<UserMissionEntity> userMissions = listUserMissions(userId);

    int todayPoints = userMissions.stream()
        .filter(um -> um.getCompleted() == 1 && um.getClaimed() == 1)
        .mapToInt(um -> {
          MissionEntity m = missionMapper.selectById(um.getMissionId());
          return m != null ? m.getPoints() : 0;
        }).sum();

    long completedCount = userMissions.stream()
        .filter(um -> um.getCompleted() == 1)
        .count();

    List<MissionEntity> allMissions = listAllMissions();
    long totalMissions = allMissions.size();

    Map<String, Object> stats = new HashMap<>();
    stats.put("todayPoints", todayPoints);
    stats.put("completedCount", completedCount);
    stats.put("totalMissions", totalMissions);
    return stats;
  }
}
