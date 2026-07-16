package com.moyuyo.service;

import com.moyuyo.dao.entity.BargainEntity;
import com.moyuyo.dao.entity.BargainHelpEntity;

import java.util.List;

public interface BargainService {

  List<BargainEntity> list();

  BargainEntity getDetail(Long id);

  BargainHelpEntity help(Long id, Long userId);

  List<BargainHelpEntity> getHelpers(Long id);
}
