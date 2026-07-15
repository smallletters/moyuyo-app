package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_favorites_group")
public class FavoriteGroupEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private String name;

    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
