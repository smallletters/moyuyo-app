package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.dao.entity.InvoiceEntity;
import com.moyuyo.dao.mapper.InvoiceMapper;
import com.moyuyo.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

  private final InvoiceMapper invoiceMapper;

  @Override
  public IPage<InvoiceEntity> listInvoices(Long userId, int page, int size) {
    return invoiceMapper.selectPage(new Page<>(page, size),
        new LambdaQueryWrapper<InvoiceEntity>()
            .eq(InvoiceEntity::getUserId, userId)
            .orderByDesc(InvoiceEntity::getCreateTime));
  }

  @Override
  @Transactional
  public InvoiceEntity applyInvoice(Long userId, Long orderId, String title, String taxId,
                                    String email, String type, BigDecimal amount) {
    InvoiceEntity entity = new InvoiceEntity();
    entity.setUserId(userId);
    entity.setOrderId(orderId);
    entity.setTitle(title);
    entity.setTaxId(taxId);
    entity.setEmail(email);
    entity.setType(type);
    entity.setAmount(amount);
    entity.setStatus("PENDING");
    invoiceMapper.insert(entity);
    log.info("Invoice applied: userId={}, orderId={}, type={}", userId, orderId, type);
    return entity;
  }

  @Override
  public InvoiceEntity getInvoiceDetail(Long id, Long userId) {
    InvoiceEntity entity = invoiceMapper.selectById(id);
    if (entity == null) {
      throw new IllegalArgumentException("发票申请不存在");
    }
    if (!Objects.equals(entity.getUserId(), userId)) {
      throw new IllegalArgumentException("无权访问该发票");
    }
    return entity;
  }
}
