package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_user_mission")
public class UserMissionEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long userId;

  private Long missionId;

  private Integer progress;

  private Integer completed;

  private Integer claimed;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
