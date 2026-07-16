package com.moyuyo.service;

import com.moyuyo.dao.entity.MissionEntity;
import com.moyuyo.dao.entity.UserMissionEntity;

import java.util.List;
import java.util.Map;

public interface MissionService {

  List<MissionEntity> listAllMissions();

  List<UserMissionEntity> listUserMissions(Long userId);

  void claimReward(Long userId, Long missionId);

  Map<String, Object> getMissionStats(Long userId);
}
