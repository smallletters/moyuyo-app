package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("mo_pet")
public class PetEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long userId;

  private String name;

  private String species;

  private String breed;

  private String gender;

  private LocalDate birthday;

  private String avatar;

  private Double weight;

  private String notes;

  @TableLogic
  private Integer deleted;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createdAt;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updatedAt;
}
