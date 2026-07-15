package com.moyuyo.service;

import com.moyuyo.dao.entity.CategoryEntity;

import java.util.List;

public interface CategoryService {

  List<CategoryEntity> getCategoryTree();

  List<CategoryEntity> getCategoryByParent(Long parentId);
}
