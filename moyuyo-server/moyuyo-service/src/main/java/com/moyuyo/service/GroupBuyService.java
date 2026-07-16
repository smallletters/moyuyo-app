package com.moyuyo.service;

import com.moyuyo.dao.entity.GroupBuyEntity;
import com.moyuyo.dao.entity.GroupBuyMemberEntity;

import java.util.List;

public interface GroupBuyService {

  List<GroupBuyEntity> list();

  GroupBuyEntity getDetail(Long id);

  GroupBuyMemberEntity join(Long id, Long userId);

  List<GroupBuyMemberEntity> getMembers(Long id);
}
