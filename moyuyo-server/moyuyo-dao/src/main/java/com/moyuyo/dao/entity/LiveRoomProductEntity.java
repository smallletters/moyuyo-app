package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_live_room_product")
public class LiveRoomProductEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long roomId;

  private Long productId;

  private Integer sortOrder;

  private String status;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
