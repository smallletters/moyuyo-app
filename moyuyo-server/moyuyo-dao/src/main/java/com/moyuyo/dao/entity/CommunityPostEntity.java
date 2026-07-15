package com.moyuyo.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mo_community_post")
public class CommunityPostEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private String content;

    private String images;

    private String topic;

    private Integer likes;

    private Integer comments;

    private String status;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
