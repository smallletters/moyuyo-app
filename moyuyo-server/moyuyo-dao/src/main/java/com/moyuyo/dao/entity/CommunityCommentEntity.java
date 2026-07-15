package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_community_comment")
public class CommunityCommentEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long postId;

    private Long userId;

    private Long parentId;

    private String content;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
