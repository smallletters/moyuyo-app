package com.moyuyo.service;

import com.moyuyo.common.dto.pet.PetReminderVO;
import com.moyuyo.common.dto.pet.PetVO;
import com.moyuyo.dao.entity.GrowthRecordEntity;
import com.moyuyo.dao.entity.PetAchievementEntity;
import com.moyuyo.dao.entity.PetEntity;
import com.moyuyo.dao.entity.PetReminderEntity;

import java.util.List;

public interface PetService {

  List<PetVO> listByUserId(Long userId);

  PetVO getPetDetail(Long petId, Long userId);

  PetEntity createPet(Long userId, PetEntity pet);

  PetEntity updatePet(Long userId, PetEntity pet);

  void deletePet(Long petId, Long userId);

  List<GrowthRecordEntity> getGrowthRecords(Long petId, Long userId);

  GrowthRecordEntity createGrowthRecord(Long userId, GrowthRecordEntity record);

  List<PetReminderVO> getReminders(Long petId, Long userId);

  PetReminderEntity updateReminder(Long userId, PetReminderEntity reminder);

  List<PetAchievementEntity> getAchievements(Long petId, Long userId);
}
