package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_points_log")
public class PointsLogEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long userId;

  private Integer changeValue;

  private String type;

  private String bizNo;

  private String remark;

  @TableLogic
  private Integer deleted;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createdAt;
}
