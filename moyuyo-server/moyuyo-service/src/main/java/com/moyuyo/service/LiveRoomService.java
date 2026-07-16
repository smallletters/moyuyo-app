package com.moyuyo.service;

import com.moyuyo.dao.entity.LiveRoomEntity;
import com.moyuyo.dao.entity.LiveRoomProductEntity;

import java.util.List;

public interface LiveRoomService {

  List<LiveRoomEntity> list();

  LiveRoomEntity getDetail(Long id);

  List<LiveRoomProductEntity> getProducts(Long id);
}
