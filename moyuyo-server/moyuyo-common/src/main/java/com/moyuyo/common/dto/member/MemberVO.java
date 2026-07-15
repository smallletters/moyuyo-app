package com.moyuyo.common.dto.member;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberVO {

  private Long userId;

  private String level;

  private Integer growthValue;

  private Integer points;

  private BigDecimal balance;
}
