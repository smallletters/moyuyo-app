package com.moyuyo.common.dto.pet;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PetSceneVO {

  private Long id;

  private Long petId;

  private String sceneCode;

  private Boolean unlocked;

  private LocalDateTime unlockedAt;

  private Integer sortOrder;
}
