package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("mo_pet_reminder")
public class PetReminderEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long petId;

  private String reminderType;

  private LocalDate nextDate;

  private Integer intervalDays;

  private Integer advanceDays;

  private Boolean enabled;

  private LocalDate lastNotifiedDate;

  private String extra;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createdAt;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updatedAt;
}
