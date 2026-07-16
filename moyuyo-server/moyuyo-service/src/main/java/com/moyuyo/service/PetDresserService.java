package com.moyuyo.service;

import com.moyuyo.dao.entity.PetOutfitEntity;

import java.util.List;

public interface PetDresserService {

  List<PetOutfitEntity> listByPetId(Long petId, Long userId);

  void equip(Long id, Long petId, Long userId);

  void delete(Long id, Long petId, Long userId);
}
