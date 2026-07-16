package com.moyuyo.service;

import com.moyuyo.dao.entity.PetWeightEntity;

import java.util.List;

public interface PetWeightService {

  List<PetWeightEntity> listByPetId(Long petId, Long userId);

  PetWeightEntity create(Long petId, Long userId, PetWeightEntity entity);

  List<PetWeightEntity> getChart(Long petId, Long userId);
}
