package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_group_buy_member")
public class GroupBuyMemberEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long groupBuyId;

  private Long userId;

  private Boolean isLeader;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
