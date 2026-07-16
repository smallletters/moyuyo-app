package com.moyuyo.service.impl;

import com.moyuyo.dao.entity.OrderEntity;
import com.moyuyo.dao.entity.OrderItemEntity;
import com.moyuyo.dao.entity.ProductEntity;
import com.moyuyo.dao.entity.CategoryEntity;
import com.moyuyo.dao.mapper.OrderMapper;
import com.moyuyo.dao.mapper.OrderItemMapper;
import com.moyuyo.dao.mapper.ProductMapper;
import com.moyuyo.dao.mapper.CategoryMapper;
import com.moyuyo.service.WooCommerceSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WooCommerceSyncServiceImpl implements WooCommerceSyncService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final WooCommerceClient client;

    @Override
    @Transactional
    public void syncOrderToWooCommerce(OrderEntity order) {
        if (order == null) return;
        if (order.getWooOrderId() != null) {
            log.info("Order already synced: orderNo={}, wooOrderId={}",
                    order.getOrderNo(), order.getWooOrderId());
            return;
        }

        try {
            Map<String, Object> orderData = buildOrderData(order);
            Map<String, Object> created = client.createOrder(orderData);
            Number wooId = (Number) created.get("id");

            order.setWooOrderId(wooId.longValue());
            order.setSyncStatus(1);
            order.setSyncRetryCount(0);
            order.setSyncLastTime(LocalDateTime.now());
            orderMapper.updateById(order);

            log.info("WooCommerce sync success: orderNo={}, wooOrderId={}",
                    order.getOrderNo(), wooId);
        } catch (Exception e) {
            order.setSyncStatus(-1);
            order.setSyncRetryCount(order.getSyncRetryCount() == null ? 1 : order.getSyncRetryCount() + 1);
            order.setSyncLastTime(LocalDateTime.now());
            orderMapper.updateById(order);
            log.error("WooCommerce sync failed: orderNo={}", order.getOrderNo(), e);
        }
    }

    @Override
    public void syncOrderStatus(Long wooOrderId, String status) {
        if (wooOrderId == null) return;
        try {
            client.updateOrderStatus(wooOrderId.intValue(), status);
            log.info("WooCommerce status sync: wooOrderId={}, status={}", wooOrderId, status);
        } catch (Exception e) {
            log.error("WooCommerce status sync failed: wooOrderId={}", wooOrderId, e);
        }
    }

    @Override
    public void syncAllPendingOrders() {
        List<OrderEntity> pending = orderMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OrderEntity>()
                        .eq(OrderEntity::getSyncStatus, -1)
                        .or()
                        .isNull(OrderEntity::getWooOrderId));
        for (OrderEntity order : pending) {
            syncOrderToWooCommerce(order);
        }
        log.info("WooCommerce sync all pending: count={}", pending.size());
    }

    // ========== Product Sync ==========

    @Override
    public void syncProductsFromWooCommerce() {
        try {
            int page = 1;
            int perPage = 100;
            int totalSynced = 0;

            while (true) {
                List<Map<String, Object>> products = client.getProducts(page, perPage);
                if (products.isEmpty()) break;

                for (Map<String, Object> wp : products) {
                    try {
                        syncSingleProduct(wp);
                        totalSynced++;
                    } catch (Exception e) {
                        log.error("Failed to sync product: id={}", wp.get("id"), e);
                    }
                }
                page++;
            }
            log.info("WooCommerce product sync completed: total={}", totalSynced);
        } catch (Exception e) {
            log.error("WooCommerce product sync failed", e);
        }
    }

    private void syncSingleProduct(Map<String, Object> wp) {
        Number wooId = (Number) wp.get("id");
        String name = (String) wp.get("name");
        String description = (String) wp.get("description");
        String status = (String) wp.get("status");

        ProductEntity existing = productMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ProductEntity>()
                        .eq(ProductEntity::getWooProductId, wooId.longValue()));

        ProductEntity product = existing != null ? existing : new ProductEntity();
        if (existing == null) {
            product.setWooProductId(wooId.longValue());
        }
        product.setName(name);
        product.setDetail(description);

        // Sync categories
        List<Map<String, Object>> categories = (List<Map<String, Object>>) wp.get("categories");
        if (categories != null && !categories.isEmpty()) {
            Number catId = (Number) categories.get(0).get("id");
            product.setCategoryId(catId.longValue());
            syncCategory(categories.get(0));
        }

        // Sync prices
        String regularPrice = (String) wp.get("regular_price");
        String salePrice = (String) wp.get("sale_price");
        if (regularPrice != null) {
            product.setOriginalPrice(new BigDecimal(regularPrice));
        }
        if (salePrice != null && !salePrice.isBlank()) {
            product.setPrice(new BigDecimal(salePrice));
        } else if (regularPrice != null) {
            product.setPrice(new BigDecimal(regularPrice));
        }

        // Sync stock
        Number stockQuantity = (Number) wp.get("stock_quantity");
        product.setStock(stockQuantity != null ? stockQuantity.intValue() : 0);

        // Sync images
        List<Map<String, Object>> images = (List<Map<String, Object>>) wp.get("images");
        if (images != null && !images.isEmpty()) {
            product.setMainImage((String) images.get(0).get("src"));
        }

        product.setOnSale("publish".equals(status));
        if (existing == null) {
            product.setCreateTime(LocalDateTime.now());
        }
        product.setUpdateTime(LocalDateTime.now());

        if (existing != null) {
            productMapper.updateById(product);
        } else {
            productMapper.insert(product);
        }
    }

    private void syncCategory(Map<String, Object> wcCat) {
        try {
            Number catId = (Number) wcCat.get("id");
            String catName = (String) wcCat.get("name");
            CategoryEntity existing = categoryMapper.selectById(catId.longValue());
            if (existing == null) {
                CategoryEntity category = new CategoryEntity();
                category.setId(catId.longValue());
                category.setName(catName);
                category.setCreateTime(LocalDateTime.now());
                categoryMapper.insert(category);
                log.info("Synced category: id={}, name={}", catId, catName);
            }
        } catch (Exception e) {
            log.warn("Failed to sync category", e);
        }
    }

    @Override
    public void syncCategoriesFromWooCommerce() {
        try {
            List<Map<String, Object>> categories = client.getCategories();
            int synced = 0;
            for (Map<String, Object> wcCat : categories) {
                syncCategory(wcCat);
                synced++;
            }
            log.info("WooCommerce category sync completed: total={}", synced);
        } catch (Exception e) {
            log.error("WooCommerce category sync failed", e);
        }
    }

    // ========== Helpers ==========

    private Map<String, Object> buildOrderData(OrderEntity order) {
        Map<String, Object> data = new LinkedHashMap<>();

        // Customer
        Map<String, Object> billing = new LinkedHashMap<>();
        billing.put("first_name", order.getReceiverName());
        billing.put("phone", order.getReceiverPhone());
        billing.put("address_1", order.getReceiverAddress());
        billing.put("postcode", order.getReceiverZip());
        data.put("billing", billing);

        Map<String, Object> shipping = new LinkedHashMap<>(billing);
        data.put("shipping", shipping);

        // Status mapping
        String status = switch (order.getStatus()) {
            case "PENDING_PAY" -> "pending";
            case "PENDING_SHIP" -> "processing";
            case "SHIPPED", "PENDING_RECEIVE" -> "completed";
            case "CANCELLED" -> "cancelled";
            case "REFUNDING" -> "refunded";
            default -> "pending";
        };
        data.put("status", status);

        // Line items
        List<Map<String, Object>> lineItems = new ArrayList<>();
        List<OrderItemEntity> items = orderItemMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OrderItemEntity>()
                        .eq(OrderItemEntity::getOrderId, order.getId()));
        for (OrderItemEntity item : items) {
            Map<String, Object> li = new LinkedHashMap<>();
            li.put("product_id", item.getProductId());
            li.put("quantity", item.getQuantity());
            li.put("price", item.getPrice().toString());
            li.put("name", item.getProductName());
            lineItems.add(li);
        }
        data.put("line_items", lineItems);

        data.put("customer_note", "Synced from MOYUYO app. Order #" + order.getOrderNo());
        data.put("set_paid", true);

        return data;
    }
}
