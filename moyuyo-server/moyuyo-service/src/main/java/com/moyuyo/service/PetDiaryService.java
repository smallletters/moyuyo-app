package com.moyuyo.service;

import com.moyuyo.dao.entity.PetDiaryEntity;

import java.util.List;

public interface PetDiaryService {

  List<PetDiaryEntity> listByPetId(Long petId, Long userId);

  PetDiaryEntity getDetail(Long id, Long petId, Long userId);

  PetDiaryEntity create(Long petId, Long userId, PetDiaryEntity entity);

  void delete(Long id, Long petId, Long userId);
}
