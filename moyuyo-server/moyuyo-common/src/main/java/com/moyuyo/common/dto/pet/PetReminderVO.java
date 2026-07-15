package com.moyuyo.common.dto.pet;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PetReminderVO {

  private Long id;

  private Long petId;

  private String reminderType;

  private LocalDate nextDate;

  private Integer intervalDays;

  private Integer advanceDays;

  private Boolean enabled;

  private LocalDate lastNotifiedDate;

  private String extra;
}
