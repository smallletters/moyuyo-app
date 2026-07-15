package com.moyuyo.common.dto.member;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletVO {

  private Long userId;

  private BigDecimal balance;

  private BigDecimal totalRecharged;

  private BigDecimal totalSpent;

  private String status;
}
