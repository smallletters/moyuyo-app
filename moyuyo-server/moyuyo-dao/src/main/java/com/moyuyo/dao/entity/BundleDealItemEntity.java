package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_bundle_deal_item")
public class BundleDealItemEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long bundleId;

  private Long productId;

  private Long skuId;

  private Boolean isMain;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
