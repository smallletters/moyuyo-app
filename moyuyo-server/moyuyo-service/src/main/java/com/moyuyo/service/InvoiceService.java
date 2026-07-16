package com.moyuyo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moyuyo.dao.entity.InvoiceEntity;

import java.math.BigDecimal;

public interface InvoiceService {

  IPage<InvoiceEntity> listInvoices(Long userId, int page, int size);

  InvoiceEntity applyInvoice(Long userId, Long orderId, String title, String taxId,
                             String email, String type, BigDecimal amount);

  InvoiceEntity getInvoiceDetail(Long id, Long userId);
}
