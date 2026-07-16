package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moyuyo.dao.entity.PetAlbumEntity;
import com.moyuyo.dao.mapper.PetAlbumMapper;
import com.moyuyo.service.PetAlbumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetAlbumServiceImpl implements PetAlbumService {

  private final PetAlbumMapper petAlbumMapper;

  @Override
  public List<PetAlbumEntity> listByPetId(Long petId, Long userId) {
    return petAlbumMapper.selectList(
        new LambdaQueryWrapper<PetAlbumEntity>()
            .eq(PetAlbumEntity::getPetId, petId)
            .orderByDesc(PetAlbumEntity::getCreateTime));
  }

  @Override
  @Transactional
  public PetAlbumEntity create(Long petId, Long userId, PetAlbumEntity entity) {
    entity.setId(null);
    entity.setPetId(petId);
    petAlbumMapper.insert(entity);
    log.info("PetAlbum created: petId={}, albumId={}", petId, entity.getId());
    return entity;
  }

  @Override
  @Transactional
  public void delete(Long id, Long petId, Long userId) {
    PetAlbumEntity existing = petAlbumMapper.selectById(id);
    if (existing == null || !existing.getPetId().equals(petId)) {
      throw new IllegalArgumentException("相片不存在或无权操作");
    }
    petAlbumMapper.deleteById(id);
    log.info("PetAlbum deleted: albumId={}, petId={}", id, petId);
  }
}
