package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_community_like")
public class CommunityLikeEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long postId;

    private Long userId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
