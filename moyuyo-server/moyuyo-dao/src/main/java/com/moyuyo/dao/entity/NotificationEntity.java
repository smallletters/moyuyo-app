package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_notification")
public class NotificationEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long userId;

  private String type;

  private String title;

  private String content;

  private Long relatedId;

  private Integer read;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
