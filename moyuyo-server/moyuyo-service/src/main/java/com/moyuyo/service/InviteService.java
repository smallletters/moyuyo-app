package com.moyuyo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moyuyo.dao.entity.InviteEntity;

import java.util.Map;

public interface InviteService {

  String getInviteCode(Long userId);

  Map<String, Object> getInviteStats(Long userId);

  IPage<InviteEntity> getInviteHistory(Long userId, int page, int size);
}
