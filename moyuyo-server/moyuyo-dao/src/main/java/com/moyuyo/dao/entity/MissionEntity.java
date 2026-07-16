package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("mo_mission")
public class MissionEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private String type;

  private String name;

  private String description;

  private String icon;

  private Integer points;

  private Integer target;

  private Integer sortOrder;

  private Integer active;
}
