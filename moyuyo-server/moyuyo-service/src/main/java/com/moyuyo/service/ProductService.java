package com.moyuyo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.dao.entity.ProductEntity;
import com.moyuyo.dao.entity.ProductImageEntity;
import com.moyuyo.dao.entity.ProductSkuEntity;

import java.util.List;

public interface ProductService {

  Page<ProductEntity> listProducts(int page, int size, Long categoryId, String sortBy, String sortOrder, String keyword, Long brandIpId);

  ProductEntity getProductDetail(Long productId);

  List<ProductSkuEntity> getSkusByProductId(Long productId);

  List<ProductImageEntity> getImagesByProductId(Long productId);

  ProductEntity getProductWithDetails(Long productId);
}
