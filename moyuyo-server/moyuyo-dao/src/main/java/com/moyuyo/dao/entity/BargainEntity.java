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
@TableName("mo_bargain")
public class BargainEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long productId;

  private Long skuId;

  private BigDecimal originalPrice;

  private BigDecimal bargainPrice;

  private Integer totalStock;

  private Integer remainingStock;

  private LocalDateTime endTime;

  private String status;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
