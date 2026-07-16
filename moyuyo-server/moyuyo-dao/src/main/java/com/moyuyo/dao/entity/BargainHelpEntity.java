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
@TableName("mo_bargain_help")
public class BargainHelpEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long bargainId;

  private Long userId;

  private BigDecimal helpAmount;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
