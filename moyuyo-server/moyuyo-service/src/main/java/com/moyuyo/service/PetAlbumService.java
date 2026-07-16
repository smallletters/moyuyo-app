package com.moyuyo.service;

import com.moyuyo.dao.entity.PetAlbumEntity;

import java.util.List;

public interface PetAlbumService {

  List<PetAlbumEntity> listByPetId(Long petId, Long userId);

  PetAlbumEntity create(Long petId, Long userId, PetAlbumEntity entity);

  void delete(Long id, Long petId, Long userId);
}
