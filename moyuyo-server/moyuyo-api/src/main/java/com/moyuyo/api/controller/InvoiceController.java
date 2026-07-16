package com.moyuyo.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moyuyo.common.Result;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.InvoiceEntity;
import com.moyuyo.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "发票管理")
@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

  private final InvoiceService invoiceService;

  @Operation(summary = "发票列表")
  @GetMapping
  public Result<IPage<InvoiceEntity>> list(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    return Result.success(invoiceService.listInvoices(UserContextHolder.getUserId(), page, size));
  }

  @Operation(summary = "申请发票")
  @PostMapping
  public Result<InvoiceEntity> apply(@RequestParam Long orderId,
                                     @RequestParam String title,
                                     @RequestParam String taxId,
                                     @RequestParam String email,
                                     @RequestParam String type,
                                     @RequestParam BigDecimal amount) {
    return Result.success(invoiceService.applyInvoice(
        UserContextHolder.getUserId(), orderId, title, taxId, email, type, amount));
  }

  @Operation(summary = "发票详情")
  @GetMapping("/{id}")
  public Result<InvoiceEntity> getDetail(@PathVariable Long id) {
    return Result.success(invoiceService.getInvoiceDetail(id, UserContextHolder.getUserId()));
  }
}
