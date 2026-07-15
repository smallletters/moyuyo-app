package com.moyuyo.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moyuyo.common.Result;
import com.moyuyo.common.dto.order.CancelOrderRequest;
import com.moyuyo.common.dto.order.CreateOrderRequest;
import com.moyuyo.common.dto.order.OrderItemRequest;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.OrderEntity;
import com.moyuyo.dao.entity.OrderItemEntity;
import com.moyuyo.dao.entity.ProductEntity;
import com.moyuyo.dao.entity.ProductSkuEntity;
import com.moyuyo.dao.mapper.ProductMapper;
import com.moyuyo.dao.mapper.ProductSkuMapper;
import com.moyuyo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "订单管理")
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final ProductSkuMapper productSkuMapper;
  private final ProductMapper productMapper;

  @Operation(summary = "创建订单")
  @PostMapping
  public Result<OrderEntity> createOrder(@RequestBody CreateOrderRequest request) {
    Long userId = UserContextHolder.getUserId();

    // 将请求中的商品信息转换为订单项实体
    List<OrderItemEntity> items = new ArrayList<>();
    for (OrderItemRequest itemReq : request.getItems()) {
      ProductSkuEntity sku = productSkuMapper.selectById(itemReq.getSkuId());
      if (sku == null) {
        return Result.error("SKU不存在: " + itemReq.getSkuId());
      }
      ProductEntity product = productMapper.selectById(itemReq.getProductId());
      if (product == null) {
        return Result.error("商品不存在: " + itemReq.getProductId());
      }

      OrderItemEntity item = new OrderItemEntity();
      item.setSkuId(itemReq.getSkuId());
      item.setProductId(itemReq.getProductId());
      item.setProductName(product.getName());
      item.setMainImage(product.getMainImage());
      item.setPrice(sku.getPrice());
      item.setQuantity(itemReq.getQuantity());
      items.add(item);
    }

    OrderEntity order = orderService.createOrder(userId, items, request.getAddressId(), request.getRemark(), request.getCouponId());
    return Result.success(order);
  }

  @Operation(summary = "获取订单列表")
  @GetMapping
  public Result<IPage<OrderEntity>> listOrders(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String status) {
    Long userId = UserContextHolder.getUserId();
    IPage<OrderEntity> orderPage = orderService.listOrders(userId, page, size, status);

    // 填充每个订单的订单项
    for (OrderEntity order : orderPage.getRecords()) {
      order.setItems(orderService.getOrderItems(order.getId()));
    }

    return Result.success(orderPage);
  }

  @Operation(summary = "获取订单详情")
  @GetMapping("/{id}")
  public Result<OrderEntity> getOrderDetail(@PathVariable Long id) {
    Long userId = UserContextHolder.getUserId();
    OrderEntity order = orderService.getOrderDetail(id, userId);
    order.setItems(orderService.getOrderItems(id));
    return Result.success(order);
  }

  @Operation(summary = "取消订单")
  @PostMapping("/{id}/cancel")
  public Result<Void> cancelOrder(@PathVariable Long id, @RequestBody CancelOrderRequest request) {
    Long userId = UserContextHolder.getUserId();
    orderService.cancelOrder(id, userId, request.getReason());
    return Result.success();
  }

  @Operation(summary = "确认收货")
  @PostMapping("/{id}/confirm")
  public Result<Void> confirmReceived(@PathVariable Long id) {
    Long userId = UserContextHolder.getUserId();
    orderService.confirmReceived(id, userId);
    return Result.success();
  }

  @Operation(summary = "删除订单")
  @DeleteMapping("/{id}")
  public Result<Void> deleteOrder(@PathVariable Long id) {
    Long userId = UserContextHolder.getUserId();
    orderService.deleteOrder(id, userId);
    return Result.success();
  }

  @Operation(summary = "支付回调（供支付网关调用）")
  @PostMapping("/pay/callback")
  public Result<Void> payCallback(
      @RequestParam String orderNo,
      @RequestParam String payChannel,
      @RequestParam String transactionId) {
    orderService.payCallback(orderNo, payChannel, transactionId);
    return Result.success();
  }
}
