package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_address")
public class AddressEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long userId;

  private String receiver;

  private String phone;

  private String country;

  private String province;

  private String city;

  private String district;

  private String detail;

  private String zipCode;

  private String tag;

  private Boolean isDefault;

  @TableLogic
  private Integer deleted;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createdAt;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updatedAt;
}
