package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("mo_pet_diary")
public class PetDiaryEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long petId;

  private String title;

  private String content;

  private String images;

  private String mood;

  private String tags;

  private BigDecimal weight;

  private Integer likes;

  private Integer comments;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
