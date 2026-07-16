package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("mo_gift_card_transaction")
public class GiftCardTransactionEntity {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private Long cardId;

  private String type;

  private BigDecimal amount;

  private Long orderId;

  private String remark;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
}
