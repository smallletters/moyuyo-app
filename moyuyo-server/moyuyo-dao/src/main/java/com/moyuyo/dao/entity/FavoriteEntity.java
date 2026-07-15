package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_favorites")
public class FavoriteEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private Long productId;

    private Long groupId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
