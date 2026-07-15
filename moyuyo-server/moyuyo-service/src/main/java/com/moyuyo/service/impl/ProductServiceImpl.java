package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.dao.entity.ProductEntity;
import com.moyuyo.dao.entity.ProductImageEntity;
import com.moyuyo.dao.entity.ProductSkuEntity;
import com.moyuyo.dao.mapper.ProductImageMapper;
import com.moyuyo.dao.mapper.ProductMapper;
import com.moyuyo.dao.mapper.ProductSkuMapper;
import com.moyuyo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductMapper productMapper;
  private final ProductSkuMapper productSkuMapper;
  private final ProductImageMapper productImageMapper;

  @Override
  public Page<ProductEntity> listProducts(int page, int size, Long categoryId, String sortBy, String sortOrder, String keyword, Long brandIpId) {
    LambdaQueryWrapper<ProductEntity> wrapper = new LambdaQueryWrapper<ProductEntity>()
        .eq(categoryId != null, ProductEntity::getCategoryId, categoryId)
        .eq(brandIpId != null, ProductEntity::getBrandIpId, brandIpId)
        .eq(ProductEntity::getOnSale, true)
        .like(StringUtils.isNotBlank(keyword), ProductEntity::getName, keyword);

    if (StringUtils.isNotBlank(sortBy)) {
      boolean asc = !"desc".equalsIgnoreCase(sortOrder);
      if ("price".equalsIgnoreCase(sortBy)) {
        wrapper.orderBy(true, asc, ProductEntity::getPrice);
      } else if ("createTime".equalsIgnoreCase(sortBy) || "create_time".equalsIgnoreCase(sortBy)) {
        wrapper.orderBy(true, asc, ProductEntity::getCreateTime);
      } else if ("sales".equalsIgnoreCase(sortBy)) {
        wrapper.orderBy(true, asc, ProductEntity::getSales);
      }
    } else {
      wrapper.orderByDesc(ProductEntity::getCreateTime);
    }

    return productMapper.selectPage(new Page<>(page, size), wrapper);
  }

  @Override
  public ProductEntity getProductDetail(Long productId) {
    ProductEntity product = productMapper.selectById(productId);
    if (product == null) {
      throw new IllegalArgumentException("商品不存在");
    }
    return product;
  }

  @Override
  public List<ProductSkuEntity> getSkusByProductId(Long productId) {
    return productSkuMapper.selectList(
        new LambdaQueryWrapper<ProductSkuEntity>()
            .eq(ProductSkuEntity::getProductId, productId));
  }

  @Override
  public List<ProductImageEntity> getImagesByProductId(Long productId) {
    return productImageMapper.selectList(
        new LambdaQueryWrapper<ProductImageEntity>()
            .eq(ProductImageEntity::getProductId, productId)
            .orderByAsc(ProductImageEntity::getSort));
  }

  @Override
  public ProductEntity getProductWithDetails(Long productId) {
    ProductEntity product = getProductDetail(productId);
    List<ProductSkuEntity> skus = getSkusByProductId(productId);
    List<ProductImageEntity> images = getImagesByProductId(productId);
    product.setSkus(skus);
    product.setImages(images);
    return product;
  }
}
