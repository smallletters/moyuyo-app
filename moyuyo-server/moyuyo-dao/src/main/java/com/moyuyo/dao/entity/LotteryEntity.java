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
@TableName("mo_lottery")
public class LotteryEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private String name;

  private String description;

  private String prizeImage;

  private String prizeName;

  private BigDecimal probability;

  private Integer dailyFree;

  private Integer pointsCost;

  private Boolean active;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
