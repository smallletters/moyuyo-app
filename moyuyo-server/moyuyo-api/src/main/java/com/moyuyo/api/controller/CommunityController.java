package com.moyuyo.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.common.Result;
import com.moyuyo.common.dto.community.CommunityPostVO;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.service.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "社区管理")
@RestController
@RequestMapping("/api/v1/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @Operation(summary = "帖子列表（公开）")
    @GetMapping("/posts")
    public Result<Page<CommunityPostVO>> listPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String topic) {
        return Result.success(communityService.listPosts(topic, page, size));
    }

    @Operation(summary = "帖子详情")
    @GetMapping("/posts/{id}")
    public Result<CommunityPostVO> getPostDetail(@PathVariable Long id) {
        Long userId = UserContextHolder.getUserId();
        return Result.success(communityService.getPostDetail(id, userId));
    }

    @Operation(summary = "发布帖子")
    @PostMapping("/posts")
    public Result<CommunityPostVO> createPost(
            @RequestParam String content,
            @RequestParam(required = false) List<String> images,
            @RequestParam(required = false) String topic) {
        return Result.success(communityService.createPost(
                UserContextHolder.getUserId(), content, images, topic));
    }

    @Operation(summary = "我的帖子")
    @GetMapping("/posts/mine")
    public Result<Page<CommunityPostVO>> myPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.success(communityService.listMyPosts(UserContextHolder.getUserId(), page, size));
    }

    @Operation(summary = "点赞")
    @PostMapping("/posts/{id}/like")
    public Result<Void> likePost(@PathVariable Long id) {
        communityService.likePost(UserContextHolder.getUserId(), id);
        return Result.success();
    }

    @Operation(summary = "取消点赞")
    @DeleteMapping("/posts/{id}/like")
    public Result<Void> unlikePost(@PathVariable Long id) {
        communityService.unlikePost(UserContextHolder.getUserId(), id);
        return Result.success();
    }

    @Operation(summary = "评论")
    @PostMapping("/posts/{postId}/comments")
    public Result<Void> addComment(
            @PathVariable Long postId,
            @RequestParam String content,
            @RequestParam(required = false) Long parentId) {
        communityService.addComment(UserContextHolder.getUserId(), postId, parentId, content);
        return Result.success();
    }
}
