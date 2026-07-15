package com.moyuyo.service;

import com.moyuyo.common.dto.refund.RefundApplyRequest;
import com.moyuyo.common.dto.refund.RefundVO;

public interface RefundService {

    RefundVO applyRefund(Long userId, RefundApplyRequest request);

    void approveRefund(Long refundId, Long operatorId);

    void rejectRefund(Long refundId, Long operatorId, String reason);

    void completeRefund(Long refundId, Long operatorId, String transactionId);

    RefundVO getRefundDetail(Long refundId, Long userId);

    com.baomidou.mybatisplus.core.metadata.IPage<RefundVO> listUserRefunds(Long userId, int page, int size);

    com.baomidou.mybatisplus.core.metadata.IPage<RefundVO> listAllRefunds(int page, int size, String status);
}
