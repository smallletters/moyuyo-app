package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moyuyo.dao.entity.CategoryEntity;
import com.moyuyo.dao.mapper.CategoryMapper;
import com.moyuyo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryMapper categoryMapper;

  @Override
  public List<CategoryEntity> getCategoryTree() {
    List<CategoryEntity> all = categoryMapper.selectList(
        new LambdaQueryWrapper<CategoryEntity>()
            .orderByAsc(CategoryEntity::getSort));

    Map<Long, List<CategoryEntity>> childrenMap = all.stream()
        .collect(Collectors.groupingBy(c -> c.getParentId() != null ? c.getParentId() : 0L));

    List<CategoryEntity> roots = childrenMap.getOrDefault(0L, new ArrayList<>());
    fillChildren(roots, childrenMap);
    return roots;
  }

  private void fillChildren(List<CategoryEntity> parents, Map<Long, List<CategoryEntity>> childrenMap) {
    for (CategoryEntity parent : parents) {
      List<CategoryEntity> children = childrenMap.getOrDefault(parent.getId(), new ArrayList<>());
      parent.setChildren(children);
      fillChildren(children, childrenMap);
    }
  }

  @Override
  public List<CategoryEntity> getCategoryByParent(Long parentId) {
    return categoryMapper.selectList(
        new LambdaQueryWrapper<CategoryEntity>()
            .eq(CategoryEntity::getParentId, parentId)
            .orderByAsc(CategoryEntity::getSort));
  }
}
