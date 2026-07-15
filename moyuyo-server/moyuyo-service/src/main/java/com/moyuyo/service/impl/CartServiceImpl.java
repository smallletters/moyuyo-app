package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moyuyo.dao.entity.CartEntity;
import com.moyuyo.dao.entity.OrderEntity;
import com.moyuyo.dao.entity.OrderItemEntity;
import com.moyuyo.dao.entity.ProductEntity;
import com.moyuyo.dao.entity.ProductSkuEntity;
import com.moyuyo.dao.mapper.CartMapper;
import com.moyuyo.dao.mapper.ProductMapper;
import com.moyuyo.dao.mapper.ProductSkuMapper;
import com.moyuyo.service.CartService;
import com.moyuyo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

  private final CartMapper cartMapper;
  private final ProductSkuMapper productSkuMapper;
  private final ProductMapper productMapper;
  private final OrderService orderService;

  @Override
  public List<CartEntity> getUserCart(Long userId) {
    return cartMapper.selectList(
        new LambdaQueryWrapper<CartEntity>()
            .eq(CartEntity::getUserId, userId));
  }

  @Override
  @Transactional
  public CartEntity addItem(Long userId, Long skuId, int quantity) {
    CartEntity existing = cartMapper.selectOne(
        new LambdaQueryWrapper<CartEntity>()
            .eq(CartEntity::getUserId, userId)
            .eq(CartEntity::getSkuId, skuId));

    if (existing != null) {
      existing.setQuantity(existing.getQuantity() + quantity);
      cartMapper.updateById(existing);
      return existing;
    }

    CartEntity cart = new CartEntity();
    cart.setUserId(userId);
    cart.setSkuId(skuId);
    cart.setQuantity(quantity);
    cart.setChecked(true);
    cartMapper.insert(cart);
    return cart;
  }

  @Override
  @Transactional
  public CartEntity updateQuantity(Long userId, Long skuId, int quantity) {
    CartEntity existing = cartMapper.selectOne(
        new LambdaQueryWrapper<CartEntity>()
            .eq(CartEntity::getUserId, userId)
            .eq(CartEntity::getSkuId, skuId));

    if (existing == null) {
      throw new IllegalArgumentException("购物车中未找到该商品");
    }

    existing.setQuantity(quantity);
    cartMapper.updateById(existing);
    return existing;
  }

  @Override
  @Transactional
  public void removeItem(Long userId, Long skuId) {
    cartMapper.delete(
        new LambdaQueryWrapper<CartEntity>()
            .eq(CartEntity::getUserId, userId)
            .eq(CartEntity::getSkuId, skuId));
  }

  @Override
  @Transactional
  public void toggleCheck(Long userId, Long skuId, Boolean selected) {
    CartEntity existing = cartMapper.selectOne(
        new LambdaQueryWrapper<CartEntity>()
            .eq(CartEntity::getUserId, userId)
            .eq(CartEntity::getSkuId, skuId));

    if (existing == null) {
      throw new IllegalArgumentException("购物车中未找到该商品");
    }

    existing.setChecked(selected);
    cartMapper.updateById(existing);
  }

  @Override
  @Transactional
  public void toggleCheckAll(Long userId, boolean selected) {
    CartEntity update = new CartEntity();
    update.setChecked(selected);
    cartMapper.update(update,
        new LambdaQueryWrapper<CartEntity>()
            .eq(CartEntity::getUserId, userId));
  }

  @Override
  @Transactional
  public void clear(Long userId) {
    cartMapper.delete(
        new LambdaQueryWrapper<CartEntity>()
            .eq(CartEntity::getUserId, userId));
  }

  @Override
  @Transactional
  public OrderEntity checkout(Long userId, Long addressId, String remark, String couponId) {
    List<CartEntity> selected = cartMapper.selectList(
        new LambdaQueryWrapper<CartEntity>()
            .eq(CartEntity::getUserId, userId)
            .eq(CartEntity::getChecked, true));

    if (selected.isEmpty()) {
      throw new IllegalArgumentException("请选择要结算的商品");
    }

    List<OrderItemEntity> items = new java.util.ArrayList<>();
    for (CartEntity cart : selected) {
      ProductSkuEntity sku = productSkuMapper.selectById(cart.getSkuId());
      if (sku == null) {
        throw new IllegalArgumentException("商品SKU不存在: " + cart.getSkuId());
      }
      ProductEntity product = productMapper.selectById(sku.getProductId());
      if (product == null) {
        throw new IllegalArgumentException("商品不存在: " + sku.getProductId());
      }

      OrderItemEntity item = new OrderItemEntity();
      item.setSkuId(cart.getSkuId());
      item.setProductId(product.getId());
      item.setProductName(product.getName());
      item.setMainImage(product.getMainImage());
      item.setPrice(sku.getPrice());
      item.setQuantity(cart.getQuantity());
      items.add(item);
    }

    OrderEntity order = orderService.createOrder(userId, items, addressId, remark, couponId);

    cartMapper.delete(
        new LambdaQueryWrapper<CartEntity>()
            .eq(CartEntity::getUserId, userId)
            .eq(CartEntity::getChecked, true));

    log.info("Cart checkout: userId={}, orderNo={}, items={}", userId, order.getOrderNo(), items.size());
    return order;
  }
}
