package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_invite")
public class InviteEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long userId;

  private String inviteCode;

  private Long invitedUserId;

  private String status;

  private Integer pointsAwarded;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
