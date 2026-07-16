package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moyuyo.dao.entity.PetOutfitEntity;
import com.moyuyo.dao.mapper.PetOutfitMapper;
import com.moyuyo.service.PetDresserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetDresserServiceImpl implements PetDresserService {

  private final PetOutfitMapper petOutfitMapper;

  @Override
  public List<PetOutfitEntity> listByPetId(Long petId, Long userId) {
    return petOutfitMapper.selectList(
        new LambdaQueryWrapper<PetOutfitEntity>()
            .eq(PetOutfitEntity::getPetId, petId)
            .orderByDesc(PetOutfitEntity::getCreateTime));
  }

  @Override
  @Transactional
  public void equip(Long id, Long petId, Long userId) {
    PetOutfitEntity existing = petOutfitMapper.selectById(id);
    if (existing == null || !existing.getPetId().equals(petId)) {
      throw new IllegalArgumentException("装扮不存在或无权操作");
    }
    List<PetOutfitEntity> equipped = petOutfitMapper.selectList(
        new LambdaQueryWrapper<PetOutfitEntity>()
            .eq(PetOutfitEntity::getPetId, petId)
            .eq(PetOutfitEntity::getEquipped, true));
    for (PetOutfitEntity item : equipped) {
      item.setEquipped(false);
      petOutfitMapper.updateById(item);
    }
    existing.setEquipped(true);
    petOutfitMapper.updateById(existing);
    log.info("PetOutfit equipped: outfitId={}, petId={}", id, petId);
  }

  @Override
  @Transactional
  public void delete(Long id, Long petId, Long userId) {
    PetOutfitEntity existing = petOutfitMapper.selectById(id);
    if (existing == null || !existing.getPetId().equals(petId)) {
      throw new IllegalArgumentException("装扮不存在或无权操作");
    }
    petOutfitMapper.deleteById(id);
    log.info("PetOutfit deleted: outfitId={}, petId={}", id, petId);
  }
}
