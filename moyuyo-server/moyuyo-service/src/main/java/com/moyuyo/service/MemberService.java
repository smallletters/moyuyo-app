package com.moyuyo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.common.dto.member.MemberVO;
import com.moyuyo.common.dto.member.WalletVO;
import com.moyuyo.dao.entity.PointsLogEntity;

import java.math.BigDecimal;

public interface MemberService {

  MemberVO getMemberInfo(Long userId);

  Page<PointsLogEntity> getPointsLog(Long userId, int page, int size);

  void addPoints(Long userId, int changeValue, String type, String bizNo, String remark);

  void spendPoints(Long userId, int changeValue, String bizNo, String remark);

  int getPointsBalance(Long userId);

  WalletVO getWallet(Long userId);

  WalletVO recharge(Long userId, BigDecimal amount, String channel);
}
