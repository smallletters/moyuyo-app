package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_member_task")
public class MemberTaskEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long userId;

  private String taskType;

  private String taskName;

  private Integer rewardPoints;

  private Boolean completed;

  @TableLogic
  private Integer deleted;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createdAt;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updatedAt;
}
