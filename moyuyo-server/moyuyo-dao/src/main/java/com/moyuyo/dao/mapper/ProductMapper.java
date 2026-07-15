package com.moyuyo.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.dao.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper extends BaseMapper<ProductEntity> {

    /**
     * 分页查询上架商品（按创建时间倒序）
     */
    Page<ProductEntity> selectOnSalePage(IPage<ProductEntity> page);

    /**
     * 按分类分页查询商品
     */
    Page<ProductEntity> selectPageByCategory(IPage<ProductEntity> page, @Param("categoryId") Long categoryId);

    /**
     * 按关键词模糊搜索商品名称
     */
    Page<ProductEntity> selectPageByKeyword(IPage<ProductEntity> page, @Param("keyword") String keyword);
}
