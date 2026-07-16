package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.common.security.UserContextHolder;
import com.moyuyo.dao.entity.GroupBuyEntity;
import com.moyuyo.dao.entity.GroupBuyMemberEntity;
import com.moyuyo.service.GroupBuyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "拼团活动")
@RestController
@RequestMapping("/api/v1/group-buys")
@RequiredArgsConstructor
public class GroupBuyController {

  private final GroupBuyService groupBuyService;

  @Operation(summary = "拼团活动列表")
  @GetMapping
  public Result<List<GroupBuyEntity>> list() {
    return Result.success(groupBuyService.list());
  }

  @Operation(summary = "拼团活动详情")
  @GetMapping("/{id}")
  public Result<GroupBuyEntity> getById(@PathVariable Long id) {
    return Result.success(groupBuyService.getDetail(id));
  }

  @Operation(summary = "参与拼团")
  @PostMapping("/{id}/join")
  public Result<GroupBuyMemberEntity> join(@PathVariable Long id) {
    return Result.success(groupBuyService.join(id, UserContextHolder.getUserId()));
  }

  @Operation(summary = "拼团成员列表")
  @GetMapping("/{id}/members")
  public Result<List<GroupBuyMemberEntity>> getMembers(@PathVariable Long id) {
    return Result.success(groupBuyService.getMembers(id));
  }
}
