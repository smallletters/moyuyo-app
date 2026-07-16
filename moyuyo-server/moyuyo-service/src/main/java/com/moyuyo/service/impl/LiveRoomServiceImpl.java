package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moyuyo.dao.entity.LiveRoomEntity;
import com.moyuyo.dao.entity.LiveRoomProductEntity;
import com.moyuyo.dao.mapper.LiveRoomMapper;
import com.moyuyo.dao.mapper.LiveRoomProductMapper;
import com.moyuyo.service.LiveRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiveRoomServiceImpl implements LiveRoomService {

  private final LiveRoomMapper liveRoomMapper;
  private final LiveRoomProductMapper liveRoomProductMapper;

  @Override
  public List<LiveRoomEntity> list() {
    return liveRoomMapper.selectList(
        new LambdaQueryWrapper<LiveRoomEntity>()
            .eq(LiveRoomEntity::getStatus, "LIVE")
            .orderByDesc(LiveRoomEntity::getViewerCount));
  }

  @Override
  public LiveRoomEntity getDetail(Long id) {
    LiveRoomEntity entity = liveRoomMapper.selectById(id);
    if (entity == null) {
      throw new IllegalArgumentException("直播间不存在");
    }
    return entity;
  }

  @Override
  public List<LiveRoomProductEntity> getProducts(Long id) {
    return liveRoomProductMapper.selectList(
        new LambdaQueryWrapper<LiveRoomProductEntity>()
            .eq(LiveRoomProductEntity::getRoomId, id)
            .orderByAsc(LiveRoomProductEntity::getSortOrder));
  }
}
