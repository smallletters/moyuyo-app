package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_lottery_record")
public class LotteryRecordEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long userId;

  private Long lotteryId;

  private String prizeName;

  private Boolean won;

  private Boolean usedFreeSpin;

  private Integer pointsSpent;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
