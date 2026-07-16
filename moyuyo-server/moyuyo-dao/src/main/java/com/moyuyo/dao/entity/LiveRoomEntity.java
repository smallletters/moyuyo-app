package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_live_room")
public class LiveRoomEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private String name;

  private String coverUrl;

  private String hostName;

  private String hostAvatar;

  private String status;

  private Integer viewerCount;

  private Integer productCount;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
