package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("mo_category")
public class CategoryEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long parentId;

    private String name;

    private String icon;

    private Integer sort;

    private Integer level;

    private Long collectionId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private List<CategoryEntity> children;
}
