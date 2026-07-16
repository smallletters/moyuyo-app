package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_feedback")
public class FeedbackEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long userId;

  private String type;

  private String content;

  private String images;

  private String contact;

  private String status;

  private String replyContent;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
