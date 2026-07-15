package com.moyuyo.common.dto.pet;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PetAchievementVO {

  private Long id;

  private Long petId;

  private String achievementCode;

  private Boolean unlocked;

  private LocalDateTime unlockedAt;

  private Integer progress;
}
