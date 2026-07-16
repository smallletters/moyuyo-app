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
@TableName("mo_bundle_deal")
public class BundleDealEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private String name;

  private String description;

  private Long mainProductId;

  private BigDecimal bundlePrice;

  private BigDecimal originalTotal;

  private BigDecimal discountPercent;

  private Boolean active;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
